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

import java.io.InputStream
import java.text.SimpleDateFormat

import org.apache.log4j.Logger
import org.springframework.web.context.request.RequestContextHolder

@MultiTenant
class Pilot extends Competitor {
    private static final Logger log = Logger.getLogger(Pilot.class.getName())

    // Injected MessageSource service
    def messageSource

    byte[] picture
    String country
    Date dateOfBirth
    Integer flyingSinceYear
    String job
    String selection
    String glider
    String sponsor
    Integer civlRank

    static constraints = {
        name(blank: false)
        country(blank: false)
        dateOfBirth(nullable: true)
        flyingSinceYear(nullable: true, range: 1980..2012)
        job(nullable:true)
        selection(nullable:true)
        glider(nullable:true)
        sponsor(nullable:true)
        civlRank(nullable:true)
        picture(nullable:true, maxSize:5000000)
    }

    static transients = ["age"]

    def Integer getAge() {
        if (dateOfBirth) {
            Calendar dob = Calendar.getInstance();
            dob.setTime(dateOfBirth);
            Calendar today = Calendar.getInstance();
            Integer age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
            if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
                age--;
            }
            return age;
        }
        return null;
    }

    static void removeAll() {
        Pilot.findAll().each { pilot ->
            withTransaction { transaction ->
                try {
                    pilot.delete()
                    transaction.flush()
                } catch (e) {
                    log.warn("Unable to delete pilot '${pilot}'")
                    transaction.setRollbackOnly()
                }
            }
        }
    }

    static importFromStream(InputStream inputStream) {
        def dateFormat = new SimpleDateFormat("dd.MM.yyyy")

        removeAll();
        inputStream.toCsvReader(['charset':'UTF-8']).eachLine() { fields ->
            if (fields?.length >= 10) {

                def Date dateOfBirth
                try {
                    dateOfBirth = dateFormat.parse(fields[3])
                } catch (Throwable e) {
                    dateOfBirth = null;
                }

                def Integer flyingSinceYear;
                try {
                    flyingSinceYear = fields[4] as Integer
                } catch (Throwable e) {
                    flyingSinceYear = null;
                }

                def pilot = new Pilot( name:fields[1] + " " + fields[2],
                        country:fields[3],
                        dateOfBirth:dateOfBirth,
                        flyingSinceYear: flyingSinceYear,
                        job: fields[6],
                        glider:fields[7],
                        sponsor:fields[8],
                        bestResult:fields[9],
                        civlRank: fields[10]
                        )
                try {
                    pilot.save(failOnError: true, flush: true)
                } catch (Exception e) {
                    log.warn("Failed to import pilot '${fields[0]} ${}fields[1]}'", e)
                }

                try {
                    URL url = new URL("http://www.acroleague.ch/data/mod_listing/" + fields[9].encodeAsURL());
                    InputStream stream = new BufferedInputStream(url.openStream());
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    byte[] buf = new byte[1024];
                    int n = 0;
                    while (-1!=(n=stream.read(buf))) {
                        out.write(buf, 0, n);
                    }
                    out.close();
                    stream.close();
                    byte[] response = out.toByteArray();
                    pilot.picture = response;
                    pilot.save(failOnError: true, flush: true)
                } catch (Exception e) {
                    log.warn("Failed to import picture for pilot '${fields[0]} ${fields[1]}'", e)
                }
            }
        }
    }

    def String toJSON() {
        '{' + '"name" : "' + name + '",' + '"country" : "' + toCountryISO3166_1()  + '"' + addAge() + addImageSrc() + addFlyingSince() + addGlider() + addSponsor() + addCivlRank() + addTicker() + '}'
    }
    def String addImageSrc() {
        def result = ''
        if ( picture ) {
            result = ',"imageSrc" : "' + buildImgSrc()  + '"'
        }
        result
    }
    def String addAge() {
        def result = ''
        if ( age ) {
            Object[] args = [age]
            result = ',"birthDate" : "' + messageSource.getMessage( 'displayer.pilot.age', args, Locale.default ) + '"'
        }
        result
    }
    def String addGlider() {
        def result = ''
        if ( glider ) {
            Object[] args = [glider]
            result = ',"glider" : "' + messageSource.getMessage( 'displayer.pilot.glider', args, Locale.default ) + '"'
        }
        result
    }
    def String addFlyingSince() {
        def result = ''
        if ( flyingSinceYear ) {
            Object[] args = [
                sprintf('%d', flyingSinceYear)
            ]
            result = ',"flyingSince" : "' + messageSource.getMessage( 'displayer.pilot.flyingSince', args, Locale.default ) + '"'
        }
        result
    }
        def String addSponsor() {
        def result = ''
        if ( sponsor ) {
            Object[] args = [
                sponsor
            ]
            result = ',"sponsor" : "' + messageSource.getMessage( 'displayer.pilot.sponsor', args, Locale.default ) + '"'
        }
        result
    }

   def String addCivlRank() {
        def result = ''
        if ( civlRank ) {
            Object[] args = [
                    civlRank
            ]
            result = ',"ranking" : "' + messageSource.getMessage( 'displayer.pilot.civlRank', args, Locale.default ) + '"'
        }
        result
    }
    
    def String addTicker() {
        def result = ',"ticker" : "'
        if ( job || glider || sponsor || bestResult || flyingSinceYear ) {
            result += messageSource.getMessage( 'displayer.ticker.pilotName', [name]as Object[], Locale.default )
            if ( job ) {
                result += addSeparator() + messageSource.getMessage( 'displayer.ticker.job', [job]as Object[], Locale.default )
            }
            
            if ( civlRank ) {
                result +=  addSeparator() + messageSource.getMessage( 'displayer.ticker.civlRank', [civlRank]as Object[], Locale.default )
            }
            
            if ( glider ) {
                result += addSeparator() + messageSource.getMessage( 'displayer.ticker.glider', [glider]as Object[], Locale.default )
            }
            if ( sponsor ) {
                result +=  addSeparator() + messageSource.getMessage( 'displayer.ticker.sponsor', [sponsor]as Object[], Locale.default )
            }
            if ( bestResult ) {
                result += addSeparator() + messageSource.getMessage( 'displayer.ticker.bestResult', [bestResult]as Object[], Locale.default )
            }
            if ( flyingSinceYear ) {
                result += addSeparator() + messageSource.getMessage( 'displayer.ticker.flyingSince', [
                    sprintf('%d', flyingSinceYear)]
                as Object[], Locale.default )
            }
        }
        result += '"'
    }
    def String addSeparator() {
        '      '
    }
    def String buildImgSrc() {
        def serverName = RequestContextHolder.currentRequestAttributes().getRequest().getServerName()
        def serverPort = RequestContextHolder.currentRequestAttributes().getRequest().getServerPort()
        "http://" + serverName + ":" + serverPort + "/pilot/displayPicture/" + id
    }
    def String toCountryISO3166_1() {
        country.substring( 0, 2 )
    }
    
    
}
