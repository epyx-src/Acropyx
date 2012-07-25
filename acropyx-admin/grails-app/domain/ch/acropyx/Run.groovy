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
class Run implements Comparable {

    String name
    Date startTime
    Date endTime

    Competition competition
    static hasMany = [ flights : Flight ]

    static constraints = {
        competition(blank:false)
        name(blank: false)
        startTime(nullable: true, format: ConfigurationHolder.config.ch.acropyx.dateFormat)
        endTime(nullable: true, format: ConfigurationHolder.config.ch.acropyx.dateFormat)
    }

    def int compareTo(Object object) {
        return id.compareTo(object.id);
    }

    static transients = ["active", "started", "ended"]

    def boolean isActive() {
        return (startTime != null) && (endTime == null)
    }

    def boolean isStarted() {
        return (startTime != null)
    }

    def boolean isEnded() {
        return (startTime != null) && (endTime != null)
    }

    def start() {
        if (isActive()) {
            throw new RuntimeException("Run '${this}' is already started");
        }
        if (competition.isActive() == false) {
            throw new RuntimeException("The run must belong to an active competition");
        }
        if (Run.countByStartTimeIsNotNullAndEndTimeIsNull() >= 1) {
            throw new RuntimeException("Maximum 1 active run");
        }

        if (!startTime) {
            startTime = new Date()
        }
        endTime = null
    }

    def end() {
        if (startTime == null) {
            throw new RuntimeException("Run '${this}' is not yet started");
        }
        if (endTime != null) {
            throw new RuntimeException("Run '${this}' is already terminated");
        }
        endTime = new Date();
    }

    def findEndedFlights(boolean sortByResult = false) {
        def endedFlights = flights.findAll { it.isEnded() }

        if (sortByResult) {
            return endedFlights.sort { flight ->
                flight.computeResult(flight.computeDetailedResults())
            }.reverse()
        }

        return endedFlights
    }
    
    def startingOrder(){
        //Is first Run
        
        def endedRuns = run.competition.findEndedRuns()
        if (endedRuns.length == 0)
        {
            return Pilot.list.sort(it.selection)
        }
        
        def lastRun = endedRuns[-1]
        
        def competitors = Competitor.competitorsForActiveRun()
        
        return competitors.sort { competitor -> lastRun.getCompetitorPlace(competitor.id)}
    }
    
    def getCompetitorPlace(Competitor){
        findEndedFlights
    }
    
    

    def String toString() {
        sprintf("%s:%s", competition?.code, name);
    }
}
