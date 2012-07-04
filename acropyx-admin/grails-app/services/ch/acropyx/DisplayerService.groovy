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

import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient

import java.math.RoundingMode
import java.text.DecimalFormat

import javax.annotation.PostConstruct

import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH

class DisplayerService {
    // Base url constant to access the 'Displayer' is 'ch.acropyx.displayerUrl'
    def messageSource

    def RESTClient restClient

    @PostConstruct
    def void init() {
        restClient = new RESTClient( CH.config.ch.acropyx.displayerUrl )
        restClient.handler.success = { "Success!" }
        restClient.handler.failure = { resp -> "Unexpected failure: ${resp.statusLine}" }
    }

    def void competitionHasStarted(String tenant, Competition competition) {
        Object[] args = [competition.name]
        def name = messageSource.getMessage( 'displayer.competition_start.title', args, Locale.default )
        def text = messageSource.getMessage( 'displayer.competition_start.text', args, Locale.default )
        def resp = restClient.post( path : 'waiting',
                body : '{ "name" : "' + name + '", "text" : "' + text + '" }',
                requestContentType : ContentType.JSON,
                headers : ["Tenant": tenant] )
            
      //  sleep(30000)
      //  competitionStartingOrder(tenant, competition)
    }
    
    def void runStartingOrder(String tenant, Run run){
        if (run) {
            def msgKey = (run.endTime ? 'displayer.result.final.text' : 'displayer.result.intermediate.text')
            def subTitle = messageSource.getMessage( 'displayer.run_start.order', []as Object[], Locale.default )
            Object[] args = [
                run.competition.name,
                run.name,
                subTitle
            ]
            def name = messageSource.getMessage( 'displayer.run_end.title', args, Locale.default )
            def json = '{ "name" : "' + name + '", "competitors" : ' + generateRunStartingOrder(run) + '}'
            def resp = restClient.post( path : 'startOrderRun',
                    body : json,
                    requestContentType : ContentType.JSON,
                    headers : ["Tenant": tenant] )
        }
    }

    

    def void competitionHasEnded(String tenant, Competition competition) {
        resultCompetition(tenant, competition)
    }

    def void runHasStarted(String tenant, Run run) {
        Object[] args = [
            run.competition.name,
            run.name
        ]
        def name = messageSource.getMessage( 'displayer.run_start.title', args, Locale.default )
        def text = messageSource.getMessage( 'displayer.run_start.text', args, Locale.default )
        def resp = restClient.post( path : 'waiting',
                body : '{ "name" : "' + name + '", "text" : "' + text + '" }',
                requestContentType : ContentType.JSON,
                headers : ["Tenant": tenant] )
        
        sleep(30000)
        runStartingOrder(tenant, run)
    }

    def void runHasEnded(String tenant, Run run) {
        resultRun(tenant, run)
    }

    def void flightHasStarted(String tenant, Flight flight) {
        Object[] args = [
            flight.run.competition.name,
            flight.run.name
        ]
        def name = messageSource.getMessage( 'displayer.flight_start.title', args, Locale.default )
        def competitor = flight.competitor
        def isPilot = (competitor instanceof Pilot)
        def json = '{ "name" : "' + name + '", "' + (isPilot ? 'competitor' : 'team' ) + '" : ' + competitor.toJSON() + '}'
        def resp = restClient.post( path : (isPilot ? 'currentFlightSolo' : 'currentFlightTeam'),
                body : json,
                requestContentType : ContentType.JSON,
                headers : ["Tenant": tenant] )
    }

    def void flightHasEnded(String tenant, Flight flight) {
        Object[] args = [
            flight.run.competition.name,
            flight.run.name
        ]
        def name = messageSource.getMessage( 'displayer.flight_end.title', args, Locale.default )
        def competitor = flight.competitor
        def isPilot = (competitor instanceof Pilot)
        def json = '{ "name" : "' + name + '", "' + (isPilot ? 'competitor' : 'team' ) + '" : ' + competitor.toJSON()
        def detailedResults = flight.computeDetailedResults()
        json += ', "marks" : ' + generateDetailedResults(detailedResults)
        json += ', "overall" : { "kind" : "Result", "value" : ' + roundResult(flight.computeResult(detailedResults)) + '}'
        json += ', "warnings" : ' + flight.warnings
        json += ', "rank" : "' + generateRank(flight) + '."}'
        def resp = restClient.post( path : (isPilot ? 'resultFlightSolo' : 'resultFlightTeam'),
                body : json,
                requestContentType : ContentType.JSON,
                headers : ["Tenant": tenant] )
        //TODO: Define sleep time in config file
        sleep(7000)
        resultRun(tenant, flight.run)
        sleep(7000)
        resultCompetition(tenant, flight.run.competition)
    }

