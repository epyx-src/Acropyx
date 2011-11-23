
<%@ page import="ch.acropyx.Manoeuvre" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'manoeuvre.label', default: 'Manoeuvre')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
		<g:render template="/layouts/navListWithUpload" collection="['Manoeuvre']" var="entityName" />
        <div class="body">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                            <g:sortableColumn property="id" title="${message(code: 'manoeuvre.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="name" title="${message(code: 'manoeuvre.name.label', default: 'Name')}" />
                        
                            <g:sortableColumn property="coefficient" title="${message(code: 'manoeuvre.coefficient.label', default: 'Coefficient')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${manoeuvreInstanceList}" status="i" var="manoeuvreInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${manoeuvreInstance.id}">${fieldValue(bean: manoeuvreInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: manoeuvreInstance, field: "name")}</td>
                        
                            <td>${fieldValue(bean: manoeuvreInstance, field: "coefficient")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${manoeuvreInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
