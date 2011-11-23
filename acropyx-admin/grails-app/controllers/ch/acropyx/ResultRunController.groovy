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

class ResultRunController {

    def index = {
        def activeRun = Run.findByStartTimeIsNotNullAndEndTimeIsNull()
        if (activeRun) {
            redirect(action: "show", id: activeRun.id)
        }
        else {
            flash.message = "No active run"
            render(view: 'show');
        }
    }

    def show = {
        def runInstance = Run.get(params.id)
        def flights
        if (params.notEndedFlights) {
            flights = runInstance.flights.sort { flight ->
                flight.computeResult(flight.computeDetailedResults())
            }.reverse()
        }
        else {
            flights = runInstance.findEndedFlights(true)
        }
        return [runInstance: runInstance, flights: flights]
    }
}
