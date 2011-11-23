

<%@ page import="ch.acropyx.Run" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'run.label', default: 'Run')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
		<g:render template="/layouts/navCreate" collection="['Run']" var="entityName" />
        <div class="body">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${runInstance}">
            <div class="errors">
                <g:renderErrors bean="${runInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" >
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
