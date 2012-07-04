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

class FlightManoeuvreController {

    def edit = {
        try {
            def flightInstance = Flight.get(params.id)
            if (!flightInstance) {
                render(status:400, text:"No flight selected")
            }

            session["originReferer"] = request.getHeader("Referer")
            def model = [flightInstance: flightInstance, manoeuvres: Manoeuvre.listOrderByName()]

            def flightManoeuvres = [];
            flightInstance.manoeuvres?.each() { flightManoeuvre ->
                flightManoeuvres.add(flightManoeuvre)
            }
            model.put("flightManoeuvres", flightManoeuvres)

            return model
        }
        catch (Throwable e) {
            render(status:400)
        }
    }

    def save = {
        def flightInstance = Flight.get(params.id)

        flightInstance.manoeuvres?.clear();

        params.get('flightManoeuvreIds').each { value ->
            def manoeuvre = Manoeuvre.get(value as long)
            flightInstance.manoeuvres.add(manoeuvre);
        }

        flightInstance.save()

        if (session["originReferer"]) {
            redirect(url: session["originReferer"])
        } else {
            redirect(controller: "event")
        }
    }
}
