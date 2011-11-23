<div id="selectCompetitor">
	<g:if test="${competitionName}">
		<g:form action="selectCompetitor">
	        <div class="dialog">
	              <table>
	                   <tbody>
	                       <tr class="prop">
	                           <td valign="top" class="label">
	                               <label for="competitor">Competitor</label>
	                           </td>
	                           <td valign="top" class="value">
	                               <g:select name="competitor.id" from="${ch.acropyx.Competitor.list()}" optionKey="id" value="${command?.competitor?.id}" noSelection="['': '-- Choose a competitor --']"/>
	                           </td>
	                       </tr>
	                	</tbody>
					</table>
			</div>
	        <div class="buttons">
               	<span class="button">
		      		<g:submitToRemote update="resultArea" value="Set current flight" url="[action:'selectCompetitor']"
		          				onLoading="showSpinner(true)"  onComplete="showSpinner(false)" >
		      		</g:submitToRemote>
		      	</span>
	        </div>
		</g:form>
	</g:if>
</div>
