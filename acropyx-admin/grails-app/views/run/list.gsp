
<%@ page import="ch.acropyx.Run" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'run.label', default: 'Run')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
		<g:render template="/layouts/navList" collection="['Run']" var="entityName" />
        <div class="body">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                            <g:sortableColumn property="id" title="${message(code: 'run.id.label', default: 'Id')}" />
                        
                            <th><g:message code="run.competition.label" default="Competition" /></th>
                        
                            <g:sortableColumn property="name" title="${message(code: 'run.name.label', default: 'Name')}" />
                        
                            <g:sortableColumn property="startTime" title="${message(code: 'run.startTime.label', default: 'Start Time')}" />
                        
                            <g:sortableColumn property="endTime" title="${message(code: 'run.endTime.label', default: 'End Time')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${runInstanceList}" status="i" var="runInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${runInstance.id}">${fieldValue(bean: runInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: runInstance, field: "competition")}</td>
                        
                            <td>${fieldValue(bean: runInstance, field: "name")}</td>
                        
                            <td><g:formatDate format="${grailsApplication.config.ch.acropyx.dateFormat}" date="${runInstance.startTime}" /></td>
                        
                            <td><g:formatDate format="${grailsApplication.config.ch.acropyx.dateFormat}" date="${runInstance.endTime}" /></td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${runInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
