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

import java.text.DecimalFormat
import java.math.RoundingMode

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
    def reportRunResults = {
        def run = Run.list()
        def runInstance = Run.get(params.run_id)

        def flights = runInstance.findEndedFlights(true)
        def labels = [:]
        def fields = []

        def resultList = []

        def maxLocationIndex=0;

        flights.eachWithIndex { flight, i ->
            def expanded_record = ["rank": i+1]
            if (!fields.contains("rank")){
                fields.add("rank")
                labels["rank"] = "Rank"
            }
            expanded_record["competitor"] =  flight.competitor.name
            if (!fields.contains("competitor")){
                fields.add("competitor")
                labels["competitor"] = "Competitor"
            }
            def detailedResults = flight.computeDetailedResults()
            runInstance.competition.markCoefficients.eachWithIndex { markCoefficient, y ->
                def markName = markCoefficient.markDefinition.name
                expanded_record["${markName}"] = roundMark(detailedResults.get(markCoefficient.id))
                if (!fields.contains("${markName}")){
                    fields.add("${markName}")
                    labels["${markName}"] = "${markName}"
                }

            }
            expanded_record["Result"] =  roundMark(flight.computeResult(detailedResults))
            if (!fields.contains("Result")){
                fields.add("Result")
                labels["Result"] = "Result"
            }

            resultList.add expanded_record
        }

        //exportService.export("pdf", response.outputStream, resultList, fields, labels, [:], [:])

        //def run = Run.list()

        params.ACROPYX_COMPETITION = runInstance.competition.toString()
        params.ACROPYX_RUN = runInstance.toString()
        params.ACROPYX_RESULT = (runInstance.isEnded())? "Final results": "Intermediate results"
        chain(controller:'jasper',action:'index',model:[data:resultList],params:params)
    }

    def double roundMark(mark) {
        def decimalFormat = new DecimalFormat("#.###")
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP)
        def markString = decimalFormat.format(mark)
        return markString as double
    }
}
