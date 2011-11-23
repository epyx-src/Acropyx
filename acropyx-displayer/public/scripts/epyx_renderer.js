/**
 * Handle a fixed canvas renderer, without animation
 */

var epyxRenderer = function() {
	var renderers = new Array();
	
	function log(str) {
		if ( window.console ) {
			window.console.log( str );
		}
		else if (document.getElementById("log_div")) {
			document.getElementById("log_div").innerHTML += str + "<br>";
		}
	}

	/**
	 * Draw of all canvas
	 */
	function paint(canvas,renderFunction) {
		var objectContent = getData(canvas);
		// resize the canvas
		canvas.width = canvas.clientWidth;
		canvas.height = canvas.clientHeight;
		renderFunction( canvas.getContext("2d"),canvas.width,canvas.height,objectContent);
	}

	/**
	 * Get data from canvas content in JSON format
	 */
	function getData( canvas ) {
		if ( !canvas ) {
			return undefined;
		}
		return JSON.parse( canvas.innerHTML );
	}
	

	function render() {		
		for ( var i = 0 ; i < renderers.length ; i++ ) {
			var className = renderers[i].htmlClass;
			var fun = renderers[i].fun;
			var canvasList = document.getElementsByClassName( className );
			if (!canvasList) {
				return;
			}
			for ( var ii = 0; ii < canvasList.length; ii++) {
				var canvas = canvasList[ii];
				paint(canvas,fun);
			}
		}
		
	}

	function findRenderer( htmlClass ) {
		for ( var i = 0 ; i < renderers.length ; i++ ) {
			if ( renderers[i].htmlClass == htmlClass ) {
				return renderers[i];
			}
		}
	}
	
	function roundRect(ctx, x, y, width, height, radius, fill, stroke) {
		if (typeof stroke == "undefined") {
			stroke = true;
		}
		if (typeof radius === "undefined") {
			radius = 5;
		}
		ctx.beginPath();
		ctx.moveTo(x + radius, y);
		ctx.lineTo(x + width - radius, y);
		ctx.quadraticCurveTo(x + width, y, x + width, y + radius);
		ctx.lineTo(x + width, y + height - radius);
		ctx.quadraticCurveTo(x + width, y + height, x + width - radius, y+ height);
		ctx.lineTo(x + radius, y + height);
		ctx.quadraticCurveTo(x, y + height, x, y + height - radius);
		ctx.lineTo(x, y + radius);
		ctx.quadraticCurveTo(x, y, x + radius, y);
		ctx.closePath();
		if (stroke) {
			ctx.stroke();
		}
		if (fill) {
			ctx.fill();
		}
	}
	
	/**
	 * Generate a path with rounded corner
	 */
	function roundedCorners(ctx,x,y,width,height,radius,lt,rt,lb,rb,bezel){
		ctx.translate(x,y);
		ctx.beginPath(); ctx.moveTo(0,radius);
		if(lb==1) {ctx.lineTo(0,height-radius);if(bezel) {ctx.lineTo(radius,height);}else {ctx.quadraticCurveTo(0,height,radius,height);}}else {ctx.lineTo(0,height);}
		if(rb==1) {ctx.lineTo(width-radius,height);if(bezel) {ctx.lineTo(width,height-radius);}else {ctx.quadraticCurveTo(width,height,width,height-radius);}}else {ctx.lineTo(width,height);}
		if(rt==1) {ctx.lineTo(width,radius);if(bezel) {ctx.lineTo(width-radius,0);}else {ctx.quadraticCurveTo(width,0,width-radius,0);}}else {ctx.lineTo(width,0);}	
		if(lt==1) {ctx.lineTo(radius,0);if(bezel) {ctx.lineTo(0,radius);}else {ctx.quadraticCurveTo(0,0,0,radius);}}else {ctx.lineTo(0,0);}	
		ctx.closePath();
		ctx.translate(-x,-y);
	}
	
	/**
	 * Scrolling banner
	 */
	return {
		init : function() {
			log("init");
			render();
			window.addEventListener('resize', render, false);
		},
		
		/**
		 * Register a renderer on a canvas class name
		 * render function is on the form 
		 * function( Context , width , height, data )
		 */
		registerRenderer: function( htmlClass,renderFunction ) {
			// --- Check duplication 	
			if ( findRenderer( htmlClass )) {
				throw "Renderer on class '"+htmlClass+"' already registered";
			}
			var renderer = new Object();
			renderer.htmlClass = htmlClass;
			renderer.fun = renderFunction;
			renderers.push( renderer );
		},
		
		unregisterRenderer : function( htmlClass ) {
			for ( var i = 0 ; i < renderers.length ; i++ ) {
				if ( renderers[i].htmlClass == thmlClass ) {
					renderers.splice(i,1);
					i--;
				}
			}
		},
		
		roundRect : roundRect,
		log : log,
		roundedCorners : roundedCorners,
		
		render : function() {
			render();
		}
	};
}();

window.addEventListener('load', epyxRenderer.init, false);
