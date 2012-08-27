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

@MultiTenant
class Mark {

    Flight flight
    Judge judge

    MarkDefinition markDefinition
    float mark

    static belongsTo = [flight: Flight]

    static constraints = {
        flight(blank: false)
        judge(blank: false)
        markDefinition(blank: false)
        mark(blank: false, range:0..10)
    }

    def static searchMark(Flight flight, Judge judge, MarkDefinition markDefinition) {
        def criteria = Mark.createCriteria()
        criteria.get() {
            eq("flight", flight)
            eq("judge", judge)
            eq("markDefinition", markDefinition)
        }
    }

    def String toString() {
        //sprintf('%s=%d', markDefinition, mark)
        sprintf('%s=%f', markDefinition, mark)
    }
}
