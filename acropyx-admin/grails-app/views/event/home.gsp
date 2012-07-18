<%@ page import="org.codehaus.groovy.grails.commons.ConfigurationHolder" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main_event" />
        <title>Home</title>
		<link rel="stylesheet" href="${resource(dir:'css/ui-lightness',file:'jquery-ui-1.8.16.custom.css')}" />
		<g:javascript library="jquery-1.6.2.min"/>
		<g:javascript library="jquery-ui-1.8.16.custom.min"/>
        <g:javascript src="combobox.js"/>
        
    	<script> 
    		function openDisplayer() { 
    		    window.open('http://' + document.domain + ':' + ${ConfigurationHolder.config.ch.acropyx.displayerPort}); 
    		} 
        </script>
        
        <script>
            $(document).ready(function() {
                comboboxErrorText = "Test1";
                $("#selectCompetitor").combobox();
                comboboxErrorText = "Test2";                
                $("#selectFlight").combobox();

                // Fix combobox size
                $(".ui-button-icon-only").css("width", "1.9em");
                $(".ui-button-text").css("padding", ".1em");
                
                //Select Default 
            });
        </script>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
			<span class="menuButton"><a class="admin" href="${createLink(uri: '/admin')}"><g:message code="default.admin.label"/></a></span>
			<sec:ifNotLoggedIn>
	            <span class="menuLogin"><a class="login" href="${createLink(uri: '/login')}"><g:message code="default.login.label"/></a></span>
			</sec:ifNotLoggedIn>
			<sec:ifLoggedIn>
				<span class="menuLogin"><sec:username />(<g:link controller="logout"><g:message code="default.logout.label"/></g:link>)</span>
			</sec:ifLoggedIn>
        </div>
        <div class="body">
                <h1>Active competitions</h1>
	        <g:if test="${flash.competitionMessage}">
                <div class="errors">${flash.competitionMessage}</div>
            </g:if>
            <g:hasErrors bean="${competitionInstance}">
            <div class="errors">
                <g:renderErrors bean="${competitionInstance}" as="list" />
            </div> 
            </g:hasErrors>
	        <div class="list">
	            <table>
	                <thead>
	                    <tr>
                            <th><g:message code="competition.id.label" default="Id" /></th>
                        
                            <th><g:message code="competition.code.label" default="Code" /></th>
                        
                            <th><g:message code="competition.type.label" default="Type" /></th>
                        
                            <th><g:message code="competition.startTime.label" default="Start Time" /></th>
                            
                            <th>Actions</th>
                        
                            <th><g:message code="competition.endTime.label" default="End Time" /></th>
	                    </tr>
	                </thead>
	                <tbody>
	                <g:each in="${competitionInstanceList}" status="i" var="competitionInstance">
	                    <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
	                    
	                        <td><g:link controller="competition" action="show" id="${competitionInstance.id}">${fieldValue(bean: competitionInstance, field: "id")}</g:link></td>
	                    
	                        <td>${fieldValue(bean: competitionInstance, field: "code")}</td>
	                        
	                        <td>${fieldValue(bean: competitionInstance, field: "type")}</td>
	                    
	                        <td><g:formatDate format="${grailsApplication.config.ch.acropyx.dateFormat}" date="${competitionInstance.startTime}" /></td>
	                    
	                        <td>
                                <g:form controller="resultCompetition" action="show" id="${competitionInstance.id}">
                                    <g:submitButton name="Results" />
                                </g:form>
                            </td> 
                            
                            <g:if test="${competitionInstance.endTime != null}">
	                            <td><g:formatDate format="${grailsApplication.config.ch.acropyx.dateFormat}" date="${competitionInstance.endTime}" /></td>
	                        </g:if>
	                        <g:else>
	                            <g:form action="endCompetition" id="${competitionInstance.id}">
                                    <td><g:submitButton name="End" /></td>
                                </g:form>    
	                        </g:else>
	                    </tr>
	                </g:each>
	                </tbody>
					<tfoot>
						<tr>
							<g:form action="startCompetition">
								<td><g:select name="id"
										from="${ch.acropyx.Competition.list()}" optionKey="id" value="${competition?.id}"
										noSelection="['': '-- Choose a competition --']" />
								</td>
								<td colspan="5"><g:submitButton name="Start a new competition" /></td>
							</g:form>
						</tr>
					</tfoot>
			    </table>
	        </div>
	        
	        <h1>Active run</h1>
	        <g:if test="${flash.runMessage}">
                <div class="errors">${flash.runMessage}</div>
            </g:if>
            <g:hasErrors bean="${runInstance}">
            <div class="errors">
                <g:renderErrors bean="${runInstance}" as="list" />
            </div> 
            </g:hasErrors>
	        <div class="list">
	            <table>
	                <thead>
	                    <tr>
	                        <th><g:message code="run.id.label" default="Id" /></th>
	                    
	                        <th><g:message code="run.competition.label" default="Competition" /></th>
	                    
	                        <th><g:message code="run.name.label" default="Name" /></th>
	                    
	                        <th><g:message code="run.startTime.label" default="Start Time" /></th>
                            
                            <th>Actions</th>
	                    
	                        <th><g:message code="run.endTime.label" default="End Time" /></th>
	                    </tr>
	                </thead>
	                <tbody>
	                <g:each in="${runInstanceList}" status="i" var="runInstance">
	                    <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
	                    
	                        <td><g:link controller="run" action="show" id="${runInstance.id}">${fieldValue(bean: runInstance, field: "id")}</g:link></td>
	                    
	                        <td>${fieldValue(bean: runInstance, field: "competition")}</td>
	                    
	                        <td>${fieldValue(bean: runInstance, field: "name")}</td>
	                    
	                        <td><g:formatDate format="${grailsApplication.config.ch.acropyx.dateFormat}" date="${runInstance.startTime}" /></td>
	                    
                            <td>
                                <g:link controller="resultRun" action="show" id="${runInstance.id}" params="[notEndedFlights:true]">
                                    <g:submitButton name="Results" />
                                </g:link>
                            </td>
                            
                            <g:if test="${runInstance.endTime != null}">
                                <td><g:formatDate date="${runInstance.endTime}" /></td>
                            </g:if>
                            <g:else>
                                <g:form action="endRun" id="${runInstance.id}">
                                    <td><g:submitButton name="End" /></td>
                                </g:form>    
                            </g:else> 
	                    </tr>
	                </g:each>
	                </tbody>
	                <tfoot>
                        <tr>
                            <g:form action="startRun">
                                <% def noSelectionText = ch.acropyx.Competition.countByStartTimeIsNotNullAndEndTimeIsNull() == 0 ? ['': '-- Please start a competition --'] : ['': '-- Choose a run --'] %>
                                <td><g:select name="id"
                                        from="${ch.acropyx.Run.list().findAll { it.competition.isActive() }}" optionKey="id" value="${run?.id}"
                                        noSelection="${noSelectionText}" />
                                </td>
                                <td colspan="5"><g:submitButton name="Start a new run" /></td>
                            </g:form>
                        </tr>
                    </tfoot>
	            </table>
	        </div>
	    
		    <h1>Active flight</h1>
		    <g:if test="${flash.flightMessage}">
                <div class="errors">${flash.flightMessage}</div>
            </g:if>
            <g:hasErrors bean="${newFlight}">
            <div class="errors">
                <g:renderErrors bean="${newFlight}" as="list" />
            </div> 
            </g:hasErrors>
	        <div class="list">
	           <table>
	               <thead>
	                   <tr>
	                       <th><g:message code="flight.id.label" default="Id" /></th>
	                   
	                       <th><g:message code="flight.competitor.label" default="Competitor" /></th>
	                   
	                       <th><g:message code="flight.run.label" default="Run" /></th>
	                   
	                       <th><g:message code="flight.startTime.label" default="Start Time" /></th>
                           
                           <th>Actions</th>
	                   
	                       <th><g:message code="flight.endTime.label" default="End Time" /></th>
	                   </tr>
	               </thead>
	               <tbody>
	               <g:each in="${flightInstanceList}" status="i" var="flightInstance">
	                   <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
	                   
	                       <td><g:link controller="flight" action="show" id="${flightInstance.id}">${fieldValue(bean: flightInstance, field: "id")}</g:link></td>
                           
                            <g:if test="${flightInstance.competitor instanceof ch.acropyx.Team}">
                                <td><g:link controller="team" action="show" id="${flightInstance.competitor.id}">${flightInstance.competitor.name}</g:link></td>
                            </g:if>
                            <g:else>
                                <td><g:link controller="pilot" action="show" id="${flightInstance.competitor.id}">${flightInstance.competitor.name}</g:link></td>
                            </g:else>
	                   
	                       <td>${fieldValue(bean: flightInstance, field: "run")}</td>
	                   
	                       <td><g:formatDate format="${grailsApplication.config.ch.acropyx.dateFormat}" date="${flightInstance.startTime}" /></td>
	                   
                            <td>
                                <g:form  action="displayFlight" id="${flightInstance.id}">
                                    <g:submitButton name="Display" />
                                </g:form>
                                <g:form controller="flightManoeuvre" action="edit" id="${flightInstance.id}">
                                    <g:submitButton name="Maneuvers" />
                                </g:form>
                                <g:form controller="vote" action="edit" id="${flightInstance.id}">
                                    <g:submitButton name="Vote" />
                                </g:form>
                                <g:form action="addWarning" id="${flightInstance.id}">
                                    <g:submitButton name="Add warning" />
                                </g:form>                                
                            </td>
                       
                            <g:if test="${flightInstance.endTime != null}">
                                <td><g:formatDate date="${flightInstance.endTime}" /></td>
                            </g:if>
                            <g:else>
                                <td>
	                                <g:form action="endFlight" id="${flightInstance.id}">
	                                    <g:submitButton name="End" />
	                                </g:form>
                                    <g:form action="deleteFlight" id="${flightInstance.id}">
                                        <g:submitButton name="Delete" onclick="return confirm('Delete the flight with its maneuvers and marks ?')" />
                                    </g:form>  	                                
                                </td>      
                            </g:else>                          
	                   </tr>
	               </g:each>
	               </tbody>
	               <tfoot>
                        <tr>
                            <g:form action="startFlight">
                                <td>
                                    <% def noSelectionText = ch.acropyx.Run.countByStartTimeIsNotNullAndEndTimeIsNull() == 0 ? ['': '-- Please start a run --'] : ['': ''] %>
								    <g:select name="selectCompetitor"
								              from="${ch.acropyx.Competitor.competitorsForActiveRun()}" optionKey="id" value="${competitor?.id}"
								              noSelection="${noSelectionText}" />
                                </td>
                                <td colspan="5"><g:submitButton name="New flight" /></td>
                            </g:form>
                        </tr>
                    </tfoot>
	            </table>
	        </div>

            <h1>Control the displayer (<a style="font-weight:normal" href="#" onclick="openDisplayer();return false;">open the diplayer</a>)</h1>
            <table>
                <tr>
                    <td>
                        <p style="color:#666; font-weight:bold">Send message !</p>
                        <g:if test="${flash.sendMessage}">
                        <div class="errors">
                            ${flash.sendMessage}
                        </div>
                        </g:if>
                        <g:form action="sendMessage">
                        <div>
                            <input type="text" name="sendMessageText" size="25" /> Sticky <g:checkBox name="sticky" />
                        </div>
                        <g:submitButton name="Send" />
                        <g:actionSubmit action="clearMessage" value="Clear" />
                        </g:form>
                      </td>
                  </tr>
                  <tr>
                      <td>  
                        <p style="color:#666; font-weight:bold">Display results</p>
                        <g:if test="${flash.displayResults}">
                            <div class="errors">
                                ${flash.displayResults}
                            </div>
                        </g:if>
                        <div>
                            <g:form action="sendCompetitionResultToDisplay">
                                <g:select name="id"
                                        from="${ch.acropyx.Competition.findAllByStartTimeIsNotNull()}"
                                        optionKey="id" value="${activeCompetition?.id}"
                                        noSelection="['': '-- Choose a competition --']" />
                                <g:submitButton name="Send competition result to display" />
                            </g:form>
                        </div>                        
                        <div>
                            <g:form action="sendRunResultToDisplay">
                                <g:select name="id"
                                        from="${ch.acropyx.Run.findAllByStartTimeIsNotNull()}"
                                        optionKey="id" value="${activeRun?.id}"
                                        noSelection="['': '-- Choose a run --']" />
                                <g:submitButton name="Send run result to display" />
                            </g:form>
                        </div>
                        <div>
                            <g:form action="sendFlightToDisplay">
                                <g:select name="id"
                                        from="${ch.acropyx.Flight.findAllByStartTimeIsNotNull()}"
                                        optionKey="id" value="${activeFlight?.id}" noSelection="['': '-- Choose a flight --']" 
                                        
                                        />
                                <g:submitButton name="Send flight to display" />
                            </g:form>
                        </div>
                    </td>
                </tr>
            </table>        
            <br />
        </div>
    </body>
</html>
