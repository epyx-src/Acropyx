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
import org.grails.plugins.csv.CSVWriter



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
        def isSolo = (competitionInstance.type == Competition.Type.Solo)
        return [competitionInstance: competitionInstance, isSolo: isSolo, competitionId: competitionInstance.id,  endedRuns: competitionInstance.findStartedRuns(), competitorResults: competitionInstance.computeResults()]
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
            else{
                def team= result.competitor as Team
                def pilot = team.pilots.asList().get(0);
                def pilot1 = team.pilots.asList().get(1);


                expanded_record["Pilot"] =  pilot.name
                if (!fields.contains("Pilot")){
                    fields.add("Pilot")
                    labels["Pilot"] = "Pilot"
                }

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

                expanded_record["Pilot1"] =  pilot1.name
                if (!fields.contains("Pilot1")){
                    fields.add("Pilot1")
                    labels["Pilot1"] = "Pilot1"
                }

                expanded_record["Country1"] =  pilot1.country
                if (!fields.contains("Country1")){
                    fields.add("Country1")
                    labels["Country1"] = "Country1"
                }

                expanded_record["Glider1"] =  pilot1.glider
                if (!fields.contains("Glider1")){
                    fields.add("Glider1")
                    labels["Glider1"] = "Glider1"
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
        params.ACROPYX_RESULT = (competitionInstance.isEnded())? "Final overall ranking": "Intermediate results"
        chain(controller:'jasper',action:'index',model:[data:resultList],params:params)

    }

    def exportCompetition = {
        def competitionInstance = Competition.get(params.id)

        def runs = competitionInstance.findStartedRuns()

        def sw = new StringWriter()
        def csv = new CSVWriter(sw, {
           Rank { it.rank }
           CivlId { it.civlId }
           Competitor { it.competitor }
           Country {it.country}
           Glider {it.glider}
           Warnings {it.warnings}
           runs.each { run ->
                "${run.name}" {it."${run.name}"}
            }
            Total {it.overall }
        })


        def competitionResults = competitionInstance.computeResults()





        competitionResults.eachWithIndex{ result, i ->
            //add rank
            def values = [:]
            values.put("rank",  i+1)
            //Add Competitor
             values.put("competitor", result.competitor)
            //Add warnings
            values.put("warnings", (int)result.warnings)

            if (result.competitor instanceof Pilot){
                def flight_pilot = result.competitor as Pilot
                def pilot = Pilot.get(flight_pilot.id)
                values.put("country", pilot.country)
                values.put("glider", pilot.glider)
                values.put("civlId", pilot.civlId)
            }
            else{
              //Add pilots
                def flight_team = result.competitor as Team
                def team = Team.get(flight_team.id)
                def pilots = team.pilots.asList()

                def pilot1 = pilots.get(0)
                def pilot2 = pilots.get(1)

                values.put("competitor", team.name + " ( " + pilot1.name + " / " + pilot2.name + " ) ")
                values.put("country", pilot1.country + " / " + pilot2.country)
                values.put("glider", pilot1.glider + " / " + pilot2.glider)
                values.put("civlId", pilot1.civlId + " / " + pilot2.civlId)

            }
            //add overall
            values.put("overall", roundMark(result.overall))
            //Add Run results
           runs.eachWithIndex { run, j ->
                def runResult =  result.flights?.get(run.id)
                values.put(run.name, (runResult)?roundMark(runResult.result):"")
            }


            csv << values
        }


        response.setContentType("text/csv")
        response.setHeader("Content-disposition", "filename=${competitionInstance}.csv")
        response.outputStream << sw
    }

//    def exportCompetitionSync = {
//        def competitionInstance = Competition.get(params.id)
//
//        def runs = competitionInstance.findStartedRuns()
//
//        def sw = new StringWriter()
//        def csv = new CSVWriter(sw, {
//            Rank { it.rank }
//            Team { it.competitor }
//            Pilot1 { it.pilot1 }
//            Pilot2 { it.pilot2 }
//            Warnings {it.warnings}
//            runs.each { run ->
//                "${run.name}" {it."${run.name}"}
//            }
//            Result {it.overall }
//        })
//
//
//        def competitionResults = competitionInstance.computeResults()
//
//        competitionResults.eachWithIndex{ result, i ->
//            //add rank
//            def values = [:]
//            values.put("rank",  i+1)
//            //Add Competitor
//            values.put("competitor", result.competitor)
//            //Add warnings
//            values.put("warnings", (int)result.warnings)
//
//            //Add pilots
//            def flight_team = result.competitor as Team
//            def team = Team.get(flight_team.id)
//            values.put("pilot1", team.pilots[0].name + "(" + team.pilots[0].civlId +")")
//            values.put("pilot2", team.pilots[1].name + "(" + team.pilots[1].civlId +")")
//
//            //add overall
//            values.put("overall", roundMark(result.overall))
//            //Add Run results
//            runs.eachWithIndex { run, j ->
//                def runResult =  result.flights?.get(run.id)
//                values.put(run.name, (runResult)?roundMark(runResult.result):"")
//            }
//
//            csv << values
//        }
//
//
//        response.setContentType("text/csv")
//        response.setHeader("Content-disposition", "filename=${competitionInstance}.csv")
//        response.outputStream << sw
//    }


    def  roundMark(mark) {
        def decimalFormat = new DecimalFormat("0.000")
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP)
        return decimalFormat.format(mark)
        //return markString as double
    }
}
