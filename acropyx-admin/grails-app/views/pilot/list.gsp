
<%@ page import="ch.acropyx.Pilot" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'pilot.label', default: 'Pilot')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
		<g:render template="/layouts/navListWithUpload" collection="['Pilot']" var="entityName" />
        <div class="body">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                            <g:sortableColumn property="id" title="${message(code: 'pilot.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="name" title="${message(code: 'pilot.name.label', default: 'Name')}" />
                        
                            <g:sortableColumn property="country" title="${message(code: 'pilot.country.label', default: 'Country')}" />
                        
                            <g:sortableColumn property="age" title="${message(code: 'pilot.age.label', default: 'Age')}" />
                        
                            <g:sortableColumn property="flyingSinceYear" title="${message(code: 'pilot.flyingSinceYear.label', default: 'Flying Since Year')}" />
                        
                            <g:sortableColumn property="job" title="${message(code: 'pilot.job.label', default: 'Job')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${pilotInstanceList}" status="i" var="pilotInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${pilotInstance.id}">${fieldValue(bean: pilotInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: pilotInstance, field: "name")}</td>
                        
                            <td><g:country code="${pilotInstance.country}" /></td>
                        
                            <td>${fieldValue(bean: pilotInstance, field: "age")}</td>
                        
                            <td>${fieldValue(bean: pilotInstance, field: "flyingSinceYear")}</td>
                        
                            <td>${fieldValue(bean: pilotInstance, field: "job")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${pilotInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
