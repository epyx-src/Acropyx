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

class ResultCompetitionController {

    def index = {
        def activeCompetition = Competition.findByStartTimeIsNotNullAndEndTimeIsNull()
        if (activeCompetition) {
            redirect(action: "show", id: activeCompetition.id)
        }
        else {
            flash.message = "No active competition"
            render(view: 'show');
        }
    }

    def show = {
        def competitionInstance = Competition.get(params.id)

        return [competitionInstance: competitionInstance, competitionId: competitionInstance.id,  endedRuns: competitionInstance.findStartedRuns(), competitorResults: competitionInstance.computeResults()]
    }

    def reportCompetitionResults = {

        def competitionInstance = Competition.get(params.competition_id)

        def competitionResults = competitionInstance.computeResults()

        def runs = competitionInstance.findStartedRuns()

        def labels = [:]
        def fields = []
        def  resultList = []
        competitionResults.eachWithIndex{ result, i ->
            //add rank
            def expanded_record = ["rank": i+1]
            if (!fields.contains("rank")){
                fields.add("rank")
                labels["rank"] = "Rank"
            }
            //Add Competitor
            expanded_record["competitor"] =  result.competitor
            if (!fields.contains("competitor")){
                fields.add("competitor")
                labels["competitor"] = "Competitor"
            }

            //Add warnings
            expanded_record["Warnings"] =  (int)result.warnings
            if (!fields.contains("Warnings")){
                fields.add("Warnings")
                labels["Warnings"] = "Warnings"
            }

            if (result.competitor instanceof Pilot){
                def flight_pilot = result.competitor as Pilot
                def pilot = Pilot.get(flight_pilot.id)
                expanded_record["Country"] =  pilot.country
                if (!fields.contains("Country")){
                    fields.add("Country")
                    labels["Country"] = "Country"
                }

                expanded_record["Glider"] =  pilot.glider
                if (!fields.contains("Glider")){
                    fields.add("Glider")
                    labels["Glider"] = "Glider"
                }
            }



            //add overall
            expanded_record["Result"] =  roundMark(result.overall)
            if (!fields.contains("Result")){
                fields.add("Result")
                labels["Result"] = "Result"
            }

            //Add Run results
            runs.eachWithIndex { run, j ->
                def runResult =  result.flights?.get(run.id)
                expanded_record["r" + (j+1).toString()] = (runResult)?roundMark(runResult.result):""
                if (!fields.contains("r" + (j+1).toString())){
                    fields.add("r" + (j+1).toString())
                    labels["r" + j.toString()] = (runResult)?"r" + (j+1).toString():""
                }
                params["ACROPYX_RUN_TITLE_" + (j+1).toString()]   = run.name
            }


            resultList.add expanded_record
        }


        params.ACROPYX_COMPETITION = competitionInstance.name
        params.ACROPYX_RESULT = (competitionInstance.isEnded())? "Final ranking": "Intermediate ranking"
        chain(controller:'jasper',action:'index',model:[data:resultList],params:params)

    }

    def  roundMark(mark) {
        def decimalFormat = new DecimalFormat("0.000")
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP)
        return decimalFormat.format(mark)
        //return markString as double
    }
}
