

<%@ page import="ch.acropyx.Manoeuvre" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'manoeuvre.label', default: 'Manoeuvre')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
		<g:render template="/layouts/navCreate" collection="['Manoeuvre']" var="entityName" />
        <div class="body">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${manoeuvreInstance}">
            <div class="errors">
                <g:renderErrors bean="${manoeuvreInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="name"><g:message code="manoeuvre.name.label" default="Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: manoeuvreInstance, field: 'name', 'errors')}">
                                    <g:textField name="name" value="${manoeuvreInstance?.name}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="coefficient"><g:message code="manoeuvre.coefficient.label" default="Coefficient" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: manoeuvreInstance, field: 'coefficient', 'errors')}">
                                    <g:textField name="coefficient" value="${fieldValue(bean: manoeuvreInstance, field: 'coefficient')}" />
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
