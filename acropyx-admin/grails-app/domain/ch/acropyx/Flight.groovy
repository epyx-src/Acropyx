/*******************************************************************************
 * Copyright (c) 2011 epyx SA.
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License along
 * with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package ch.acropyx

import grails.plugin.multitenant.core.groovy.compiler.MultiTenant

import java.util.Date

import org.codehaus.groovy.grails.commons.ConfigurationHolder

@MultiTenant
class Flight {

    Date startTime
    Date endTime

    Competitor competitor
    Run run

    List manoeuvres
    int warnings

    static hasMany = [manoeuvres: Manoeuvre, marks: Mark]

    static constraints = {
        competitor(blank: false)
        run(blank: false, unique: 'competitor')
        startTime(nullable: true, format: ConfigurationHolder.config.ch.acropyx.dateFormat)
        endTime(nullable: true, format: ConfigurationHolder.config.ch.acropyx.dateFormat)
        warnings(range:0..3)
    }

    static transients = ["active", "ended"]

    def boolean isActive() {
        return (startTime != null) && (endTime == null)
    }

    def boolean isEnded() {
        return (startTime != null) && (endTime != null)
    }

    def start() {
        if (endTime != null) {
            throw new RuntimeException("Flight '${this}' is terminated");
        }
        if (startTime != null) {
            throw new RuntimeException("Flight '${this}' is already started");
        }
        if ((run == null) || (run.isActive() == false)) {
            throw new RuntimeException("There is no active run");
        }
        if (Flight.countByStartTimeIsNotNullAndEndTimeIsNull() >= 1) {
            throw new RuntimeException("Maximum 1 active flight");
        }

        startTime = new Date();
    }

    def end() {
        if (startTime == null) {
            throw new RuntimeException("Flight '${this}' is not yet started");
        }
        if (endTime != null) {
            throw new RuntimeException("Flight '${this}' is already terminated");
        }
        endTime = new Date();
    }

    def addWarning() {
        warnings++
        if (warnings > 3) {
            warnings = 3;
        }
    }

    def removeWarning() {
        warnings--
        if (warnings < 0) {
            warnings = 0;
        }
    }

    def Map computeDetailedResults() {
        def competition = run.competition
        def markCoefficients = competition.markCoefficients

        def FAIs = competition.judges.findAll { it.role == Judge.Role.FAI }
        def VIPs = competition.judges.findAll { it.role == Judge.Role.VIP }

        def manoeuvresSum = 0.0d
        manoeuvres.each() { manoeuvre ->
            manoeuvresSum += manoeuvre.coefficient
        }
        def manoeuvresMean = 0.0d
        if ( manoeuvres.size() > 0 ) {
            manoeuvresMean = manoeuvresSum / manoeuvres.size()
        }

        def detailedResults = [:]
        markCoefficients.each() { markCoefficient ->
            def markDefinition = markCoefficient.markDefinition

            // FAI judges
            def FAISum = 0d
            FAIs.each { judge ->
                def mark = Mark.searchMark(this, judge, markDefinition)
                if (mark) {
                    if (markDefinition.name == "Technical expression") {
                        FAISum += mark.mark * manoeuvresMean
                    }
                    else {
                        FAISum += mark.mark
                    }
                }
            }
            def FAIMark = FAISum / FAIs.size()

            // VIP judges
            def VIPSum = 0d
            VIPs.each { judge ->
                def mark = Mark.searchMark(this, judge, markDefinition)
                if (mark) {
                    if (markDefinition.name == "Technical expression") {
                        VIPSum += mark.mark * manoeuvresMean
                    }
                    else {
                        VIPSum += mark.mark
                    }
                }
            }
            def VIPMark = VIPSum / VIPs.size()

            def mark;
            if ((FAIs.size() > 0) && (VIPs.size() > 0)) {
                mark = FAIMark * 0.8d + VIPMark * 0.2d
            } else if (FAIs.size() > 0) {
                mark = FAIMark
            }  else if (VIPs.size() > 0) {
                mark = VIPMark
            } else {
                mark = 0d
            }

            detailedResults.put(markCoefficient.id, mark)
        }

        return detailedResults
    }

    def double computeResult(Map detailedResults) {
        def sum = 0d
        detailedResults.each { key, value ->
            def markCoefficient = MarkCoefficient.get(key)
            sum += value * markCoefficient.coefficient
        }

        if (warnings == 1) {
            sum = sum - 1
        }
        else if (warnings == 2) {
            sum = sum - 3
        }
        else if (warnings == 3) {
            sum = 0;
        }

        return (sum > 0 ? sum : 0)
    }

    def String toString() {
        return sprintf('%s - %s', competitor?.name, run.toString());
    }
}
