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

class CompetitionController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 100, 100)
        [competitionInstanceList: Competition.list(params), competitionInstanceTotal: Competition.count()]
    }

	@Secured(['ROLE_ADMIN'])
    def create = {
        def competitionInstance = new Competition()
        competitionInstance.properties = params
        return [competitionInstance: competitionInstance]
    }

	@Secured(['ROLE_ADMIN'])
    def save = {
        def competitionInstance = new Competition(params)
        if (competitionInstance.save(flush: true)) {
            
            // Default values
            competitionInstance.addDefaultCoefficients()
            competitionInstance.addAllJudges()
            
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'competition.label', default: 'Competition'), competitionInstance.id])}"
            redirect(action: "show", id: competitionInstance.id)
        }
        else {
            render(view: "create", model: [competitionInstance: competitionInstance])
        }
    }

    def show = {
        def competitionInstance = Competition.get(params.id)
        if (!competitionInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'competition.label', default: 'Competition'), params.id])}"
            redirect(action: "list")
        }
        else {
            [competitionInstance: competitionInstance]
        }
    }

	@Secured(['ROLE_ADMIN'])
    def edit = {
        def competitionInstance = Competition.get(params.id)
        if (!competitionInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'competition.label', default: 'Competition'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [competitionInstance: competitionInstance]
        }
    }

	@Secured(['ROLE_ADMIN'])
    def update = {
        def competitionInstance = Competition.get(params.id)
        if (competitionInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (competitionInstance.version > version) {
                    
                    competitionInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'competition.label', default: 'Competition')] as Object[], "Another user has updated this Competition while you were editing")
                    render(view: "edit", model: [competitionInstance: competitionInstance])
                    return
                }
            }
            competitionInstance.properties = params
            if (!competitionInstance.hasErrors() && competitionInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'competition.label', default: 'Competition'), competitionInstance.id])}"
                redirect(action: "show", id: competitionInstance.id)
            }
            else {
                render(view: "edit", model: [competitionInstance: competitionInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'competition.label', default: 'Competition'), params.id])}"
            redirect(action: "list")
        }
    }

	@Secured(['ROLE_ADMIN'])
    def delete = {
        def competitionInstance = Competition.get(params.id)        
        if (competitionInstance) {
            Competition.withTransaction { status ->
                try {
                    competitionInstance.delete(flush: true)
                    flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'competition.label', default: 'Competition'), params.id])}"
                    redirect(action: "list")
                }
                catch (org.springframework.dao.DataIntegrityViolationException e) {
                    flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'competition.label', default: 'Competition'), params.id])}"
                    status.setRollbackOnly()
                    redirect(action: "show", id: params.id)
                }
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'competition.label', default: 'Competition'), params.id])}"
            redirect(action: "list")
        }
    }
}
