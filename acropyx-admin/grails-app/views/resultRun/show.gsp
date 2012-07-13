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
            <span class="menuButton"><g:link class="list" controller="run" action="export_pdf" id="${runInstance.id}">Export PDF</g:link></span>
            
            
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
                                <td><g:formatNumber number="${detailedResults.get(markCoefficient.id)}" format="#.#" roundingMode="HALF_UP" /><span style="color:#CCC"><g:formatNumber number="${detailedResults.get(markCoefficient.id)}" format=" (#.###)" roundingMode="HALF_UP" /></span></td>
                            </g:each>
                            
                            <% def result = flight.computeResult(detailedResults) %>
                            <td><g:link controller="flight" action="show" id="${flight.id}"><g:formatNumber number="${result}" format="#.#" roundingMode="HALF_UP" /></g:link><span style="color:#CCC"><g:formatNumber number="${result}" format=" (#.###)" roundingMode="HALF_UP" /></span></td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
        </div>
    </body>
</html>