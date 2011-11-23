var epyxBanner = function() {
	var context;
	var x = 0;
	var initX = 0;
	var canvas;
	var text;
	var textWidth;
	var spaceWidth = 0;
	var fontString;
	var speed = 30;
	var posY;
	


	function draw() {
		if (!context) {
			return;
		}
		x -= 1;
		if (x <= -initX) {
			x = 0;
		}
		context.font = fontString;
		context.clearRect(0, 0, canvas.width, canvas.height);
		var drawX = x;
		while (drawX < canvas.width) {
			context.fillText(text, drawX, posY);
			drawX += textWidth + spaceWidth;
		}
	}
	
	function trim( input ) {
        return input.replace(/^\s*(\S*(?:\s+\S+)*)\s*$/, "$1");
    }
	
	function handleWindowResize() {
		var canvasHeight = 50;
		canvas = document.getElementById("epyx_banner");
		if (!canvas) {
			return;
		}
		var fSize = canvas.getAttribute("fontSize");
		if (fSize == undefined) {
			fSize = canvasHeight;
		}
		var fColor = canvas.getAttribute("fontColor");
		if (fColor == undefined) {
			fColor = "black";
		}

		text = trim( canvas.innerHTML );
		if ( !text ) {
			return;
		}
		text = text + "      "
		// text = text+" "+text+" "+text+" ";

		fontString = "bold " + fSize + "px arial,sans-serif " + fColor;

		context = canvas.getContext('2d');

		canvas.style.overflow = 'hidden';
		canvas.style.position = "fixed";
		canvas.style.width = "100%";
		canvas.style.height = canvasHeight + "px";
		canvas.height = canvasHeight
		canvas.width = window.innerWidth
		context.font = fontString;
		textWidth = context.measureText(text).width;
		initX = textWidth + spaceWidth;
		posY = Math.round((canvas.height + fSize / 2) / 2);
		x = canvas.width;		
	}
	
	/**
	 * Scrolling banner
	 */
	return {
		init: function() {
			handleWindowResize();
			window.addEventListener('resize', handleWindowResize, false);
			return setInterval(draw, speed);
		},
	
		changed: function() {
			handleWindowResize();
		}
	};
}();

window.addEventListener('load', epyxBanner.init, false);

