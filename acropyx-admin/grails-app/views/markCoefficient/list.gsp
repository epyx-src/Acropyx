
<%@ page import="ch.acropyx.MarkCoefficient" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'markCoefficient.label', default: 'MarkCoefficient')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                            <g:sortableColumn property="id" title="${message(code: 'markCoefficient.id.label', default: 'Id')}" />
                        
                            <th><g:message code="markCoefficient.markDefinition.label" default="Mark Definition" /></th>
                        
                            <th><g:message code="markCoefficient.competition.label" default="Competition" /></th>
                        
                            <g:sortableColumn property="coefficient" title="${message(code: 'markCoefficient.coefficient.label', default: 'Coefficient')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${markCoefficientInstanceList}" status="i" var="markCoefficientInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${markCoefficientInstance.id}">${fieldValue(bean: markCoefficientInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: markCoefficientInstance, field: "markDefinition")}</td>
                        
                            <td>${fieldValue(bean: markCoefficientInstance, field: "competition")}</td>
                        
                            <td>${fieldValue(bean: markCoefficientInstance, field: "coefficient")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${markCoefficientInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
