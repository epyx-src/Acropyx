

<%@ page import="ch.acropyx.Pilot" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'pilot.label', default: 'Pilot')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
		<g:render template="/layouts/navCreate" collection="['Pilot']" var="entityName" />
        <div class="body">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${pilotInstance}">
            <div class="errors">
                <g:renderErrors bean="${pilotInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save"  enctype="multipart/form-data">
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="name"><g:message code="pilot.name.label" default="Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: pilotInstance, field: 'name', 'errors')}">
                                    <g:textField name="name" value="${pilotInstance?.name}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="country"><g:message code="pilot.country.label" default="Country" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: pilotInstance, field: 'country', 'errors')}">
                                    <g:countrySelect name="country" value="${pilotInstance?.country}" default="che" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="dateOfBirth"><g:message code="pilot.dateOfBirth.label" default="Date of birth (dd/MM/yyyy)" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: pilotInstance, field: 'dateOfBirth', 'errors')}">
                                    <g:textField name="dateOfBirth" value="${formatDate(format:'dd/MM/yyyy', date:pilotInstance?.dateOfBirth)}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="flyingSinceYear"><g:message code="pilot.flyingSinceYear.label" default="Flying Since Year" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: pilotInstance, field: 'flyingSinceYear', 'errors')}">
                                    <g:select name="flyingSinceYear" from="${1980..2012}" value="${fieldValue(bean: pilotInstance, field: 'flyingSinceYear')}" noSelection="['': '']" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="job"><g:message code="pilot.job.label" default="Job" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: pilotInstance, field: 'job', 'errors')}">
                                    <g:textField name="job" value="${pilotInstance?.job}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="selection"><g:message code="pilot.selection.label" default="Selection" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: pilotInstance, field: 'selection', 'errors')}">
                                    <g:textField name="selection" value="${pilotInstance?.selection}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="glider"><g:message code="pilot.glider.label" default="Glider" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: pilotInstance, field: 'glider', 'errors')}">
                                    <g:textField name="glider" value="${pilotInstance?.glider}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="sponsor"><g:message code="pilot.sponsor.label" default="Sponsor" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: pilotInstance, field: 'sponsor', 'errors')}">
                                    <g:textField name="sponsor" value="${pilotInstance?.sponsor}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="bestResult"><g:message code="pilot.bestResult.label" default="Best Result" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: pilotInstance, field: 'bestResult', 'errors')}">
                                    <g:textField name="bestResult" value="${pilotInstance?.bestResult}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="picture"><g:message code="pilot.picture.label" default="Picture" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: pilotInstance, field: 'picture', 'errors')}">
                                    <input type="file" id="picture" name="picture" />
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
