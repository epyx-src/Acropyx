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
class Competitor {

    String name
    String bestResult
    int rank;

    def String toString() {
        return name;
    }

    static constraints = { bestResult(nullable:true) rank(nullable:true) }

    def static List competitorsForActiveRun() {
        if ( Run.countByStartTimeIsNotNullAndEndTimeIsNull() == 1 ) {
            if ( Run.findByStartTimeIsNotNullAndEndTimeIsNull().competition.type == Competition.Type.Solo ) {
                return Pilot.listOrderByRank();
            }
            else {
                return Team.list()
            }
        }
        []
    }
}
