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
class Team extends Competitor {
    // Injected MessageSource service
    def messageSource

    static hasMany = [ pilots : Pilot ]

    static constraints = {
        name(blank: false)
        bestResult()
        pilots(minSize: 2, maxSize: 2)
    }

    def String toJSON() {
        '{ "name" : "' + name + '"' + addBestResult() + addTicker() + ',"pilots" : [' + buildPilots()  + '] }'
    }

    def String addBestResult() {
        def result = ''
        if ( bestResult ) {
            Object[] args = [bestResult]
            result = ',"bestResult" : "' + messageSource.getMessage( 'displayer.competitor.bestResult', args, Locale.default ) + '"'
        }
        result
    }

    def String addTicker() {
        def result = ',"ticker" : "'
        if ( bestResult ) {
            result += messageSource.getMessage( 'displayer.ticker.teamName', [name]as Object[], Locale.default )
            if ( bestResult ) {
                result += addSeparator() + messageSource.getMessage( 'displayer.ticker.bestResult', [bestResult]as Object[], Locale.default )
            }
        }
        result += '"'
    }
    def String addSeparator() {
        '      '
    }

    def String buildPilots() {
        def nbOfPilots = pilots?.size()
        def ii = 0
        def json = ''
        if ( pilots ) {
            for ( pilot in pilots ) {
                ii++
                json += pilot.toJSON()
                if ( ii < nbOfPilots ) {
                    json += ','
                }
            }
        }
        json
    }
}
