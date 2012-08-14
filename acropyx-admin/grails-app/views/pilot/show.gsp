
<%@ page import="ch.acropyx.Pilot" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'pilot.label', default: 'Pilot')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
		<g:render template="/layouts/navShow" collection="['Pilot']" var="entityName" />
        <div class="body">
            <h1><g:message code="default.show.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="dialog">
                <table>
                    <tbody>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="pilot.id.label" default="Id" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: pilotInstance, field: "id")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="pilot.name.label" default="Name" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: pilotInstance, field: "name")}</td>
                            
                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="pilot.civlRank.label" default="Civl Rank" /></td>

                            <td valign="top" class="value">${fieldValue(bean: pilotInstance, field: "civlRank")}</td>

                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="pilot.civlId.label" default="Civl Id" /></td>

                            <td valign="top" class="value">${fieldValue(bean: pilotInstance, field: "civlId")}</td>

                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="pilot.country.label" default="Country" /></td>
                            
                            <td valign="top" class="value"><g:country code="${pilotInstance.country}" /></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="pilot.age.label" default="Age" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: pilotInstance, field: "age")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="pilot.flyingSinceYear.label" default="Flying Since Year" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: pilotInstance, field: "flyingSinceYear")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="pilot.job.label" default="Job" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: pilotInstance, field: "job")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="pilot.selection.label" default="Selection" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: pilotInstance, field: "selection")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="pilot.glider.label" default="Glider" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: pilotInstance, field: "glider")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="pilot.sponsor.label" default="Sponsor" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: pilotInstance, field: "sponsor")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="pilot.bestResult.label" default="Best Result" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: pilotInstance, field: "bestResult")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="pilot.picture.label" default="Picture" /></td>
                            <td valign="top" class="value">
                                <g:if test="${pilotInstance.picture?.length > 0}">
                                    <img src="${createLink(action:'displayPicture', id:pilotInstance?.id)}" />
                                </g:if>
                            </td>
                        </tr>
                    
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <g:hiddenField name="id" value="${pilotInstance?.id}" />
                    <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </g:form>
            </div>
        </div>
    </body>
</html>
