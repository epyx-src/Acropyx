if ( epyxRenderer ) {
	epyxRenderer.registerRenderer("epyx_team_canvas", function(context,w,h,data) {
		var x = 0;
		var y = 0;
		
		var headSize = h/15;
		
		//----- Display base -----------------
		context.save();
		var my_gradient = context.createLinearGradient(x, y, x, y+h-10);
		my_gradient.addColorStop(0, "#CCC");
		my_gradient.addColorStop(1, "#EEE");
		context.fillStyle = my_gradient;
		context.strokeStyle = "rgb(0,0,0)";
		
		context.shadowOffsetX = 0;
		context.shadowOffsetY = 3;
		context.shadowColor = "rgba(0,0,0,0.3)";
		context.shadowBlur = 4;
		epyxRenderer.roundRect(context,x+1, y+1, w-2, headSize*3.2,headSize,true,true);
		
		//----- Display name
		context.font = "bold "+(headSize*2)+"pt arial";
		context.fillStyle = "black";
		context.fillText(data.name,x+headSize*4.0,y+headSize*2.5);
		context.restore();
		
		var flag = new Image();
		flag.src = "images/Team-128.png";
		flag.onload = function() {
			var posX = 7;
			var posY = -8;
			var ratio = this.width / this.height;
			var ih = headSize*3.8;
			var iw = ih * ratio;
			context.save();
			context.shadowOffsetX = 1;
			context.shadowOffsetY = 3;
			context.shadowColor = "rgba(0,0,0,0.3)";
			context.shadowBlur = 7;
			//context.drawImage(flag,x+w+posX+3,y+3,rowHeight-17,rowHeight-17);
			context.drawImage(this,posX,posY,iw,ih);
			
			context.restore();
		};
	});	
} else {
	if ( window.console ) {
		window.console.error("No epyxRenderer definied, you should import the script epyx_rendere.js before using this one");
	}
}