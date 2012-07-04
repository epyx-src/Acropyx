<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main_event" />
        <title>Home</title>
        <g:javascript library="jquery-1.6.2.min"/>
        <script type="text/javascript">
            window.onload = function() {
                $(".note")[0].focus();
            }
            /* Yo */
            function GetPosibleMarks()
            {
              
            }
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
            <h1>${flightInstance}</h1>
            <br />
            
            <g:form action="save" id="${flightInstance.id}">                  
            <div class="list">
                <table>
                    <thead>
                        <tr>
                            <th>Marks</th>
                            <g:each in="${judges}" var="judge">
	                            <th>${judge}</th>
	                        </g:each>    
                        </tr>
                    </thead>
                    <tbody>
                        <g:each in="${markCoefficients}" var="markCoefficient">
                            <tr>
                                <td>${markCoefficient}</td>
                                <g:each in="${judges}" var="judge">
	                                <td><g:select class="note" name="Mark_${judge.id}_${markCoefficient.markDefinition.id}" from="${posibleMarks}" value="${this.('Mark_' + judge.id + '_' + markCoefficient.markDefinition.id)}" /></td>
	                            </g:each>    
                            </tr>    
                        </g:each>  
                    </tbody>
                </table>
            </div>
            <div><g:submitButton name="Save" /></div>
            </g:form>
        </div>          
    </body>
</html>
