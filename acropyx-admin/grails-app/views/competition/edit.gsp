

<%@ page import="ch.acropyx.Competition" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'competition.label', default: 'Competition')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
		<g:render template="/layouts/navEdit" collection="['Competition']" var="entityName" />
        <div class="body">
            <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${competitionInstance}">
            <div class="errors">
                <g:renderErrors bean="${competitionInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${competitionInstance?.id}" />
                <g:hiddenField name="version" value="${competitionInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="code"><g:message code="competition.code.label" default="Code" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: competitionInstance, field: 'code', 'errors')}">
                                    <g:textField name="code" maxlength="15" value="${competitionInstance?.code}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="name"><g:message code="competition.name.label" default="Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: competitionInstance, field: 'name', 'errors')}">
                                    <g:textField name="name" value="${competitionInstance?.name}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="type"><g:message code="competition.type.label" default="Type" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: competitionInstance, field: 'type', 'errors')}">
                                    <g:select name="type" from="${ch.acropyx.Competition$Type?.values()}" keys="${ch.acropyx.Competition$Type?.values()*.name()}" value="${competitionInstance?.type?.name()}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="markCoefficients"><g:message code="competition.markCoefficients.label" default="Mark Coefficients" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: competitionInstance, field: 'markCoefficients', 'errors')}">
                                    
<ul>
<g:each in="${competitionInstance?.markCoefficients?}" var="m">
    <li><g:link controller="markCoefficient" action="show" id="${m.id}">${m?.encodeAsHTML()}</g:link></li>
</g:each>
</ul>
<g:link controller="markCoefficient" action="create" params="['competition.id': competitionInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'markCoefficient.label', default: 'MarkCoefficient')])}</g:link>

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="judges"><g:message code="competition.judges.label" default="Judges" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: competitionInstance, field: 'judges', 'errors')}">
                                    <g:select name="judges" from="${ch.acropyx.Judge.list()}" multiple="yes" optionKey="id" size="5" value="${competitionInstance?.judges*.id}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="startTime"><g:message code="competition.startTime.label" default="Start Time (dd/MM/yyyy HH:mm)" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: competitionInstance, field: 'startTime', 'errors')}">
                                    <g:textField name="startTime" value="${formatDate(format:grailsApplication.config.ch.acropyx.dateFormat,date:competitionInstance?.startTime)}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="endTime"><g:message code="competition.endTime.label" default="End Time (dd/MM/yyyy HH:mm)" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: competitionInstance, field: 'endTime', 'errors')}">
                                    <g:textField name="endTime" value="${formatDate(format:grailsApplication.config.ch.acropyx.dateFormat,date:competitionInstance?.endTime)}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="runs"><g:message code="competition.runs.label" default="Runs" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: competitionInstance, field: 'runs', 'errors')}">
                                    
<ul>
<g:each in="${competitionInstance?.runs?}" var="r">
    <li><g:link controller="run" action="show" id="${r.id}">${r?.encodeAsHTML()}</g:link></li>
</g:each>
</ul>
<g:link controller="run" action="create" params="['competition.id': competitionInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'run.label', default: 'Run')])}</g:link>

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
