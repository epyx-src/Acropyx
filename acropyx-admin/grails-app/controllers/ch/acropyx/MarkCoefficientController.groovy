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

class MarkCoefficientController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = 100 //Math.min(params.max ? params.int('max') : 10, 100)
        [markCoefficientInstanceList: MarkCoefficient.list(params), markCoefficientInstanceTotal: MarkCoefficient.count()]
    }

    def create = {
        def markCoefficientInstance = new MarkCoefficient()
        markCoefficientInstance.properties = params
        return [markCoefficientInstance: markCoefficientInstance]
    }

    def save = {
        def markCoefficientInstance = new MarkCoefficient(params)
        if (markCoefficientInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'markCoefficient.label', default: 'MarkCoefficient'), markCoefficientInstance.id])}"
            redirect(action: "show", id: markCoefficientInstance.id)
        }
        else {
            render(view: "create", model: [markCoefficientInstance: markCoefficientInstance])
        }
    }

    def show = {
        def markCoefficientInstance = MarkCoefficient.get(params.id)
        if (!markCoefficientInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'markCoefficient.label', default: 'MarkCoefficient'), params.id])}"
            redirect(action: "list")
        }
        else {
            [markCoefficientInstance: markCoefficientInstance]
        }
    }

    def edit = {
        def markCoefficientInstance = MarkCoefficient.get(params.id)
        if (!markCoefficientInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'markCoefficient.label', default: 'MarkCoefficient'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [markCoefficientInstance: markCoefficientInstance]
        }
    }

    def update = {
        def markCoefficientInstance = MarkCoefficient.get(params.id)
        if (markCoefficientInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (markCoefficientInstance.version > version) {

                    markCoefficientInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [
                        message(code: 'markCoefficient.label', default: 'MarkCoefficient')]
                    as Object[], "Another user has updated this MarkCoefficient while you were editing")
                    render(view: "edit", model: [markCoefficientInstance: markCoefficientInstance])
                    return
                }
            }
            markCoefficientInstance.properties = params
            if (!markCoefficientInstance.hasErrors() && markCoefficientInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'markCoefficient.label', default: 'MarkCoefficient'), markCoefficientInstance.id])}"
                redirect(action: "show", id: markCoefficientInstance.id)
            }
            else {
                render(view: "edit", model: [markCoefficientInstance: markCoefficientInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'markCoefficient.label', default: 'MarkCoefficient'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def markCoefficientInstance = MarkCoefficient.get(params.id)
        if (markCoefficientInstance) {
            try {
                markCoefficientInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'markCoefficient.label', default: 'MarkCoefficient'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'markCoefficient.label', default: 'MarkCoefficient'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'markCoefficient.label', default: 'MarkCoefficient'), params.id])}"
            redirect(action: "list")
        }
    }
}
