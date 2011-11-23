var epyxTableScroll = function() {
	var tableToScroll;
	var y = 0;
	var dyDown = -1;
	var dyUp = 4;

	var speed = 100;
	var maxHeight;

	var offsetWait = 100;

	function draw() {
		if (!tableToScroll) {
			return;
		}
		y += dy;
		if (y <= -(maxHeight+10)) {
			dy = dyUp;
		}
		if (y >= offsetWait) {
			dy = dyDown;
		}
		//alert("draw-3  y= "+y);
		if ((y > -maxHeight) && (y < 0)) {
			tableToScroll.style.top = y+"px";
		}
	}
	
	function paint( canvas ) {
		var buffer = document.createElement("canvas");
		buffer.width = canvas.width;
		buffer.height = canvas.height;
		var bufferContext = buffer.getContext("2d");
		// draw in buffer context then switch
		var data = bufferContext.getImageData(0,0,buffer.width,buffer.height);
		canvas.getContext("2d").putImageData(data,0,0);
	}
	
	function handleWindowResize() {		
		tableToScroll = document.getElementById("epyx_scrolling_table");
		if (!tableToScroll) {
			return;
		}
		tableToScroll.parentNode.style.overflow = "hidden";
		tableToScroll.style.position ="absolute";
		maxHeight = tableToScroll.clientHeight - tableToScroll.parentNode.clientHeight+20;
		y = 0;
		dy = dyDown;
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
	
		start: function() {
			handleWindowResize();
		}
	};
}();

window.addEventListener('load', epyxTableScroll.init, false);

