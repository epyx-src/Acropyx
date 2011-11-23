
<%@ page import="ch.acropyx.MarkDefinition" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'markDefinition.label', default: 'MarkDefinition')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
		<g:render template="/layouts/navListWithUpload" collection="['MarkDefinition']" var="entityName" />
        <div class="body">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                            <g:sortableColumn property="id" title="${message(code: 'markDefinition.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="name" title="${message(code: 'markDefinition.name.label', default: 'Name')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${markDefinitionInstanceList}" status="i" var="markDefinitionInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${markDefinitionInstance.id}">${fieldValue(bean: markDefinitionInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: markDefinitionInstance, field: "name")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${markDefinitionInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
