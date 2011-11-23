var epyxDivAnimation = function() {
	var animatedList = new Array();
	var speed = 10;
	var parallel = false;
	var running = false;
	var onfinish = function() {};
	var steps = 50;
	var init = false;
	
	function draw() {
		if ( !running ) return;
		var finished = true;
		for ( var i = 0; i < animatedList.length; i++) {
			var animated = animatedList[i];
			var next = animated.move();
			if (next && !parallel) {
				i = animatedList.length;
				finished = false;
			} else {
				finished = finished & !next;
			}
		}
		if ( finished ) {
			if ( running ) {
				running = false;
				didFinish();
			}		
		}
	}

	function didFinish() {
		if ( onfinish != null ) {
			onfinish();
		}
	}
	function trim(input) {
		return input.replace(/^\s*(\S*(?:\s+\S+)*)\s*$/, "$1");
	}

	function initCanvas() {
		// find all canvas with a given class
		animatedList = new Array()
		var divList = document.getElementsByClassName("movable_div");
		if (!divList) {
			return;
		}

		for ( var i = 0; i < divList.length; i++) {
			var div = divList[i];
			div.style.position = "absolute";			
			var endPos = div.getAttribute("pos");
			if (endPos == undefined) {
				endPos = "-100,-100,100,100"
			}
			var elms = endPos.split(',');


			var animated = new Object();
			animated.div = div;		
			animated.x = parseInt( elms[0] );
			animated.y = parseInt( elms[1] );
			animated.endX = parseInt( elms[2] );
			animated.endY = parseInt( elms[3] );
			animated.dx = (animated.endX-animated.x) / steps;
			animated.dy = (animated.endY-animated.y) / steps;
			animated.dop = (1.0/steps);
			animated.op = 0.0;
			animated.move = function() {
				var div = animated.div;
				if (this.x >= this.endX || this.y >= this.endY) {
					return false;
				} 
				this.x = this.x + this.dx;
				this.y = this.y + this.dy;
				this.op = this.op + this.dop;
				div.style.top = this.y+"px";
				div.style.left = this.x+"px";
				div.style.opacity = animated.op;
				return true;
			};
			div.style.opacity = 0;
			animated.move(); // just to clear
			animatedList.push(animated);
		}
		
	}

	/**
	 * Scrolling banner
	 */
	return {
		start : function() {
			initCanvas();	
			running = true;
		},
		init : function() {
			if ( init ) return;
			init = true;
			initCanvas();
			return setInterval(draw, speed);
		},
		registerOnFinish: function( fun ) {
			onfinish = fun;
		}
	};
}();

window.addEventListener('load', epyxDivAnimation.init(), false);
