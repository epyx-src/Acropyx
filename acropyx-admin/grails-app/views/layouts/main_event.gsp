<!DOCTYPE html>
<html>
    <head>
        <title><g:layoutTitle default="Acropyx"/></title>
        <link rel="stylesheet" href="${resource(dir:'css',file:'main.css')}" />
        <link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
        <g:layoutHead />
        <g:javascript library="application" />
    </head>
    <body>
        <div id="spinner" class="spinner" style="display:none;">
            <img src="${resource(dir:'images',file:'spinner.gif')}" alt="${message(code:'spinner.alt',default:'Loading...')}" />
        </div>
        <div id="header">
			<img style="position:absolute;left:5px;top:5px;padding:5px;height:50px" src="${resource(dir:'images',file:'swiss-acro-league.png')}"></img>
			<img style="position:absolute;right:5px;top:5px;padding:5px;height:50px;" src="${resource(dir:'images',file:'epyx_logo_100.png')}"></img>
        </div>
        <div id="container">
	        <g:layoutBody />
        </div>
    </body>
</html>
