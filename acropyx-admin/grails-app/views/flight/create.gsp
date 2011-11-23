

<%@ page import="ch.acropyx.Flight" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'flight.label', default: 'Flight')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
		<g:render template="/layouts/navCreate" collection="['Flight']" var="entityName" />
        <div class="body">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${flightInstance}">
            <div class="errors">
                <g:renderErrors bean="${flightInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="competitor"><g:message code="flight.competitor.label" default="Competitor" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: flightInstance, field: 'competitor', 'errors')}">
                                    <g:select name="competitor.id" from="${ch.acropyx.Competitor.list()}" optionKey="id" value="${flightInstance?.competitor?.id}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="run"><g:message code="flight.run.label" default="Run" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: flightInstance, field: 'run', 'errors')}">
                                    <g:select name="run.id" from="${ch.acropyx.Run.list()}" optionKey="id" value="${flightInstance?.run?.id}"  />
                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
