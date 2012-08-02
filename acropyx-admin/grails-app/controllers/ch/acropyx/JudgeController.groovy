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

class JudgeCommand {
    String name
    Judge.Role role
    String username
    String password
    String passwordRepeat

    static constraints = {
        password (nullable: false, blank: false)
        passwordRepeat (
                nullable: false,
                blank: false,
                validator: { passwordRepeat, cmd ->
                    return passwordRepeat == cmd.password
                }
                )
    }
}

class JudgeController {

    def springSecurityService

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 100, 200)
        [judgeInstanceList: Judge.list(params), judgeInstanceTotal: Judge.count()]
    }

    @Secured(['ROLE_ADMIN'])
    def create = {
        def judgeInstance = new Judge()
        judgeInstance.properties = params
        return [judgeInstance: judgeInstance]
    }

    @Secured(['ROLE_ADMIN'])
    def save = { JudgeCommand cmd ->
        if (cmd.validate()) {
            def judgeInstance = new Judge(params)
            if (judgeInstance.save(flush: true)) {
                def user = new AcroUser(username: cmd.username, enabled: true, password: springSecurityService.encodePassword(cmd.password))
                user.save(flush: true)
                UserRole.create(user, Role.findByAuthority("ROLE_JUDGE"), true)

                flash.message = "${message(code: 'default.created.message', args: [message(code: 'judge.label', default: 'Judge'), judgeInstance.id])}"
                redirect(action: "show", id: judgeInstance.id)
            }
            else {
                render(view: "create", model: [judgeInstance: judgeInstance])
            }
        }
        else {
            render(view: "create", model: [judgeInstance: cmd])
        }
    }

    def show = {
        def judgeInstance = Judge.get(params.id)
        if (!judgeInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'judge.label', default: 'Judge'), params.id])}"
            redirect(action: "list")
        }
        else {
            [judgeInstance: judgeInstance]
        }
    }

    @Secured(['ROLE_ADMIN'])
    def edit = {
        def judgeInstance = Judge.get(params.id)
        if (!judgeInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'judge.label', default: 'Judge'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [judgeInstance: judgeInstance]
        }
    }

    @Secured(['ROLE_ADMIN'])
    def update = {
        def judgeInstance = Judge.get(params.id)
        if (judgeInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (judgeInstance.version > version) {

                    judgeInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [
                        message(code: 'judge.label', default: 'Judge')]
                    as Object[], "Another user has updated this Judge while you were editing")
                    render(view: "edit", model: [judgeInstance: judgeInstance])
                    return
                }
            }
            judgeInstance.properties = params
            if (!judgeInstance.hasErrors() && judgeInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'judge.label', default: 'Judge'), judgeInstance.id])}"
                redirect(action: "show", id: judgeInstance.id)
            }
            else {
                render(view: "edit", model: [judgeInstance: judgeInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'judge.label', default: 'Judge'), params.id])}"
            redirect(action: "list")
        }
    }

    @Secured(['ROLE_ADMIN'])
    def delete = {
        def judgeInstance = Judge.get(params.id)
        if (judgeInstance) {
            try {
                judgeInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'judge.label', default: 'Judge'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'judge.label', default: 'Judge'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'judge.label', default: 'Judge'), params.id])}"
            redirect(action: "list")
        }
    }
}
