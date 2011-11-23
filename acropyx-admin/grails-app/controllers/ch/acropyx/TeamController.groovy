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

class TeamController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [teamInstanceList: Team.list(params), teamInstanceTotal: Team.count()]
    }

    @Secured(['ROLE_ADMIN'])
    def create = {
        def teamInstance = new Team()
        teamInstance.properties = params
        return [teamInstance: teamInstance]
    }

    @Secured(['ROLE_ADMIN'])
    def save = {
        def teamInstance = new Team(params)
        if (teamInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'team.label', default: 'Team'), teamInstance.id])}"
            redirect(action: "show", id: teamInstance.id)
        }
        else {
            render(view: "create", model: [teamInstance: teamInstance])
        }
    }

    def show = {
        def teamInstance = Team.get(params.id)
        if (!teamInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'team.label', default: 'Team'), params.id])}"
            redirect(action: "list")
        }
        else {
            [teamInstance: teamInstance]
        }
    }

    @Secured(['ROLE_ADMIN'])
    def edit = {
        def teamInstance = Team.get(params.id)
        if (!teamInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'team.label', default: 'Team'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [teamInstance: teamInstance]
        }
    }

    @Secured(['ROLE_ADMIN'])
    def update = {
        def teamInstance = Team.get(params.id)
        if (teamInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (teamInstance.version > version) {

                    teamInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [
                        message(code: 'team.label', default: 'Team')]
                    as Object[], "Another user has updated this Team while you were editing")
                    render(view: "edit", model: [teamInstance: teamInstance])
                    return
                }
            }
            teamInstance.properties = params
            if (!teamInstance.hasErrors() && teamInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'team.label', default: 'Team'), teamInstance.id])}"
                redirect(action: "show", id: teamInstance.id)
            }
            else {
                render(view: "edit", model: [teamInstance: teamInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'team.label', default: 'Team'), params.id])}"
            redirect(action: "list")
        }
    }

    @Secured(['ROLE_ADMIN'])
    def delete = {
        def teamInstance = Team.get(params.id)
        if (teamInstance) {
            try {
                teamInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'team.label', default: 'Team'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'team.label', default: 'Team'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'team.label', default: 'Team'), params.id])}"
            redirect(action: "list")
        }
    }
}
