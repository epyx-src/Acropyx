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

import org.springframework.web.multipart.MultipartHttpServletRequest
import org.springframework.web.multipart.commons.CommonsMultipartFile

class MarkDefinitionController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST", upload: ["GET", "POST"]]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 100, 200)
        [markDefinitionInstanceList: MarkDefinition.list(params), markDefinitionInstanceTotal: MarkDefinition.count()]
    }

    @Secured(['ROLE_ADMIN'])
    def create = {
        def markDefinitionInstance = new MarkDefinition()
        markDefinitionInstance.properties = params
        return [markDefinitionInstance: markDefinitionInstance]
    }

    @Secured(['ROLE_ADMIN'])
    def save = {
        def markDefinitionInstance = new MarkDefinition(params)
        if (markDefinitionInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'markDefinition.label', default: 'MarkDefinition'), markDefinitionInstance.id])}"
            redirect(action: "show", id: markDefinitionInstance.id)
        }
        else {
            render(view: "create", model: [markDefinitionInstance: markDefinitionInstance])
        }
    }

    def show = {
        def markDefinitionInstance = MarkDefinition.get(params.id)
        if (!markDefinitionInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'markDefinition.label', default: 'MarkDefinition'), params.id])}"
            redirect(action: "list")
        }
        else {
            [markDefinitionInstance: markDefinitionInstance]
        }
    }

    @Secured(['ROLE_ADMIN'])
    def edit = {
        def markDefinitionInstance = MarkDefinition.get(params.id)
        if (!markDefinitionInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'markDefinition.label', default: 'MarkDefinition'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [markDefinitionInstance: markDefinitionInstance]
        }
    }

    @Secured(['ROLE_ADMIN'])
    def update = {
        def markDefinitionInstance = MarkDefinition.get(params.id)
        if (markDefinitionInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (markDefinitionInstance.version > version) {

                    markDefinitionInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [
                        message(code: 'markDefinition.label', default: 'MarkDefinition')]
                    as Object[], "Another user has updated this MarkDefinition while you were editing")
                    render(view: "edit", model: [markDefinitionInstance: markDefinitionInstance])
                    return
                }
            }
            markDefinitionInstance.properties = params
            if (!markDefinitionInstance.hasErrors() && markDefinitionInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'markDefinition.label', default: 'MarkDefinition'), markDefinitionInstance.id])}"
                redirect(action: "show", id: markDefinitionInstance.id)
            }
            else {
                render(view: "edit", model: [markDefinitionInstance: markDefinitionInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'markDefinition.label', default: 'MarkDefinition'), params.id])}"
            redirect(action: "list")
        }
    }

    @Secured(['ROLE_ADMIN'])
    def delete = {
        def markDefinitionInstance = MarkDefinition.get(params.id)
        if (markDefinitionInstance) {
            try {
                markDefinitionInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'markDefinition.label', default: 'MarkDefinition'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'markDefinition.label', default: 'MarkDefinition'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'markDefinition.label', default: 'MarkDefinition'), params.id])}"
            redirect(action: "list")
        }
    }

    @Secured(['ROLE_ADMIN'])
    def upload = {
        if (request instanceof MultipartHttpServletRequest) {
            MultipartHttpServletRequest mpr = (MultipartHttpServletRequest)request;
            CommonsMultipartFile csvFile = (CommonsMultipartFile) mpr.getFile("userFile");
            if (!csvFile.empty) {
                MarkDefinition.importFromStream(csvFile.inputStream)
                redirect(action: "list")
            } else {
                flash.message = "file cannot be empty"
            }
        }
    }
}
