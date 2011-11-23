

<%@ page import="ch.acropyx.Competition" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'competition.label', default: 'Competition')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
		<g:render template="/layouts/navCreate" collection="['Competition']" var="entityName" />
        <div class="body">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${competitionInstance}">
            <div class="errors">
                <g:renderErrors bean="${competitionInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" >
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
