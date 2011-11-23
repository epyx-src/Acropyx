
<%@ page import="ch.acropyx.Mark" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'mark.label', default: 'Mark')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
		<g:render template="/layouts/navList" collection="['Mark']" var="entityName" />
        <div class="body">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                            <g:sortableColumn property="id" title="${message(code: 'mark.id.label', default: 'Id')}" />
                        
                            <th><g:message code="mark.flight.label" default="Flight" /></th>
                        
                            <th><g:message code="mark.judge.label" default="Judge" /></th>
                        
                            <th><g:message code="mark.markDefinition.label" default="Mark Definition" /></th>
                        
                            <g:sortableColumn property="mark" title="${message(code: 'mark.mark.label', default: 'Mark')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${markInstanceList}" status="i" var="markInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${markInstance.id}">${fieldValue(bean: markInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: markInstance, field: "flight")}</td>
                        
                            <td>${fieldValue(bean: markInstance, field: "judge")}</td>
                        
                            <td>${fieldValue(bean: markInstance, field: "markDefinition")}</td>
                        
                            <td>${fieldValue(bean: markInstance, field: "mark")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${markInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
