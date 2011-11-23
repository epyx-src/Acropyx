<%@ page import="ch.acropyx.Pilot" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title>Upload picture</title>
    </head>
    <body>
		<g:render template="/layouts/navUpload" collection="['Pilot']" var="entityName" />
        <div class="body">
            <h1>Upload picture</h1>
            
            <g:if test="${flash.message}">
                <div class="message">${flash.message}</div>
            </g:if>
            
            <g:form action="upload_picture" method="post" enctype="multipart/form-data">
                <g:hiddenField name="pilot.id" value="${params.id}" />
                <input type="file" name="userFile" />
                <input type="submit" value="Upload picture" />
            </g:form>
        </div>
    </body>
</html>
