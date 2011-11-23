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

@MultiTenant
class MarkCoefficient implements Comparable {

    Competition competition
    MarkDefinition markDefinition

    double coefficient

    static belongsTo = [competition: Competition]

    static constraints = {
        markDefinition()
        competition()
        coefficient()
    }

    def int compareTo(Object object) {
        return id.compareTo(object.id)
    }

    def String toString() {
        return sprintf('%s (%1.2f)', markDefinition.name, coefficient)
    }
}
