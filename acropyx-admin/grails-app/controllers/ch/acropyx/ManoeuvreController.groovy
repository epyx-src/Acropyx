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

class ManoeuvreController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST", upload: ["GET", "POST"]]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [manoeuvreInstanceList: Manoeuvre.list(params), manoeuvreInstanceTotal: Manoeuvre.count()]
    }

    @Secured(['ROLE_ADMIN'])
    def create = {
        def manoeuvreInstance = new Manoeuvre()
        manoeuvreInstance.properties = params
        return [manoeuvreInstance: manoeuvreInstance]
    }

    @Secured(['ROLE_ADMIN'])
    def save = {
        def manoeuvreInstance = new Manoeuvre(params)
        if (manoeuvreInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'manoeuvre.label', default: 'Manoeuvre'), manoeuvreInstance.id])}"
            redirect(action: "show", id: manoeuvreInstance.id)
        }
        else {
            render(view: "create", model: [manoeuvreInstance: manoeuvreInstance])
        }
    }

    def show = {
        def manoeuvreInstance = Manoeuvre.get(params.id)
        if (!manoeuvreInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'manoeuvre.label', default: 'Manoeuvre'), params.id])}"
            redirect(action: "list")
        }
        else {
            [manoeuvreInstance: manoeuvreInstance]
        }
    }

    @Secured(['ROLE_ADMIN'])
    def edit = {
        def manoeuvreInstance = Manoeuvre.get(params.id)
        if (!manoeuvreInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'manoeuvre.label', default: 'Manoeuvre'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [manoeuvreInstance: manoeuvreInstance]
        }
    }

    @Secured(['ROLE_ADMIN'])
    def update = {
        def manoeuvreInstance = Manoeuvre.get(params.id)
        if (manoeuvreInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (manoeuvreInstance.version > version) {

                    manoeuvreInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [
                        message(code: 'manoeuvre.label', default: 'Manoeuvre')]
                    as Object[], "Another user has updated this Manoeuvre while you were editing")
                    render(view: "edit", model: [manoeuvreInstance: manoeuvreInstance])
                    return
                }
            }
            manoeuvreInstance.properties = params
            if (!manoeuvreInstance.hasErrors() && manoeuvreInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'manoeuvre.label', default: 'Manoeuvre'), manoeuvreInstance.id])}"
                redirect(action: "show", id: manoeuvreInstance.id)
            }
            else {
                render(view: "edit", model: [manoeuvreInstance: manoeuvreInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'manoeuvre.label', default: 'Manoeuvre'), params.id])}"
            redirect(action: "list")
        }
    }

    @Secured(['ROLE_ADMIN'])
    def delete = {
        def manoeuvreInstance = Manoeuvre.get(params.id)
        if (manoeuvreInstance) {
            try {
                manoeuvreInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'manoeuvre.label', default: 'Manoeuvre'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'manoeuvre.label', default: 'Manoeuvre'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'manoeuvre.label', default: 'Manoeuvre'), params.id])}"
            redirect(action: "list")
        }
    }

    @Secured(['ROLE_ADMIN'])
    def upload = {
        if (request instanceof MultipartHttpServletRequest) {
            MultipartHttpServletRequest mpr = (MultipartHttpServletRequest)request;
            CommonsMultipartFile csvFile = (CommonsMultipartFile) mpr.getFile("userFile");
            if (!csvFile.empty) {
                Manoeuvre.importFromStream(csvFile.inputStream)
                redirect(action: "list")
            } else {
                flash.message = "file cannot be empty"
            }
        }
    }
}
