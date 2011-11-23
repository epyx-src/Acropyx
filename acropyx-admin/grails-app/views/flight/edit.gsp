

<%@ page import="ch.acropyx.Flight" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'flight.label', default: 'Flight')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
		<g:render template="/layouts/navEdit" collection="['Flight']" var="entityName" />
        <div class="body">
            <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${flightInstance}">
            <div class="errors">
                <g:renderErrors bean="${flightInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${flightInstance?.id}" />
                <g:hiddenField name="version" value="${flightInstance?.version}" />
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
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="startTime"><g:message code="flight.startTime.label" default="Start Time (dd/MM/yyyy HH:mm)" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: flightInstance, field: 'startTime', 'errors')}">
                                    <g:textField name="startTime" value="${formatDate(format:grailsApplication.config.ch.acropyx.dateFormat,date:flightInstance?.startTime)}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="endTime"><g:message code="flight.endTime.label" default="End Time (dd/MM/yyyy HH:mm)" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: flightInstance, field: 'endTime', 'errors')}">
                                    <g:textField name="endTime" value="${formatDate(format:grailsApplication.config.ch.acropyx.dateFormat,date:flightInstance?.endTime)}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="Manoeuvres">Manoeuvres</label>
                                </td>
                                <td valign="top">
                                    <g:link controller="flightManoeuvre" action="edit" id="${flightInstance.id}">
                                        <g:submitButton name="Manoeuvres"></g:submitButton>
                                    </g:link>
                                </td>
                            </tr>
                            
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="Vote">Vote</label>
                                </td>
                                <td valign="top">
                                    <g:link controller="vote" action="edit" id="${flightInstance.id}">
                                        <g:submitButton name="Vote"></g:submitButton>
                                    </g:link>
                                </td>
                            </tr>
                            
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="warnings"><g:message code="flight.warnings.label" default="Warnings" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: flightInstance, field: 'warnings', 'errors')}">
                                    <g:textField name="warnings" value="${fieldValue(bean: flightInstance, field: 'warnings')}" />
                                </td>
                            </tr>                            
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
