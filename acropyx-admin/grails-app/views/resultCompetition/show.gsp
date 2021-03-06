<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main_event" />
        <title>Home</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><a class="admin" href="${createLink(uri: '/admin')}"><g:message code="default.admin.label"/></a></span>
            <sec:ifNotLoggedIn>
                <span class="menuLogin"><a class="login" href="${createLink(uri: '/login')}"><g:message code="default.login.label"/></a></span>
            </sec:ifNotLoggedIn>
            <sec:ifLoggedIn>
                <span class="menuLogin"><sec:username />(<g:link controller="logout"><g:message code="default.logout.label"/></g:link>)</span>
            </sec:ifLoggedIn>
        </div>
        <div class="body">
        
            <h1>Competition '${competitionInstance}'</h1>
            <g:if test="${flash.message}">
                <div class="errors">${flash.message}</div>
            </g:if>
            <div class="list">
                <table style="width: 0">
                    <thead>
                        <tr>
                            <th>Rank</th>
                            <th>Competitor</th>
                            
		                    <g:each in="${endedRuns}" var="endedRun">
                                <th><g:link controller="run" action="show" id="${endedRun.id}" >Run:${endedRun.name}</g:link></th>	                    
		                    </g:each>                            
                            
                            <th>Overall</th>
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${competitorResults}" status="i" var="competitorResult">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

                            <td>${i+1}</td>   
                            
                            <g:if test="${competitorResult.get('competitor') instanceof ch.acropyx.Team}">
                                <td><g:link controller="team" action="show" id="${competitorResult.get('competitor').id}">${competitorResult.get('competitor').name}</g:link></td>
                            </g:if>
                            <g:else>
                                <td><g:link controller="pilot" action="show" id="${competitorResult.get('competitor').id}">${competitorResult.get('competitor').name}</g:link></td>
                            </g:else>
                            
                            <g:each in="${endedRuns}" var="endedRun">
                            	<% def flight = competitorResult.flights?.get(endedRun.id) %>
                                <td><g:link controller="flight" action="show" id="${flight?.id}"><g:formatNumber number="${flight?.result}" format="#.#" roundingMode="HALF_UP" /></g:link><span style="color:#CCC"><g:formatNumber number="${flight?.result}" format=" (#.###)" roundingMode="HALF_UP" /></span></td>                      
                            </g:each>    
                            
                            <td><g:formatNumber number="${competitorResult.get('overall')}" format="#.#" roundingMode="HALF_UP" /><span style="color:#CCC"><g:formatNumber number="${competitorResult.get('overall')}" format=" (#.###)" roundingMode="HALF_UP" /></span></td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
        </div>
    </body>
</html>