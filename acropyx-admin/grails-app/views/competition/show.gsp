
<%@ page import="ch.acropyx.Competition" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'competition.label', default: 'Competition')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="list" controller="resultCompetition" action="show" id="${competitionInstance.id}">Results</g:link></span>
            <sec:ifNotLoggedIn>
                   <span class="menuLogin"><a class="login" href="${createLink(uri: '/login')}"><g:message code="default.login.label"/></a></span>
            </sec:ifNotLoggedIn>
            <sec:ifLoggedIn>
                <span class="menuLogin"><sec:username /> (<g:link controller="logout"><g:message code="default.logout.label"/></g:link>)</span>
            </sec:ifLoggedIn>
        </div>
        <div class="body">
            <h1><g:message code="default.show.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="dialog">
                <table>
                    <tbody>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="competition.id.label" default="Id" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: competitionInstance, field: "id")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="competition.code.label" default="Code" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: competitionInstance, field: "code")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="competition.name.label" default="Name" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: competitionInstance, field: "name")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="competition.type.label" default="Type" /></td>
                            
                            <td valign="top" class="value">${competitionInstance?.type?.encodeAsHTML()}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="competition.markCoefficients.label" default="Mark Coefficients" /></td>
                            
                            <td valign="top" style="text-align: left;" class="value">
                                <ul>
                                <g:each in="${competitionInstance.markCoefficients}" var="m">
                                    <li><g:link controller="markCoefficient" action="show" id="${m.id}">${m?.encodeAsHTML()}</g:link></li>
                                </g:each>
                                </ul>
                            </td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="competition.judges.label" default="Judges" /></td>
                            
                            <td valign="top" style="text-align: left;" class="value">
                                <ul>
                                <g:each in="${competitionInstance.judges}" var="j">
                                    <li><g:link controller="judge" action="show" id="${j.id}">${j?.encodeAsHTML()}</g:link></li>
                                </g:each>
                                </ul>
                            </td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="competition.startTime.label" default="Start Time" /></td>
                            
                            <td valign="top" class="value"><g:formatDate format="${grailsApplication.config.ch.acropyx.dateFormat}" date="${competitionInstance?.startTime}" /></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="competition.endTime.label" default="End Time" /></td>
                            
                            <td valign="top" class="value"><g:formatDate format="${grailsApplication.config.ch.acropyx.dateFormat}" date="${competitionInstance?.endTime}" /></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="competition.runs.label" default="Runs" /></td>
                            
                            <td valign="top" style="text-align: left;" class="value">
                                <ul>
                                <g:each in="${competitionInstance.runs}" var="r">
                                    <li><g:link controller="run" action="show" id="${r.id}">${r?.encodeAsHTML()}</g:link></li>
                                </g:each>
                                </ul>
                            </td>
                            
                        </tr>
                    
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <g:hiddenField name="id" value="${competitionInstance?.id}" />
                    <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </g:form>
            </div>
        </div>
    </body>
</html>
