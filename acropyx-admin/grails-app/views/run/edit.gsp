

<%@ page import="ch.acropyx.Run" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'run.label', default: 'Run')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
		<g:render template="/layouts/navEdit" collection="['Run']" var="entityName" />
        <div class="body">
            <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${runInstance}">
            <div class="errors">
                <g:renderErrors bean="${runInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${runInstance?.id}" />
                <g:hiddenField name="version" value="${runInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="competition"><g:message code="run.competition.label" default="Competition" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: runInstance, field: 'competition', 'errors')}">
                                    <g:select name="competition.id" from="${ch.acropyx.Competition.list()}" optionKey="id" value="${runInstance?.competition?.id}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="name"><g:message code="run.name.label" default="Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: runInstance, field: 'name', 'errors')}">
                                    <g:textField name="name" value="${runInstance?.name}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="startTime"><g:message code="run.startTime.label" default="Start Time (dd/MM/yyyy HH:mm)" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: runInstance, field: 'startTime', 'errors')}">
                                    <g:textField name="startTime" value="${formatDate(format:grailsApplication.config.ch.acropyx.dateFormat,date:runInstance?.startTime)}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="endTime"><g:message code="run.endTime.label" default="End Time (dd/MM/yyyy HH:mm)" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: runInstance, field: 'endTime', 'errors')}">
                                    <g:textField name="endTime" value="${formatDate(format:grailsApplication.config.ch.acropyx.dateFormat,date:runInstance?.endTime)}" />
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
