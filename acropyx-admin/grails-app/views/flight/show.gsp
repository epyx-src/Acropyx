
<%@ page import="ch.acropyx.Flight; ch.acropyx.MarkCoefficient" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'flight.label', default: 'Flight')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
		<g:render template="/layouts/navShow" collection="['Flight']" var="entityName" />
        <div class="body">
            <h1><g:message code="default.show.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="dialog">
                <table>
                    <tbody>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="flight.id.label" default="Id" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: flightInstance, field: "id")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="flight.competitor.label" default="Competitor" /></td>
                            
                            <td valign="top" class="value"><g:link controller="${flightInstance?.competitor instanceof ch.acropyx.Pilot ? 'pilot' : 'team'}" action="show" id="${flightInstance?.competitor?.id}">${flightInstance?.competitor?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="flight.run.label" default="Run" /></td>
                            
                            <td valign="top" class="value"><g:link controller="run" action="show" id="${flightInstance?.run?.id}">${flightInstance?.run?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="flight.startTime.label" default="Start Time" /></td>
                            
                            <td valign="top" class="value"><g:formatDate format="${grailsApplication.config.ch.acropyx.dateFormat}" date="${flightInstance?.startTime}" /></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="flight.endTime.label" default="End Time" /></td>
                            
                            <td valign="top" class="value"><g:formatDate format="${grailsApplication.config.ch.acropyx.dateFormat}" date="${flightInstance?.endTime}" /></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="flight.manoeuvres.label" default="Manoeuvres" /></td>
                            
                            <td valign="top" style="text-align: left;" class="value">
                                <ul>
                                <g:each in="${flightInstance.manoeuvres}" var="m">
                                    <li><g:link controller="manoeuvre" action="show" id="${m.id}">${m?.encodeAsHTML()}</g:link></li>
                                </g:each>
                                </ul>
                            </td>
                            
                        </tr>
                        
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="flight.warnings.label" default="Warnings" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: flightInstance, field: "warnings")}</td>
                            
                        </tr>                        
                        
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="flight.result.label" default="Result" /></td>
                            
                            <td valign="top" style="text-align: left;" class="value">
                                <% def detailedResults = flightInstance.computeDetailedResults() %>
                                <g:formatNumber number="${flightInstance.computeResult(detailedResults)}" format="#.#" roundingMode="HALF_UP" />
                                {<g:each in="${detailedResults}" var="detailResult">
                                    ${MarkCoefficient.get(detailResult.key).markDefinition}:<g:formatNumber number="${detailResult.value}" format="#.#" roundingMode="HALF_UP" />,
                                </g:each>}
                            </td>
                            
                        </tr>
                    
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <g:hiddenField name="id" value="${flightInstance?.id}" />
                    <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </g:form>
            </div>
        </div>
    </body>
</html>
