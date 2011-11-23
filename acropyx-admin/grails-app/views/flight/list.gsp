
<%@ page import="ch.acropyx.Flight" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'flight.label', default: 'Flight')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
		<g:render template="/layouts/navList" collection="['Flight']" var="entityName" />
        <div class="body">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                            <g:sortableColumn property="id" title="${message(code: 'flight.id.label', default: 'Id')}" />
                        
                            <th><g:message code="flight.competitor.label" default="Competitor" /></th>
                        
                            <th><g:message code="flight.run.label" default="Run" /></th>
                        
                            <g:sortableColumn property="startTime" title="${message(code: 'flight.startTime.label', default: 'Start Time')}" />
                        
                            <g:sortableColumn property="endTime" title="${message(code: 'flight.endTime.label', default: 'End Time')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${flightInstanceList}" status="i" var="flightInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${flightInstance.id}">${fieldValue(bean: flightInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: flightInstance, field: "competitor")}</td>
                        
                            <td>${fieldValue(bean: flightInstance, field: "run")}</td>
                        
                            <td><g:formatDate format="${grailsApplication.config.ch.acropyx.dateFormat}" date="${flightInstance.startTime}" /></td>
                        
                            <td><g:formatDate format="${grailsApplication.config.ch.acropyx.dateFormat}" date="${flightInstance.endTime}" /></td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${flightInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
