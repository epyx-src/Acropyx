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

import java.util.Date;

import grails.plugins.springsecurity.Secured

import org.springframework.web.multipart.MultipartHttpServletRequest
import org.springframework.web.multipart.commons.CommonsMultipartFile

class PilotController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 100, 200)
        [pilotInstanceList: Pilot.listOrderByName(params), pilotInstanceTotal: Pilot.count()]
    }

    @Secured(['ROLE_ADMIN'])
    def create = {
        def pilotInstance = new Pilot()
        pilotInstance.properties = params
        return [pilotInstance: pilotInstance]
    }

    @Secured(['ROLE_ADMIN'])
    def save = {
        def pilotInstance = new Pilot(params)
        if (pilotInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'pilot.label', default: 'Pilot'), pilotInstance.id])}"
            redirect(action: "show", id: pilotInstance.id)
        }
        else {
            render(view: "create", model: [pilotInstance: pilotInstance])
        }
    }

    def show = {
        def pilotInstance = Pilot.get(params.id)
        if (!pilotInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'pilot.label', default: 'Pilot'), params.id])}"
            redirect(action: "list")
        }
        else {
            [pilotInstance: pilotInstance]
        }
    }

    @Secured(['ROLE_ADMIN'])
    def edit = {
        def pilotInstance = Pilot.get(params.id)
        if (!pilotInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'pilot.label', default: 'Pilot'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [pilotInstance: pilotInstance]
        }
    }

    @Secured(['ROLE_ADMIN'])
    def update = {
        def pilotInstance = Pilot.get(params.id)
        if (pilotInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (pilotInstance.version > version) {

                    pilotInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [
                        message(code: 'pilot.label', default: 'Pilot')]
                    as Object[], "Another user has updated this Pilot while you were editing")
                    render(view: "edit", model: [pilotInstance: pilotInstance])
                    return
                }
            }
            pilotInstance.properties = params
            if (!pilotInstance.hasErrors() && pilotInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'pilot.label', default: 'Pilot'), pilotInstance.id])}"
                redirect(action: "show", id: pilotInstance.id)
            }
            else {
                render(view: "edit", model: [pilotInstance: pilotInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'pilot.label', default: 'Pilot'), params.id])}"
            redirect(action: "list")
        }
    }

    @Secured(['ROLE_ADMIN'])
    def delete = {
        def pilotInstance = Pilot.get(params.id)
        if (pilotInstance) {
            try {
                pilotInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'pilot.label', default: 'Pilot'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'pilot.label', default: 'Pilot'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'pilot.label', default: 'Pilot'), params.id])}"
            redirect(action: "list")
        }
    }

    def displayPicture = {
        def pilotInstance = Pilot.get(params.id)
        response.contentType = "image/jpeg"
        response.contentLength = pilotInstance?.picture?.length
        response.outputStream.write(pilotInstance?.picture)
    }

    @Secured(['ROLE_ADMIN'])
    def upload = {
        if (request instanceof MultipartHttpServletRequest) {
            MultipartHttpServletRequest mpr = (MultipartHttpServletRequest)request;
            CommonsMultipartFile csvFile = (CommonsMultipartFile) mpr.getFile("userFile");
            if (!csvFile.empty) {
                Pilot.importFromStream(csvFile.inputStream)
                redirect(action: "list")
            } else {
                flash.message = "file cannot be empty"
            }
        }
    }

    @Secured(['ROLE_ADMIN'])
    def upload_picture = {
        if (request instanceof MultipartHttpServletRequest) {
            MultipartHttpServletRequest mpr = (MultipartHttpServletRequest)request;
            CommonsMultipartFile imageFile = (CommonsMultipartFile) mpr.getFile("userFile");

            def pilotInstance = Pilot.get(params.pilot.id)
            pilotInstance.picture = imageFile.bytes
            pilotInstance.save();
            redirect(action: "show", id: params.pilot.id)
        }
    }
}
