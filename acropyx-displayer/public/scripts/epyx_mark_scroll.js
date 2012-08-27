var epyxMarkTableScroll = function() {
	var toScroll;
	var y = 0;
	var dyDown = -1;
	var dyUp = 5;

	var speed = 50;
	var maxHeight;

	var offsetWait = 150;

	var rowHeight = 70;
	var fontStyle = "bold 26pt arial,sans-serif";

	var jsonContent;
	
	var logDiv;
	
	function log(str) {
		if ( window.console ) {
			window.console.log( str );
		}
		else if (document.getElementById("log_div")) {
			document.getElementById("log_div").innerHTML += str + "<br>";
		}
	}

	function draw() {
		if (!toScroll) {
			return;
		}
		y += dy;
		if (y <= -(maxHeight + Math.round(offsetWait/4))) {
			dy = dyUp;
		}
		if (y >= offsetWait) {
			dy = dyDown;
		}
		// alert("draw-3 y= "+y);
		if ((y >= -maxHeight) && (y <= 0)) {
			toScroll.style.top = y + "px";
		}
	}

	/**
	 * Double buffering draw of the table
	 */
	function paint(canvas) {
		var list = getData(canvas);

        //Check sync
        var syncHeight = 1;
        if (list[0].pilot1 != null)
        {
            syncHeight = 2 //* rowHeight;
        }


		canvas.width = canvas.clientWidth;
		canvas.height = syncHeight * rowHeight * list.length+10;
		/*
		 * Do not use double buffering when using images...
		var bufferContext = buffer.getContext("2d");
		// draw in buffer context then switch
		paintContent(bufferContext, canvas.width, list);
		var data = bufferContext.getImageData(0, 0, buffer.width, buffer.height);
		canvas.getContext("2d").putImageData(data, 0, 0);
		*/
		paintTable( canvas.getContext("2d"),0,5,canvas.width,list);
	}

	function getData( canvas ) {
		var content = jsonContent;
		if ( !content ) {
			content = canvas.innerHTML;
		}
		return JSON.parse(content);
	}
	
	/**
	 * Paint table content in the given context
	 * height is calculated by rowHeight
	 */
	function paintTable(context,px,py,w, list) {
		var y = py;
//        //Check sync
        var syncHeight = 1
        if (list[0].pilot1 != null)
        {
            syncHeight = 1.5; // * rowHeight;
        }
		var h = syncHeight * rowHeight;
		for ( var i = 0; i < list.length; i++) {
			paintRow(context, px, y+5, h, w-2, list[i],i+1);
			y += h;
		}
	}

	function getCountryImageSrc( countryCode ) {
		return "images/flags/"+ countryCode.toLowerCase()+".png";
	}

    function getWarningsImageSrc( obj ) {
        if (obj.warnings >= 3)
        {
            return  "images/red_card.png";
        }

        return "images/yellow_card.png";
    }
	
	/**
	 * paint the given row in a context and at position
	 */
	function paintRow(context, x, y, h, w, row, position) {
		context.save();

        var syncHeight  = 1;
        if (row.pilot1 != null)
        {
            syncHeight  = 1.5;
        }

		var my_gradient = context.createLinearGradient(x, y, x, y+h );
		my_gradient.addColorStop(0, "#CCC");
		my_gradient.addColorStop(1, "#EEE");
		context.fillStyle = my_gradient;
		context.strokeStyle = "rgb(0,0,0)";
		
		context.shadowOffsetX = 0;
		context.shadowOffsetY = 3;
		context.shadowColor = "rgba(0,0,0,0.3)";
		context.shadowBlur = 4;
		roundRect(context,x+1, y+1, w-2, h -8,15,true,true);
		
		context.restore();

		var textLineY = y + h/syncHeight - 22;
		var mark_width = 0;
		if (row.mark || row.mark == 0){
			mark_width = 130
		}
		var flag_width = 0;
		if (row.country){
			flag_width = 60
		}
        var flag_width_sync = 0;
        if (row.country1){
            flag_width_sync = 60
        }


        var nbRuns_width = 0;
        if (row.nbRuns > 0)
        {
            nbRuns_width = 150;
        }


		//---------- draw number ----------------
		context.save();
		var nb_width = 60;
		my_gradient = context.createLinearGradient(x, y+5, x, y+h/syncHeight-10);
		my_gradient.addColorStop(0, "#888");
		my_gradient.addColorStop(1, "#333");
		context.fillStyle = my_gradient;
		context.drawWidth=2;
		roundRect(context,x+5, y+5, nb_width, h/syncHeight-16,15,true,true);
		context.fillStyle = "white";
		context.font = fontStyle;
		context.textAlign = "right";
		context.fillText(""+position, nb_width-2, textLineY);
		
		//+++++-- Highlight ---
		my_gradient = context.createLinearGradient(x, y, x, y+30);
		my_gradient.addColorStop(0, "rgba(255,255,255,0.0)");
		my_gradient.addColorStop(1, "rgba(255,255,255,0.3)");
		context.fillStyle = my_gradient;
		roundRect(context,x+7, y+8, nb_width-12, 15,10,true,false);
		context.restore();

		
		//-------- draw competitor text -------------
		context.save();
		var my_gradient = context.createLinearGradient(x, y, x, y+20);
		my_gradient.addColorStop(0, "rgba(255,255,255,0.8)");
		my_gradient.addColorStop(1, "rgba(255,255,255,0.0)");
		context.fillStyle = my_gradient;
	    	var endCompetitorGradientX = w-mark_width-80;
			
		roundRect(context, x+nb_width+10, y+5, endCompetitorGradientX, 40, 20, true, false);
		
		context.fillStyle = "#333";
		context.textAlign = "left";
		context.font = fontStyle;

        context.fillText(row.name, x+nb_width+flag_width+14, textLineY);

        //Paint warnings
        if ( row.warnings > 0 ) {
            var cardImage = new Image();
            cardImage.src = getWarningsImageSrc(row);

            cardImage.onload = function() {
                context.save();
                context.shadowOffsetX = 3;
                context.shadowOffsetY = 3;
                context.shadowColor = "rgba(0,0,0,0.3)";
                context.shadowBlur = 5;
                //context.drawImage(flag,x+w+posX+3,y+3,rowHeight-17,rowHeight-17);
                context.drawImage(cardImage,endCompetitorGradientX-nbRuns_width,y+6,rowHeight-17,rowHeight-17);

                if (row.warnings == 2)
                {
                    context.drawImage(cardImage,endCompetitorGradientX-nbRuns_width-50 ,y+6,rowHeight-17,rowHeight-17);
                }

                context.restore();
            };
        }

        //Print Runs count
        if (row.nbRuns) {
            var runText = (row.nbRuns == 1)? " run" : " runs";
            context.fillText(row.nbRuns + runText , endCompetitorGradientX-70, textLineY );
        }
        context.restore();



        if (row.pilot1 != null)
        {
            if ( row.country1 ) {
                var flag = new Image();
                flag.src = getCountryImageSrc(row.country1);
                flag.onload = function() {
                    var posX = -mark_width-60;

                    context.save();
                    context.shadowOffsetX = 3;
                    context.shadowOffsetY = 3;
                    context.shadowColor = "rgba(0,0,0,0.3)";
                    context.shadowBlur = 5;
                    //context.drawImage(flag,x+w+posX+3,y+3,rowHeight-17,rowHeight-17);
                    context.drawImage(flag,x+nb_width+12 +350,y +6,rowHeight-25,rowHeight-25);

                    context.restore();
                };
            }

            context.fillStyle = "#333";
            context.textAlign = "left";
            context.font = fontStyle;

            context.fillText(row.pilot1, x+nb_width+flag_width_sync+14+350, textLineY );

            if ( row.country2 ) {
                var flag = new Image();
                flag.src = getCountryImageSrc(row.country2);
                flag.onload = function() {
                    var posX = -mark_width-60;

                    context.save();
                    context.shadowOffsetX = 3;
                    context.shadowOffsetY = 3;
                    context.shadowColor = "rgba(0,0,0,0.3)";
                    context.shadowBlur = 5;
                    //context.drawImage(flag,x+w+posX+3,y+3,rowHeight-17,rowHeight-17);
                    context.drawImage(flag,x+nb_width+12 + 350,y+ 40 + 10,rowHeight-25,rowHeight-25);

                    context.restore();
                };
            }

            context.fillText(row.pilot2, x+nb_width+flag_width_sync+14 + 350, textLineY + 40);
        }


		//---------- draw flag ------------
		if ( row.country ) {
			var flag = new Image();
			flag.src = getCountryImageSrc(row.country);
			flag.onload = function() {
				var posX = -mark_width-60;
	
				context.save();
				context.shadowOffsetX = 3;
				context.shadowOffsetY = 3;
				context.shadowColor = "rgba(0,0,0,0.3)";
				context.shadowBlur = 5;
				//context.drawImage(flag,x+w+posX+3,y+3,rowHeight-17,rowHeight-17);
				context.drawImage(flag,x+nb_width+12,y+6,rowHeight-17,rowHeight-17);
				
				context.restore();
			};
		}
		
		//------- draw mark ---------------------------
		context.save();
		
		
		if ( row.mark || row.mark == 0) {
		var fontColor = "white";
		var glow = undefined;
		my_gradient = context.createLinearGradient(x, y+5, x, y+h/syncHeight-10);
		if ( position == 1 ) {
			// gold
			my_gradient.addColorStop(0, "#A95");
			my_gradient.addColorStop(1, "#A92");
			fontColor = "white";
			glow = "yellow";
		} else if ( position == 2 ) {
			// silver
			my_gradient.addColorStop(0, "#BBB");
			my_gradient.addColorStop(1, "#666");
			fontColor = "white";
			glow = "#CCC";
		} else if ( position == 3 ) {
			// bronze
			my_gradient.addColorStop(0, "#B65");
			my_gradient.addColorStop(1, "#B62");
			fontColor = "white";
			glow = "orange";
		} else {
			my_gradient.addColorStop(0, "#DDD");
			my_gradient.addColorStop(1, "#CCC");
			fontColor = "#444";
		}
		context.fillStyle = my_gradient;
		roundRect(context,x+w-mark_width-5, y+5, mark_width, h/syncHeight-16,15,true,true);
		
		if ( glow ) {
			context.shadowOffsetX = 0;
			context.shadowOffsetY = 0;
			context.shadowColor = glow;
			context.shadowBlur = 10;
		}
		context.fillStyle = fontColor;
		context.font = "bold 30pt arial";
		context.textAlign = "right";
        if (row.mark == 0 ){
            context.fillText("DSQ", x+w-10, textLineY);
        }
        else{
		    context.fillText(row.mark, x+w-10, textLineY);
        }
		context.restore();
		//+++++-- Highlight ---
		my_gradient = context.createLinearGradient(x, y, x, y+30);
		my_gradient.addColorStop(0, "rgba(255,255,255,0.0)");
		my_gradient.addColorStop(1, "rgba(255,255,255,0.4)");
		context.fillStyle = my_gradient;
		roundRect(context,x+w-mark_width-3, y+8, mark_width-6, 15,10,true,false);
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

	function handleWindowResize() {
		log("handleWindowResize");
		canvas = document.getElementById("epyx_scrolling_table");
		if (!canvas) {
			return;
		}
		canvas.parentNode.style.overflow = "hidden";
		canvas.style.position = "absolute";
		paint(canvas);
		maxHeight = canvas.clientHeight - canvas.parentNode.clientHeight + 10;
		y = offsetWait;
		dy = dyDown;
		toScroll = canvas;
	}

	/**
	 * Scrolling banner
	 */
	return {
		init : function() {
			log("init");
			handleWindowResize();
			window.addEventListener('resize', handleWindowResize, false);
			return setInterval(draw, speed);
		},
        
		startWithContent : function( jsonContent) {
			jsonContent = jsonContent;
			handleWindowResize();
		},
		
		start : function() {
			handleWindowResize();
		}
	};
}();

window.addEventListener('load', epyxMarkTableScroll.init, false);
