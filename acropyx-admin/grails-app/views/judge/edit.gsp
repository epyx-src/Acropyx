

<%@ page import="ch.acropyx.Judge" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'judge.label', default: 'Judge')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
		<g:render template="/layouts/navEdit" collection="['Judge']" var="entityName" />
        <div class="body">
            <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${judgeInstance}">
            <div class="errors">
                <g:renderErrors bean="${judgeInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${judgeInstance?.id}" />
                <g:hiddenField name="version" value="${judgeInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="name"><g:message code="judge.name.label" default="Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: judgeInstance, field: 'name', 'errors')}">
                                    <g:textField name="name" value="${judgeInstance?.name}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="username"><g:message code="judge.username.label" default="Username" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: judgeInstance, field: 'username', 'errors')}">
                                    <g:textField name="username" value="${judgeInstance?.username}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="role"><g:message code="judge.role.label" default="Role" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: judgeInstance, field: 'role', 'errors')}">
                                    <g:select name="role" from="${ch.acropyx.Judge$Role?.values()}" keys="${ch.acropyx.Judge$Role?.values()*.name()}" value="${judgeInstance?.role?.name()}"  />
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
