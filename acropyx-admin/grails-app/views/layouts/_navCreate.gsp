<div class="nav">
	<span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
    <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
	<sec:ifNotLoggedIn>
           <span class="menuLogin"><a class="login" href="${createLink(uri: '/login')}"><g:message code="default.login.label"/></a></span>
	</sec:ifNotLoggedIn>
	<sec:ifLoggedIn>
		<span class="menuLogin"><sec:username /> (<g:link controller="logout"><g:message code="default.logout.label"/></g:link>)</span>
	</sec:ifLoggedIn>
</div>
