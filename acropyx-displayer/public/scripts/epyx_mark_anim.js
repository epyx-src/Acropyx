var epyxMarkAnimation = function() {
	var animatedList = new Array();
	var speed = 10;
	var parallel = false;
	var running = false;
	var onfinish = function() {};
	var init = false;
	
	function draw() {
		if ( !running ) return;
		var finished = true;
		for ( var i = 0; i < animatedList.length; i++) {
			var animated = animatedList[i];
			var next = animated.draw();
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
		var canvasList = document.getElementsByClassName("annim_mark");
		if (!canvasList) {
			return;
		}
		for ( var i = 0; i < canvasList.length; i++) {
			var canvas = canvasList[i];
			var fSize = canvas.getAttribute("fontSize");
			if (fSize == undefined) {
				fSize = canvas.height;
			}

			var animated = new Object();
			animated.canvas = canvas;
			animated.size = 0;
			animated.maxSize = fSize;
			animated.text = trim(canvas.innerHTML);
			animated.px = 0;
			animated.py = Math.round((canvas.height + fSize / 2) / 2);
			animated.draw = function() {
				var animated = this;
				var canvas = animated.canvas;
				if (animated.size < animated.maxSize) {
					animated.size = animated.size + 1;
				} else {
					return false;
				}
				var context = canvas.getContext('2d');
				var alpha = animated.size / animated.maxSize;
				context.clearRect(0, 0, canvas.width, canvas.height);
				context.globalAlpha = alpha;
				context.fillStyle = "white";
				context.font = "bold " + animated.size + "pt arial";
                context.textAlign = "right";
				context.fillText(animated.text, 170, canvas.height);
				return true;
			};
			animated.draw(); // just to clear
			animatedList.push(animated);
		}
	}

	/**
	 * Scrolling banner
	 */
	return {
		start : function() {
			initCanvas();	
			running=true;
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

window.addEventListener('load', epyxMarkAnimation.init(), false);
