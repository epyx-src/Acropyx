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

class VoteController {

    def edit = {
        try {
            def flightInstance = Flight.get(params.id)
            if (!flightInstance) {
                render(status:400, text:"No flight selected")
            }

            session["originReferer"] = request.getHeader("Referer")
            def posibleMarks = new ArrayList()
            def i = 0; // TODO: Define configurable value
            while ( i < 21 ) // TODO: Define configurable value for initial(now 0) and step (now .5)
            {                      
                
                posibleMarks.add(i/2)
                i = i + 1
            }
            
            def markCoefficients = flightInstance.run.competition.markCoefficients
            def judges = flightInstance.run.competition.judges

            def model = [flightInstance: flightInstance,
                        judges: judges,
                        markCoefficients: markCoefficients,
                        posibleMarks: posibleMarks]

            judges.each() { judge ->
                markCoefficients.each() { markCoefficient ->
                    def mark = Mark.searchMark(flightInstance, judge, markCoefficient.markDefinition)
                    if (mark) {
                        model.put("Mark_" + judge.id + "_" + markCoefficient.markDefinition.id , mark.mark)
                    }
                }
            }

            return model
        }
        catch (Throwable e) {
            render(status:400)
        }
    }

    def save = {
        def flightInstance = Flight.get(params.id)

        params.each { param ->
            if (param.key.startsWith("Mark_")) {
                def ids = param.key.tokenize("_")

                def judge = Judge.get(ids[1])
                def markDefinition = MarkDefinition.get(ids[2])

                def mark = Mark.searchMark(flightInstance, judge, markDefinition)
                if (mark) {
                    mark.mark = param.value as float
                } else {
                    mark = new Mark(flight: flightInstance, judge: judge, markDefinition: markDefinition, mark: param.value)
                }
                mark.save()
            }
        }

        if (session["originReferer"]) {
            redirect(url: session["originReferer"])
        } else {
            redirect(controller: "event")
        }
    }
}
