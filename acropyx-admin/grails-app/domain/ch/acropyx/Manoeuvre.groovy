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
class Manoeuvre {
    private static final Logger log = Logger.getLogger(Manoeuvre.class.getName())

    String name
    float coefficient

    static constraints = {
        name(blank: false)
        coefficient()
    }

    def String toString() {
        return sprintf('%s (%1.2f)', name, coefficient)
    }

    static removeAll() {
        withTransaction { transaction ->
            try {
                Manoeuvre.findAll().each { it.delete() }
                transaction.flush()
            } catch (e) {
                log.warn("Unable to delete manoeuvres")
                transaction.setRollbackOnly()
            }
        }
    }

    static importFromStream(InputStream inputStream) {
        removeAll();
        inputStream.toCsvReader(['charset':'UTF-8']).eachLine() { fields ->
            if (fields?.length > 1) {
                def manoeuvre = new Manoeuvre(name:fields[0], coefficient:fields[1])
                try {
                    manoeuvre.save(failOnError: true, flush: true)
                }
                catch (Exception e) {
                    e.printStackTrace()
                }
            }
        }
    }
}
