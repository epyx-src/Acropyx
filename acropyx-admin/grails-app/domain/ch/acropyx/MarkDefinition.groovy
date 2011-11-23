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

import grails.plugin.multitenant.core.groovy.compiler.MultiTenant

import org.apache.log4j.Logger

@MultiTenant
class MarkDefinition {
    private static final Logger log = Logger.getLogger(MarkDefinition.class.getName())

    String name

    static constraints = {
        name(blank: false, unique: 'tenantId')
    }

    def String toString() {
        return "${name}"
    }

    static removeAll() {
        withTransaction { transaction ->
            try {
                MarkDefinition.findAll().each { it.delete() }
                transaction.flush()
            } catch (e) {
                log.warn("Unable to delete mark definitions")
                transaction.setRollbackOnly()
            }
        }
    }

    static importFromStream(InputStream inputStream) {
        removeAll();
        inputStream.toCsvReader(['charset':'UTF-8']).eachLine() { fields ->
            if (fields?.length > 0) {
                def markDefinition = new MarkDefinition(name:fields[0])
                try {
                    markDefinition.save(failOnError: true, flush: true)
                }
                catch (Exception e) {
                    e.printStackTrace()
                }
            }
        }
    }
}