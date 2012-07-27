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

class MarkController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = 100 //Math.min(params.max ? params.int('max') : 20, 100)
        [markInstanceList: Mark.list(params), markInstanceTotal: Mark.count()]
    }

    @Secured(['ROLE_ADMIN'])
    def create = {
        def markInstance = new Mark()
        markInstance.properties = params
        return [markInstance: markInstance]
    }

    @Secured(['ROLE_ADMIN'])
    def save = {
        def markInstance = new Mark(params)
        if (markInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'mark.label', default: 'Mark'), markInstance.id])}"
            redirect(action: "show", id: markInstance.id)
        }
        else {
            render(view: "create", model: [markInstance: markInstance])
        }
    }

    def show = {
        def markInstance = Mark.get(params.id)
        if (!markInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'mark.label', default: 'Mark'), params.id])}"
            redirect(action: "list")
        }
        else {
            [markInstance: markInstance]
        }
    }

    @Secured(['ROLE_ADMIN'])
    def edit = {
        def markInstance = Mark.get(params.id)
        if (!markInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'mark.label', default: 'Mark'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [markInstance: markInstance]
        }
    }

    @Secured(['ROLE_ADMIN'])
    def update = {
        def markInstance = Mark.get(params.id)
        if (markInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (markInstance.version > version) {

                    markInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [
                        message(code: 'mark.label', default: 'Mark')]
                    as Object[], "Another user has updated this Mark while you were editing")
                    render(view: "edit", model: [markInstance: markInstance])
                    return
                }
            }
            markInstance.properties = params
            if (!markInstance.hasErrors() && markInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'mark.label', default: 'Mark'), markInstance.id])}"
                redirect(action: "show", id: markInstance.id)
            }
            else {
                render(view: "edit", model: [markInstance: markInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'mark.label', default: 'Mark'), params.id])}"
            redirect(action: "list")
        }
    }

    @Secured(['ROLE_ADMIN'])
    def delete = {
        def markInstance = Mark.get(params.id)
        if (markInstance) {
            try {
                markInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'mark.label', default: 'Mark'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'mark.label', default: 'Mark'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'mark.label', default: 'Mark'), params.id])}"
            redirect(action: "list")
        }
    }
}
