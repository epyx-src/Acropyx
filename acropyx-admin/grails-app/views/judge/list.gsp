
<%@ page import="ch.acropyx.Judge" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'judge.label', default: 'Judge')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
		<g:render template="/layouts/navList" collection="['Judge']" var="entityName" />
        <div class="body">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                            <g:sortableColumn property="id" title="${message(code: 'judge.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="name" title="${message(code: 'judge.name.label', default: 'Name')}" />
                        
                            <g:sortableColumn property="username" title="${message(code: 'judge.username.label', default: 'Username')}" />
                        
                            <g:sortableColumn property="role" title="${message(code: 'judge.role.label', default: 'Role')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${judgeInstanceList}" status="i" var="judgeInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${judgeInstance.id}">${fieldValue(bean: judgeInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: judgeInstance, field: "name")}</td>
                        
                            <td>${fieldValue(bean: judgeInstance, field: "username")}</td>
                        
                            <td>${fieldValue(bean: judgeInstance, field: "role")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${judgeInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
