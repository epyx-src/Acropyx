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

import grails.plugin.multitenant.core.util.TenantUtils
import grails.plugins.springsecurity.Secured

import org.codehaus.groovy.grails.commons.ConfigurationHolder

class EventController {
    def displayerService

    def fetchEventModel() {
        
        def activeCompetitions =  Competition.findAllByStartTimeIsNotNullAndEndTimeIsNull()
        def activeRuns = Run.findAllByStartTimeIsNotNullAndEndTimeIsNull()
        def activeFlights = Flight.findAllByStartTimeIsNotNullAndEndTimeIsNull()
        
        def activeCompetition = null

        if (activeCompetitions?.size() > 0)
        {
            activeCompetition = activeCompetitions[0]
        }
        
        def activeRun = null
        if (activeRuns?.size() > 0){
          activeRun = activeRuns[0]
       //   activeCompetition = activeRun.competition
        }
        
        def activeFlight = null
        if (activeFlights?.size() > 0){
          activeFlight = activeFlights[0];
        } 
        
        [competitionInstanceList: activeCompetitions,
                    runInstanceList: activeRuns,
                    flightInstanceList: activeFlights,
                    activeCompetition: activeCompetition,
                    activeRun: activeRun,
                    activeFlight: activeFlight]
    }

    def index = {
        redirect(action: "home", params: params)
    }

    def home = { return fetchEventModel() }

    def String getTenantName() {
        def tenantId = TenantUtils.getCurrentTenant()
        def tenant = ConfigurationHolder.config.tenant.domainTenantMap.find { it.value == tenantId}
        return tenant.key.split("\\.")[0]
    }

    @Secured(['ROLE_EVENT', 'ROLE_ADMIN'])
    def startCompetition = {
        def competitionInstance = Competition.get(params.id)
        if (competitionInstance == null) {
            flash.competitionMessage = "Please choose a competition"
        }
        else {
            try {
                competitionInstance.start();
                if (competitionInstance.save()) {
                    displayerService.competitionHasStarted(getTenantName(), competitionInstance)
                } else {
                    def model = fetchEventModel();
                    model.put('competitionInstance', competitionInstance)
                    return render(view: "home", model: model)
                }
            } catch (Exception e) {
                flash.competitionMessage = e.getLocalizedMessage()
            }
        }
        redirect(action: "home")
    }

    @Secured(['ROLE_EVENT', 'ROLE_ADMIN'])
    def endCompetition = {
        def competitionInstance = Competition.get(params.id)
        if (competitionInstance == null) {
            flash.competitionMessage = "Please choose a competition"
        }
        else {
            try {
                competitionInstance.end();
                displayerService.competitionHasEnded(getTenantName(), competitionInstance)
                redirect(controller: "resultCompetition", action: "show",  id: competitionInstance.id)
                return
            } catch (Exception e) {
                flash.competitionMessage = e.getLocalizedMessage()
            }
        }
        redirect(action: "home")
    }

    @Secured(['ROLE_EVENT', 'ROLE_ADMIN'])
    def startRun = {
        def runInstance = Run.get(params.id)
        if (runInstance == null) {
            flash.runMessage = "Please choose a run"
        }
        else {
            try {
                runInstance.start();
                if (runInstance.save()) {
                    displayerService.runHasStarted(getTenantName(), runInstance)
                } else {
                    def model = fetchEventModel();
                    model.put('runInstance', runInstance)
                    return render(view: "home", model: model)
                }
            } catch (Exception e) {
                flash.runMessage = e.getLocalizedMessage()
            }
        }
        redirect(action: "home")
    }

    @Secured(['ROLE_EVENT', 'ROLE_ADMIN'])
    def endRun = {
        def runInstance = Run.get(params.id)
        if (runInstance == null) {
            flash.runMessage = "Please choose a run"
        }
        else {
            try {
                runInstance.end()
                displayerService.runHasEnded(getTenantName(), runInstance)
                redirect(controller: "resultRun", action: "show", id:runInstance.id)
                return
            } catch (Exception e) {
                flash.runMessage = e.getLocalizedMessage()
            }
        }
        redirect(action: "home")
    }

    @Secured(['ROLE_EVENT', 'ROLE_ADMIN'])
    def startFlight = {
        def competitorInstance = Competitor.get(params.selectCompetitor)
        if (competitorInstance == null) {
            flash.flightMessage = "Please choose a competitor"
        }
        else {
            try {
                def activeRun = Run.findByStartTimeIsNotNullAndEndTimeIsNull()
                def flight = new Flight(competitor:competitorInstance, run:activeRun)
                flight.start()
                if (flight.save()) {
                 //   displayerService.flightHasStarted(getTenantName(), flight)
                } else {
                    def model = fetchEventModel();
                    model.put('newFlight', flight)
                    return render(view: "home", model: model)
                }
            } catch (Exception e) {
                flash.flightMessage = e.getLocalizedMessage()
            }
        }
        redirect(action: "home")
    }
    
