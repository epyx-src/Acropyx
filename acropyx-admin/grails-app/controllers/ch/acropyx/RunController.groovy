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

import grails.plugins.springsecurity.Secured
import org.grails.plugins.csv.CSVWriter


class RunController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
    
    def exportService

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = 100 //Math.min(params.max ? params.int('max') : 10, 100)
        [runInstanceList: Run.list(params), runInstanceTotal: Run.count()]
    }

    @Secured(['ROLE_ADMIN'])
    def create = {
        def runInstance = new Run()
        runInstance.properties = params
        return [runInstance: runInstance]
    }

    @Secured(['ROLE_ADMIN'])
    def save = {
        def runInstance = new Run(params)
        if (runInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'run.label', default: 'Run'), runInstance.id])}"
            redirect(action: "show", id: runInstance.id)
        }
        else {
            render(view: "create", model: [runInstance: runInstance])
        }
    }

    def show = {
        def runInstance = Run.get(params.id)
        if (!runInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'run.label', default: 'Run'), params.id])}"
            redirect(action: "list")
        }
        else {
            [runInstance: runInstance]
        }
    }

    @Secured(['ROLE_ADMIN'])
    def edit = {
        def runInstance = Run.get(params.id)
        if (!runInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'run.label', default: 'Run'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [runInstance: runInstance]
        }
    }

    @Secured(['ROLE_ADMIN'])
    def update = {
        def runInstance = Run.get(params.id)
        if (runInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (runInstance.version > version) {

                    runInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [
                        message(code: 'run.label', default: 'Run')]
                    as Object[], "Another user has updated this Run while you were editing")
                    render(view: "edit", model: [runInstance: runInstance])
                    return
                }
            }
            runInstance.properties = params
            if (!runInstance.hasErrors() && runInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'run.label', default: 'Run'), runInstance.id])}"
                redirect(action: "show", id: runInstance.id)
            }
            else {
                render(view: "edit", model: [runInstance: runInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'run.label', default: 'Run'), params.id])}"
            redirect(action: "list")
        }
    }

    @Secured(['ROLE_ADMIN'])
    def delete = {
        def runInstance = Run.get(params.id)
        if (runInstance) {
            try {
                runInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'run.label', default: 'Run'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'run.label', default: 'Run'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'run.label', default: 'Run'), params.id])}"
            redirect(action: "list")
        }
    }

    def export = {
        def runInstance = Run.get(params.id)

        def sw = new StringWriter()
        def csv = new CSVWriter(sw, {
            Rank { it.rank }
            Competitor { it.competitor }
            runInstance.competition.markCoefficients.each { markCoefficient ->
                "${markCoefficient.markDefinition}" { it."${markCoefficient.markDefinition}"}
            }
            Overall { it.overall }
        })
        def flights = runInstance.findEndedFlights(true)

        flights.eachWithIndex { flight, i ->
            def values = [:]
            values.put('rank', i+1)
            values.put('competitor', flight.competitor.name)

            def detailedResults = flight.computeDetailedResults()
            runInstance.competition.markCoefficients.each { markCoefficient ->
                values.put(markCoefficient.markDefinition.name, detailedResults.get(markCoefficient.id))
            }

            csv << values
        }

        response.setContentType("text/csv")
        response.setHeader("Content-disposition", "filename=${runInstance}.csv")
        response.outputStream << sw
    }
    
     def export_pdf = {
        def runInstance = Run.get(params.id)
        def flights = runInstance.findEndedFlights(true)
        def labels = [:]
        def fields = []

        response.setContentType("application/pdf") //config.grails.mime.types[pdf]
        response.setHeader("Content-disposition", "filename= test.pdf")

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
                expanded_record["${markName}"] = detailedResults.get(markCoefficient.id)
                if (!fields.contains("${markName}")){
                    fields.add("${markName}")
                    labels["${markName}"] = "${markName}"
                }

            }
            resultList.add(expanded_record)
        }

        exportService.export("pdf", response.outputStream, resultList, fields, labels, [:], [:])

    }

     def reportRun = {

         def run = Run.list()
         chain(controller:'jasper',action:'index',model:[data:run],params:params)
    }

}
