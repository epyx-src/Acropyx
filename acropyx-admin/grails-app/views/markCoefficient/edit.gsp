

<%@ page import="ch.acropyx.MarkCoefficient" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'markCoefficient.label', default: 'MarkCoefficient')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${markCoefficientInstance}">
            <div class="errors">
                <g:renderErrors bean="${markCoefficientInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${markCoefficientInstance?.id}" />
                <g:hiddenField name="version" value="${markCoefficientInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="markDefinition"><g:message code="markCoefficient.markDefinition.label" default="Mark Definition" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: markCoefficientInstance, field: 'markDefinition', 'errors')}">
                                    <g:select name="markDefinition.id" from="${ch.acropyx.MarkDefinition.list()}" optionKey="id" value="${markCoefficientInstance?.markDefinition?.id}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="competition"><g:message code="markCoefficient.competition.label" default="Competition" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: markCoefficientInstance, field: 'competition', 'errors')}">
                                    <g:select name="competition.id" from="${ch.acropyx.Competition.list()}" optionKey="id" value="${markCoefficientInstance?.competition?.id}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="coefficient"><g:message code="markCoefficient.coefficient.label" default="Coefficient" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: markCoefficientInstance, field: 'coefficient', 'errors')}">
                                    <g:textField name="coefficient" value="${fieldValue(bean: markCoefficientInstance, field: 'coefficient')}" />
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
