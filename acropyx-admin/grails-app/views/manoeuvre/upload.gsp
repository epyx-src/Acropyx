
<%@ page import="ch.acropyx.Manoeuvre" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'manoeuvre.label', default: 'Manoeuvre')}" />
        <title><g:message code="data.import.csv.title" args="[entityName]" /></title>
    </head>
    <body>
		<g:render template="/layouts/navUpload" collection="['Manoeuvre']" var="entityName" />
        <div class="body">
            <h1><g:message code="data.import.csv.title" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            
            <g:form action="upload" method="post" enctype="multipart/form-data">
                <input type="file" name="userFile" />
                <input type="submit" value="Upload" />
            </g:form>
        </div>
    </body>
</html>
