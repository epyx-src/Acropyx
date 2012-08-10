<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main_event" />
        <export:resource />
        <title>Home</title>
     
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><a class="admin" href="${createLink(uri: '/admin')}"><g:message code="default.admin.label"/></a></span>
            <span class="menuButton"><g:link class="list" controller="run" action="export" id="${runInstance.id}">Export</g:link></span>
            <g:jasperReport controller="resultRun" action="reportRunResults"  jasper="runresults" format="PDF" name="PDF">
                <input type="hidden" name="run_id" value="${runInstance.id}"/>
                <input type="hidden" name="ACROPYX_COMPETITION" value="TEST"/>
                <input type="hidden" name="ACROPYX_RUN" value="RUN1"/>
                <input type="hidden" name="ACROPYX_RESULT" value="RESULTS"/>

            </g:jasperReport>
            <g:jasperReport controller="resultRun" action="reportRunResults"  jasper="runresults_xls" format="XLS" name="Run-Result">
                <input type="hidden" name="run_id" value="${runInstance.id}"/>
                <input type="hidden" name="ACROPYX_COMPETITION" value="TEST"/>
                <input type="hidden" name="ACROPYX_RUN" value="RUN1"/>
                <input type="hidden" name="ACROPYX_RESULT" value="RESULTS"/>

            </g:jasperReport>
            %{--<span class="menuButton"><g:link class="list" controller="run" action="export_pdf" id="${runInstance.id}">Export to PDF</g:link></span>--}%

            
            <sec:ifNotLoggedIn>
                <span class="menuLogin"><a class="login" href="${createLink(uri: '/login')}"><g:message code="default.login.label"/></a></span>
            </sec:ifNotLoggedIn>
            <sec:ifLoggedIn>
                <span class="menuLogin"><sec:username />(<g:link controller="logout"><g:message code="default.logout.label"/></g:link>)</span>
            </sec:ifLoggedIn>
        </div>
        <div class="body">
        
            <h1>Run '${runInstance}' ${params.notEndedFlights ? ' - not completed flights marked with (*)' : ''}</h1>
            <g:if test="${flash.message}">
                <div class="errors">${flash.message}</div>
            </g:if>
            <div class="list">
                <table style="width: 0">
                    <thead>
                        <tr>
                            <th>Rank</th>
                            <th>Competitor</th>
                            
                            <g:each in="${runInstance.competition.markCoefficients}" status="i" var="markCoefficient">
                                <th>${markCoefficient.markDefinition}</th>
                            </g:each>
                            <th>Warnings</th>
                                                        
                            <th>Result</th>
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${flights}" status="i" var="flight">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

                            <td>${i+1}</td>
                            
                            <g:if test="${flight instanceof ch.acropyx.Team}">
                                <td><g:link controller="team" action="show" id="${flight.competitor.id}">${flight.competitor.name}</g:link>${flight.isActive() ? ' (*)' : ''}</td>
                            </g:if>
                            <g:else>
                                <td><g:link controller="pilot" action="show" id="${flight.competitor.id}">${flight.competitor.name}</g:link>${flight.isActive() ? ' (*)' : ''}</td>
                            </g:else>
                            
                            <% def detailedResults = flight.computeDetailedResults() %>
                            <g:each in="${runInstance.competition.markCoefficients}" var="markCoefficient">
                                <td style="text-align: right;"><g:formatNumber number="${detailedResults.get(markCoefficient.id)}" format="0.000" roundingMode="HALF_UP" /></td>
                            </g:each>

                            <td>${flight.warnings}</td>
                            
                            <% def result = flight.computeResult(detailedResults) %>
                            <td style="text-align: right;"><g:link controller="flight" action="show" id="${flight.id}"><g:formatNumber number="${result}" format="0.000" roundingMode="HALF_UP" /></g:link></td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>

        </div>
    </body>
</html>