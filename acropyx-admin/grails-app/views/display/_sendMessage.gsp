<div id="resultArea">
	<g:if test="${competitor}">
		<!--  Result area -->
            <div class="list">
                <table>
                    <thead>
                        <tr>
                            <g:sortableColumn property="name" title="Competitor name" />
                            <g:sortableColumn property="mark" title="Mark" />
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${flightList}" status="i" var="flight">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                            <td>${fieldValue(bean: flight, field: "competitor.name")}</td>
                            <td>${fieldValue(bean: flight, field: "mark")}</td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>

        <br/>

		<!--  Send message/note area -->
		<div id="textArea">
			<g:form action="sendTextMessage">
	            <div class="dialog">
	                <table>
	                    <tbody>
	                        <tr class="prop">
	                            <td valign="top" class="label">
	                                <label for="textMessage">Text message</label>
	                            </td>
	                            <td valign="top" class="value">
	                                <g:textField name="textMessage" value="${textMessage}"/>
	                            </td>
	                        </tr>
	                 	</tbody>
					</table>
				</div>
	            <div class="buttons">
	            	<span class="button">
			      		<g:submitToRemote update="dummy" value="Send text message" url="[action:'sendTextMessage']"
			          				onLoading="showSpinner(true)"  onComplete="showSpinner(false)" >
			      		</g:submitToRemote>
	            	</span>
	            </div>
            </g:form>
		</div>

		<div id="markArea">
			<g:form action="sendMarkMessage">
	            <div class="dialog">
	                <table>
	                    <tbody>
	                        <tr class="prop">
	                            <td valign="top" class="label">
	                                <label for="mark">Mark</label>
	                            </td>
	                            <td valign="top" class="value">
	                                <g:textField name="mark" value="${mark}"/>
	                            </td>
	                        </tr>
	                 	</tbody>
					</table>
				</div>
	            <div class="buttons">
	            	<span class="button">
			      		<g:submitToRemote update="dummy" value="Send mark" url="[action:'sendMarkMessage']"
			          				onLoading="showSpinner(true)"  onComplete="showSpinner(false)" >
			      		</g:submitToRemote>
	            	</span>
	            </div>
            </g:form>
		</div>

	</g:if>

    <br/>

	<!--  Reset button -->
	<div id="dummy">
	</div>

</div>