    def void resultRun(String tenant, Run run) {
        if (run) {
            def msgKey = (run.endTime ? 'displayer.result.final.text' : 'displayer.result.intermediate.text')
            def subTitle = messageSource.getMessage( msgKey, []as Object[], Locale.default )
            Object[] args = [
                run.competition.name,
                run.name,
                subTitle
            ]
            def name = messageSource.getMessage( 'displayer.run_end.title', args, Locale.default )
            def json = '{ "name" : "' + name + '", "competitors" : ' + generateRunResult(run) + '}'
            def resp = restClient.post( path : 'resultRun',
                    body : json,
                    requestContentType : ContentType.JSON,
                    headers : ["Tenant": tenant] )
        }
    }

    def void resultCompetition(String tenant, Competition competition) {
        if (competition) {
            def msgKey = (competition.endTime ? 'displayer.result.final.text' : 'displayer.result.intermediate.text')
            def subTitle = messageSource.getMessage( msgKey, []as Object[], Locale.default )
            Object[] args = [competition.name, subTitle]
            def name = messageSource.getMessage( 'displayer.competition_end.title', args, Locale.default )
            def json = '{ "name" : "' + name + '", "competitors" : ' + generateCompetitionResult(competition) + '}'
            def resp = restClient.post( path : 'resultRun',
                    body : json,
                    requestContentType : ContentType.JSON,
                    headers : ["Tenant": tenant] )
        }
    }

    def void sendPaf(String tenant, String text, long decay) {
        text = text.replace("\'", "\\\\u0027")
        text = text.replace("\"", "\\\\u0022")
        def resp = restClient.post( path : 'paf',
                body : '{ "text" : "' + text + '", "style" : "decay:' + decay + '" }',
                requestContentType : ContentType.JSON,
                headers : ["Tenant": tenant] )
    }

    def void clearPaf(String tenant) {
        def resp = restClient.post( path : 'pafclean',
                headers : ["Tenant": tenant] )
    }
    
    
    def String generateRunStartingOrder(Run run){
        def String json = '['
        def competitors = Competitor.competitorsForActiveRun()
        
        competitors.eachWithIndex() { competitor, i ->
            json += '{ "name" :  "' + competitor.name +'"'
            if ( competitor instanceof Pilot ) {
                json += ', "country" : "' + competitor.toCountryISO3166_1() + '"'
            }
            json += '}'
            if (i < competitors.size() -1) {
                json += ','
            }
        }
        json += ']'          
    }

    def String generateRunResult(Run run) {
        def String json = '['
        def endedFlights = run.findEndedFlights(true)

        endedFlights.eachWithIndex { flight, i ->
            json += '{ "name" : "' + flight.competitor.name + '", "mark" : ' + roundResult(flight.computeResult(flight.computeDetailedResults()))
            if ( flight.competitor instanceof Pilot ) {
                json += ', "country" : "' + flight.competitor.toCountryISO3166_1() + '"'
            }
            json += '}'
            if (i < endedFlights.size() -1) {
                json += ','
            }
        }
        json += ']'
    }

    def String generateCompetitionResult(Competition competition) {
        def String json = '['
        def results = competition.computeResults()

        results.eachWithIndex { result, i ->
            json += '{ "name" : "' + result.competitor.name + '", "nbRuns" : ' + result.flights.size() + ', "mark" : ' + roundResult(result.overall)
            if (result.competitor instanceof Pilot) {
                json += ', "country" : "' + result.competitor.toCountryISO3166_1() + '"'
            }
            json += '}'
            if (i < results.size() -1) {
                json += ','
            }
        }
        json += ']'
    }

    def String generateDetailedResults(Map detailedResults) {
        def String json = '['
        if (detailedResults) {
            def nbOfResults = detailedResults.size()
            def ii = 0
            detailedResults.each { markCoefficientId, mark ->
                ii++
                def markCoefficient = MarkCoefficient.get(markCoefficientId)
                json += '{ "kind" : "' + markCoefficient.markDefinition.name + '"'
                json += ', "value" : ' + roundResult(mark) + ' }'
                if (ii < nbOfResults) {
                    json += ','
                }
            }
        }
        json += ']'
    }

    def String generateRank(Flight flight) {
        def flights = flight.run.findEndedFlights(true)
        int position = flights.indexOf(flight);
        if (position == -1) {
            return 1
        } else {
            return position + 1
        }
    }

    def double roundResult(result) {
        def decimalFormat = new DecimalFormat("#.#")
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP)
        def markString = decimalFormat.format(result)
        return markString as double
    }
}
