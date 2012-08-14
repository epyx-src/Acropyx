

<%@ page import="ch.acropyx.Pilot" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'pilot.label', default: 'Pilot')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
		<g:render template="/layouts/navEdit" collection="['Pilot']" var="entityName" />
        <div class="body">
            <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${pilotInstance}">
            <div class="errors">
                <g:renderErrors bean="${pilotInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post">
                <g:hiddenField name="id" value="${pilotInstance?.id}" />
                <g:hiddenField name="version" value="${pilotInstance?.version}" />
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
                                    <g:select name="flyingSinceYear" from="${1980..2012}" value="${pilotInstance.flyingSinceYear}" noSelection="['': '']" />
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
                                    <label for="civlId"><g:message code="pilot.civlId.label" default="Civl Id" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: pilotInstance, field: 'civlId', 'errors')}">
                                    <g:textField name="civlId" value="${pilotInstance?.civlId}" />
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
                                    <label for="civlRank"><g:message code="pilot.civlRank.label" default="CIVL Ranking" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: pilotInstance, field: 'civlRank', 'errors')}">
                                    <g:textField name="civlRank" value="${pilotInstance?.civlRank}" />
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
                                    <g:if test="${pilotInstance?.picture?.length > 0}">
                                        <img src="${createLink(action:'displayPicture', id:pilotInstance?.id)}" />
                                    </g:if>
                                    <span class="button"><g:link class="save" action="upload_picture" id="${pilotInstance?.id}">Upload picture</g:link></span>
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
