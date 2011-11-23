
<%@ page import="ch.acropyx.Competition" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'competition.label', default: 'Competition')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
		<g:render template="/layouts/navList" collection="['Competition']" var="entityName" />
        <div class="body">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                            <g:sortableColumn property="id" title="${message(code: 'competition.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="code" title="${message(code: 'competition.code.label', default: 'Code')}" />
                        
                            <g:sortableColumn property="name" title="${message(code: 'competition.name.label', default: 'Name')}" />
                        
                            <g:sortableColumn property="type" title="${message(code: 'competition.type.label', default: 'Type')}" />
                        
                            <g:sortableColumn property="startTime" title="${message(code: 'competition.startTime.label', default: 'Start Time')}" />
                        
                            <g:sortableColumn property="endTime" title="${message(code: 'competition.endTime.label', default: 'End Time')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${competitionInstanceList}" status="i" var="competitionInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${competitionInstance.id}">${fieldValue(bean: competitionInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: competitionInstance, field: "code")}</td>
                        
                            <td>${fieldValue(bean: competitionInstance, field: "name")}</td>
                        
                            <td>${fieldValue(bean: competitionInstance, field: "type")}</td>
                        
                            <td><g:formatDate format="${grailsApplication.config.ch.acropyx.dateFormat}" date="${competitionInstance.startTime}" /></td>
                        
                            <td><g:formatDate format="${grailsApplication.config.ch.acropyx.dateFormat}" date="${competitionInstance.endTime}" /></td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${competitionInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
