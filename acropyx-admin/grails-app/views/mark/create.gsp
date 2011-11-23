

<%@ page import="ch.acropyx.Mark" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'mark.label', default: 'Mark')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
		<g:render template="/layouts/navCreate" collection="['Mark']" var="entityName" />
        <div class="body">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${markInstance}">
            <div class="errors">
                <g:renderErrors bean="${markInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="flight"><g:message code="mark.flight.label" default="Flight" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: markInstance, field: 'flight', 'errors')}">
                                    <g:select name="flight.id" from="${ch.acropyx.Flight.list()}" optionKey="id" value="${markInstance?.flight?.id}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="judge"><g:message code="mark.judge.label" default="Judge" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: markInstance, field: 'judge', 'errors')}">
                                    <g:select name="judge.id" from="${ch.acropyx.Judge.list()}" optionKey="id" value="${markInstance?.judge?.id}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="markDefinition"><g:message code="mark.markDefinition.label" default="Mark Definition" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: markInstance, field: 'markDefinition', 'errors')}">
                                    <g:select name="markDefinition.id" from="${ch.acropyx.MarkDefinition.list()}" optionKey="id" value="${markInstance?.markDefinition?.id}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="mark"><g:message code="mark.mark.label" default="Mark" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: markInstance, field: 'mark', 'errors')}">
                                    <g:select name="mark" from="${1..10}" value="${fieldValue(bean: markInstance, field: 'mark')}"  />
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
