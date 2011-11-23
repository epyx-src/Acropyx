
<%@ page import="ch.acropyx.Mark" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'mark.label', default: 'Mark')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
		<g:render template="/layouts/navShow" collection="['Mark']" var="entityName" />
        <div class="body">
            <h1><g:message code="default.show.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="dialog">
                <table>
                    <tbody>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="mark.id.label" default="Id" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: markInstance, field: "id")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="mark.flight.label" default="Flight" /></td>
                            
                            <td valign="top" class="value"><g:link controller="flight" action="show" id="${markInstance?.flight?.id}">${markInstance?.flight?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="mark.judge.label" default="Judge" /></td>
                            
                            <td valign="top" class="value"><g:link controller="judge" action="show" id="${markInstance?.judge?.id}">${markInstance?.judge?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="mark.markDefinition.label" default="Mark Definition" /></td>
                            
                            <td valign="top" class="value"><g:link controller="markDefinition" action="show" id="${markInstance?.markDefinition?.id}">${markInstance?.markDefinition?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="mark.mark.label" default="Mark" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: markInstance, field: "mark")}</td>
                            
                        </tr>
                    
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <g:hiddenField name="id" value="${markInstance?.id}" />
                    <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </g:form>
            </div>
        </div>
    </body>
</html>
