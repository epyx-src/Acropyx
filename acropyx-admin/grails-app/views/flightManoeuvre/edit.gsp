<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        
		<link rel="stylesheet" href="${resource(dir:'css/ui-lightness',file:'jquery-ui-1.8.16.custom.css')}" />
		<g:javascript library="jquery-1.6.2.min"/>
		<g:javascript library="jquery-ui-1.8.16.custom.min"/>
        
        <style>
            table {
                border: 0px;
            }
		    #manoeuvres {
		        list-style-type: none;
		        margin: 0;
		        padding: 0;
		        float: left;
		        margin-right: 10px;
		    }
		    #manoeuvres li {
		        margin: 4px 4px 4px 4px;
		        padding: 2px;
		        float: left;
		        width: 100px;
		        height: 30px;
		        text-align: center;
		    }
		    #flight {
		        list-style-type: none;
		        margin: 0;
		        padding: 0;
		        float: left;
		        margin-right: 10px;
		    }
            #flight li {
                margin: 4px 4px 4px 4px;
                padding: 2px;
                float: left;
                width: 100px;
                height: 30px;
                text-align: center;
            }
            #trash { 
                position: relative;
                width: 64px;
                height: 75px;
                left: 20px; 
                top: 20px;
			    overflow: hidden; 
			    background: url('../../images/trash-closed-64x75.png');
			    background-repeat:no-repeat;
                background-position:left top;
			}
			#trash li { 
			    height: 0; 
			    width: 0; 
			    overflow: hidden; 
			}
		    </style>
		    
		    <script>
		    $(document).ready(function() {
		        $( '.manoeuvre' ).draggable({
		            connectToSortable: "#flight",
		            helper: "clone",
		        }).disableSelection();
		        $( '#flight' ).sortable({
                    connectWith: '#trash',
                    dropOnEmpty: true,
                }).disableSelection();
                $( '#trash' ).sortable({
                    tolerance: 'pointer',
                    cursor: 'pointer',
                    receive: function(event, ui) {
                        // This is the trash, remove all received items
                        ui.item.remove()       
                    },
                    over: function(event, ui) {
                        this.style.backgroundImage = "url('../../images/trash-open-64x75.png')";    
                    },
                    out: function(event, ui) {
                        this.style.backgroundImage = "url('../../images/trash-closed-64x75.png')";    
                    },
                }).disableSelection();
                var manoeuvreClick = function(event) {
                    var self = event.currentTarget;
                    if (self.parentNode.id == "manoeuvres") {
                        var newItem = $(self).clone(false);
                        $('#flight').append(newItem);
                        // Register event when the item is in the 'flight' list
                        // Disabled because it interfers with the sortable drag and drop
                        //newItem.click(manoeuvreClick);
                    }
                    /*
                    else if (self.parentElement.id == "flight") {
                    	self.parentElement.removeChild(self);
                    }
                    */
                }
                $( '.manoeuvre' ).click(manoeuvreClick);
		    });
		</script>
        
        <meta name="layout" content="main_event" />
        <title>Home</title>
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
            <table>
                <tr>
                    <td>
		                <h1>Manoeuvres list</h1>
						<ul id="manoeuvres" style="width: 800px; padding:5px; border: 1px solid #ccc;">
						    <g:each in="${manoeuvres}" status="i" var="manoeuvre">
							    <li class="manoeuvre ui-state-highlight">
							    <input type="hidden" name="flightManoeuvreIds" value="${manoeuvre.id}" />${manoeuvre}</li>
						    </g:each>
						</ul>
					</td>
				</tr>
				<tr>
				    <td>
				        <g:form action="save" id="${flightInstance.id}">
					    <table>
						    <tr>
								<td>
									<h1>${flightInstance}</h1>
									<ul id="flight" style="width:112px; min-height: 100px; padding:5px; border: 1px solid #ccc;">
										<g:each in="${flightManoeuvres}" status="i" var="manoeuvre">
	                                        <li class="sortable ui-state-highlight">
	                                        <input type="hidden" name="flightManoeuvreIds" value="${manoeuvre.id}" />${manoeuvre}</li>
	                                    </g:each>
									</ul>
									<ul id="trash"></ul>
								</td>
							</tr>
					    </table>
                        <g:if test="${goHome}">
                            <input type="hidden" id="goHome" name="goHome" value="${goHome}"/>
                        </g:if>
					    <g:submitButton name="Save" />
                        </g:form>  				    
				    </td>	    
                </tr> 
		    </table>
        </div>
    </body>
</html>