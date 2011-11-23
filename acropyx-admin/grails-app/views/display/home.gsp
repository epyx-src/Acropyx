<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
		<g:javascript library="prototype"/>
        <title>Display</title>
		<g:javascript>
		function updateCompetitor(e) {
			// The response comes back as a bunch-o-JSON 
			var competitors = eval("(" + e.responseText + ")") // evaluate JSON
		
			if (competitors) {
				var rselect = document.getElementById('competitor.id')
		
				// Clear all previous options
				var l = rselect.length
		
				while (l > 0) {
					l--
					rselect.remove(l)
				}
		
				// Rebuild the select
				for (var i=0; i < competitors.length; i++) {
					var competitor = competitors[i]
					var opt = document.createElement('option');
					opt.text = competitor.name
					opt.value = competitor.id
					try {
						rselect.add(opt, null) // standards compliant; doesn't work in IE
					}
					catch(ex) {
						rselect.add(opt) // IE only
					}
				}
			}
		}
		
		// This is called when the page loads to initialize city
		var zselect = document.getElementById('competitorType')
		var zopt = zselect.options[zselect.selectedIndex]
		${remoteFunction(controller:"display", action:"ajaxGetCompetitorList", params:"'type=' + zopt.value", onComplete:"updateCompetitor(e)")}
		</g:javascript>

    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <sec:ifAllGranted roles="ROLE_ADMIN">
				<span class="menuButton"><a class="admin" href="${createLink(uri: '/admin')}"><g:message code="default.admin.label"/></a></span>
            </sec:ifAllGranted>
			<sec:ifNotLoggedIn>
	            <span class="menuLogin"><a class="login" href="${createLink(uri: '/login')}"><g:message code="default.login.label"/></a></span>
			</sec:ifNotLoggedIn>
			<sec:ifLoggedIn>
				<span class="menuLogin"><sec:username /> (<g:link controller="logout"><g:message code="default.logout.label"/></g:link>)</span>
			</sec:ifLoggedIn>
        </div>

        <div class="body">
	        <h1>Current competition</h1>
            <g:if test="${flash.message}">
            	<div class="message">${flash.message}</div>
            </g:if>

            <g:form action="selectCurrentCompetition" >
                <div class="dialog">
                    <table>
                        <tbody>
                            <tr class="prop">
                                <td valign="top" class="label">
                                    <label for="competitionName">Name</label>
                                </td>
                                <td valign="top" class="value">
                                    <g:textField name="competitionName" value="${command?.competitionName}"/>
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="label">
                                    <label for="competitorType">Type</label>
                                </td>
                                <td valign="top" class="value">
									<g:select name="competitorType" from="['Pilot','Team']" value="${command?.competitorType}"
										      onchange="${remoteFunction(
												            controller:'display', 
												            action:'ajaxGetCompetitorList', 
												            params:'\'type=\' + escape(this.value)', 
												            onComplete:'updateCompetitor(e)')}">
									</g:select>
                                </td>
                            </tr>
	                    </tbody>
					</table>
				</div>

                <div class="buttons">
                	<span class="button">
	              		<g:submitToRemote update="selectCompetitor" value="Select current competition" url="[action:'selectCurrentCompetition']"
                   				onLoading="showSpinner(true)"  onComplete="showSpinner(false)" >
	              		</g:submitToRemote>
                	</span>
                </div>
			</g:form>

	        <br/>
          	<g:render template="selectCompetitor"/>

	        <br/>
			<g:render template="sendMessage"/>

	    </div>          
    </body>
</html>
