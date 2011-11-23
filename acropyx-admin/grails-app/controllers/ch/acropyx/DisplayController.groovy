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

import grails.converters.JSON

class DisplayController {
    def displayService

    def index = {
        redirect(action: "home", params: params)
    }

    def home = { DisplayCommand command ->
        printf( "home(%s) - Cmd:%s\n", params as String, command )
    }

    def cancel = {
        printf( "cancel(%s)\n", params as String )
        flash.message = "Canceled"
        redirect( action: "home" )
    }

    def selectCurrentCompetition = { DisplayCommand command ->
        printf( "selectCurrentCompetition(%s) - Cmd:%s\n", params as String, command )
        displayService.competitionName = command?.competitionName
        displayService.competitorType = command?.competitorType
        render(template:'selectCompetitor', model:[competitionName: command?.competitionName] )
    }

    def selectCompetitor = { DisplayCommand command ->
        printf( "selectCompetitor(%s) - Cmd:%s\n", params as String, command )
        displayService.competitor = command?.competitor
        displayService.addFlight( command?.competitor )
        render(template:'sendMessage', model:[flightList: displayService.flights, competitor: command?.competitor] )
    }

    def sendTextMessage = {
        printf( "sendTextMessage - Cmd:%s\n", params as String )
        render( template: "dummy" )
    }

    def sendMarkMessage = {
        printf( "sendMarkMessage - Cmd:%s\n", params as String )
        displayService.setMarkForCurrentFlight( params.mark as int )
        redirect( action:"selectCompetitor", params:[flightList: displayService.flights, competitor: displayService.competitor] )
    }

    def ajaxGetCompetitorList = {
        printf( "ajaxGetCompetitorList(%s)\n", params as String )
        def list = (params.type == "Team" ? Team.list() : Pilot.list())
        render list as JSON
    }

    class DisplayCommand {
        String competitionName
        String competitorType
        Competitor competitor
        String textMessage

        static constraints = {
            competitionName(blank:false)
            competitorType(inList:['Pilot', 'Team'])
        }

        def String toString() {
            "[Cmd '--${competitionName}--${competitorType}--${competitor}--']"
        }
    }
}