    @Secured(['ROLE_EVENT', 'ROLE_ADMIN'])
    def displayFlight = {
        
        def flightInstance = Flight.get(params.id)
        if (flightInstance == null) {
            flash.flightMessage = "Please choose a flight"
        }
        else {
            try {
      
                    displayerService.flightHasStarted(getTenantName(), flightInstance)
                }
             catch (Exception e) {
                    flash.flightMessage = e.getLocalizedMessage()
             }
        }
        redirect(action: "home")
    }

    @Secured(['ROLE_EVENT', 'ROLE_ADMIN'])
    def endFlight = {
        def flightInstance = Flight.get(params.id)
        if (flightInstance == null) {
            flash.flightMessage = "Please choose a flight"
        }
        else {
            try {
                if ((flightInstance.marks?.size() > 0) && (flightInstance.manoeuvres?.size() > 0)) {
                    flightInstance.end()
                    displayerService.flightHasEnded(getTenantName(), flightInstance)
                    displayerService.
                    return redirect(controller: "resultRun", id:flightInstance.run.id)
                }
                else {
                    flash.flightMessage = "Please enter maneuvers and vote before ending the flight"
                }
            } catch (Exception e) {
                flash.flightMessage = e.getLocalizedMessage()
            }
        }
        redirect(action: "home")
    }

    @Secured(['ROLE_EVENT', 'ROLE_ADMIN'])
    def deleteFlight = {
        def flightInstance = Flight.get(params.id)
        if (flightInstance) {
            try {
                flightInstance.delete(flush: true)
                flash.flightMessage = "${message(code: 'default.deleted.message', args: [message(code: 'flight.label', default: 'Flight'), params.id])}"
                return render(view: "home", model: fetchEventModel())
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.flightMessage = "${message(code: 'default.not.deleted.message', args: [message(code: 'flight.label', default: 'Flight'), params.id])}"
            }
        }
        else {
            flash.flightMessage = "${message(code: 'default.not.found.message', args: [message(code: 'flight.label', default: 'Flight'), params.id])}"
        }
        redirect(action: "home")
    }

    @Secured(['ROLE_EVENT', 'ROLE_ADMIN'])
    def addWarning = {
        def flightInstance = Flight.get(params.id)
        if (flightInstance == null) {
            flash.flightMessage = "Please choose a flight"
        }
        else {
            try {
                flightInstance.addWarning()
                flash.flightMessage = String.format("%d warning(s)", flightInstance.warnings)
            } catch (Exception e) {
                flash.flightMessage = e.getLocalizedMessage()
            }
        }
        redirect(action: "home")
    }

    @Secured(['ROLE_EVENT', 'ROLE_ADMIN'])
    def removeWarning = {
        def flightInstance = Flight.get(params.id)
        if (flightInstance == null) {
            flash.flightMessage = "Please choose a flight"
        }
        else {
            try {
                flightInstance.removeWarning()
                flash.flightMessage = String.format("%d warning(s)", flightInstance.warnings)
            } catch (Exception e) {
                flash.flightMessage = e.getLocalizedMessage()
            }
        }
        redirect(action: "home")
    }
    
    @Secured(['ROLE_EVENT', 'ROLE_ADMIN'])
    def sendMessage = {
        displayerService.clearPaf(getTenantName())

        if (params.sendMessageText) {
            def decay = 7
            if (params.sticky) {
                decay = -1
            }
            displayerService.sendPaf(getTenantName(), params.sendMessageText, decay)
        }
        redirect(action: "home")
    }

    @Secured(['ROLE_EVENT', 'ROLE_ADMIN'])
    def clearMessage = {
        displayerService.clearPaf(getTenantName())
        redirect(action: "home")
    }

    @Secured(['ROLE_EVENT', 'ROLE_ADMIN'])
    def sendCompetitionResultToDisplay = {
        def competition = Competition.get(params.id)
        if ( competition ) {
            if (competition.findStartedRuns()?.size() > 0) {
                displayerService.resultCompetition(getTenantName(), competition)
            }
            else {
                flash.displayResults = "Selected competition ('" + competition.name + "') has no started run"
            }
        }
        else {
            flash.displayResults = "Please select a competition"
        }
        redirect(action: "home")
    }

    @Secured(['ROLE_EVENT', 'ROLE_ADMIN'])
    def sendRunResultToDisplay = {
        def run = Run.get(params.id)
        if (run) {
            if (run.findEndedFlights()?.size() > 0) {
                displayerService.resultRun(getTenantName(), run)
            }
            else {
                flash.displayResults = "Selected run ('" + run.name + "') has no ended flight"
            }
        }
        else {
            flash.displayResults = "Please select a run"
        }
        redirect(action: "home")
    }

    @Secured(['ROLE_EVENT', 'ROLE_ADMIN'])
    def sendFlightToDisplay = {
        def flight = Flight.get(params.id)
        if (flight) {
            if (flight.isActive()) {
                displayerService.flightHasStarted(getTenantName(), flight)
            } else {
                displayerService.flightHasEnded(getTenantName(), flight)
            }
        }
        else {
            flash.displayResults = "Please select a flight"
        }
        redirect(action: "home")
    }
}
