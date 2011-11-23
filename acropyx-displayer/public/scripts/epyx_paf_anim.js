var epyxPafAnimation = function() {
	var canvas;
	var animatedList = new Array();

	function drawAll() {
		var todraw = 0;
		for ( var i = 0; i < animatedList.length; i++) {
			if (!animatedList[i].ended) {
				todraw++;
			}
		}
		if (todraw != 0) {
			canvas.getContext("2d")
					.clearRect(0, 0, canvas.width, canvas.height);
		} else {
			return;
		}
		for ( var i = 0; i < animatedList.length; i++) {
			animatedList[i].draw();
		}
	}

	function initCanvas() {
		if (!canvas) {
			canvas = document.createElement("canvas");
			canvas.style.position = "fixed";
			canvas.style.width = "100%";
			canvas.style.height = "100%";
			canvas.style.top = "0px";
			canvas.style.left = "0px";
			document.body.appendChild(canvas);
			canvas.height = canvas.clientHeight;
			canvas.width = canvas.clientWidth;
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
		ctx.quadraticCurveTo(x + width, y + height, x + width - radius, y
				+ height);
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

	function parseArguments(conf,args) {
		conf.x = -100;
		conf.y = -100;
		conf.step = 50;
		conf.endx = canvas.width / 2;
		conf.endy = canvas.height / 2;
		conf.size = 50;
		conf.temp = 1.0;
		conf.startColor = "#BFB";
		conf.endColor = "#5A5";
		conf.border = "#040";
		conf.spin = 1.0;
		
		var list = args.split(";");
		for ( var i = 0; i < list.length; i++) {
			var arg = list[i].split(":");
			if (arg.length == 2) {
				fillArgument(conf, arg[0].toLowerCase(), arg[1]);
			}
		}
		return conf;
	}

	function computeRelativePosition(value, relative, ext) {
		value = ""+value;
		if ( value.charAt( value.length -1 ) === '%') {
			value = value.substring(0,value.length -1 );
			value = ( parseInt( value ) * relative ) / 100.0;
		} else {
			value = parseInt( value );
		}
		if (value > 0) {
			if (ext) {
				return relative + value;
			} else {
				return value;
			}
		} else if (value < 0) {
			if (ext) {
				return value;
			} else {
				return relative + value;
			}
		} else {
			return relative / 2;
		}
	}

	/**
	 * All key ar in lower case
	 */
	function fillArgument(conf, key, value) {
		if (key == "startx") {
			var n = value;
			conf.x = computeRelativePosition(n, canvas.width, true);
		} else if (key == "starty") {
			var n = value;
			conf.y = computeRelativePosition(n, canvas.height, true);
		} else if (key == "endx") {
			var n = value;
			conf.endx = computeRelativePosition(n, canvas.width, false);
		} else if (key == "endy") {
			var n = value;
			conf.endy = computeRelativePosition(n, canvas.height, false);
		} else if (key == "size") {
			conf.size = parseInt(value);
		} else if (key == "decay") {
			conf.temp = parseFloat(value);
		} else if (key == "startcolor") {
			conf.startColor = value;
		} else if (key == "endcolor") {
			conf.endColor = value;
		} else if (key == "border") {
			conf.border = value;
		} else if (key == "step") {
			conf.step = parseFloat(value);
		} else if (key == "decay") {
			conf.temp = parseFloat(value);
		}  else if (key == "spin") {
			conf.spin = parseFloat(value);
		} else if ( key == "start") {
			conf.x = canvas.width / 2;
			conf.y = canvas.height / 2;
			if ( value.indexOf("r") != -1) {
				conf.x = canvas.width + 100;
			} else if ( value.indexOf("l") != -1) {
				conf.x = -100;
			}
			if ( value.indexOf("b") != -1) {
				conf.y = canvas.height + 100;
			} else if ( value.indexOf("t") != -1) {
				conf.y = -100;
			}
		} else {
			console.warn("Unkonw key " + key)
		}
	}

	/**
	 * @param text
	 *            Test to display
	 * @param style 
	 * 			  Style of the anim value can be : 
	 *  start
	 *            can be 'l' 'r' 't' 'b' and a combine of 2. i.e "tl" means top
	 *            left
	 *  endx
	 *            End position x if negative use right canvas relative position
	 *            if 0 center
	 *  endy
	 *            End position x if negative use bottom canvas relative position
	 *            if 0 center
	 *  size
	 *            Size in point for the text style
	 *  decay
	 *            decay factory -1 means no decay 1.0 is 1 second decay
	 */
	function startAnimation(text, style) {
		var anim = new Object();
		var conf = parseArguments(anim,style);

		anim.r = 0.1 * Math.PI - Math.PI;
		anim.dr = (Math.PI / anim.step) * anim.spin;
		anim.dop = 1.0 / anim.step;
		anim.op = 0.0;
		anim.dx = (anim.endx - anim.x) / anim.step;
		anim.dy = (anim.endy - anim.y) / anim.step;
		anim.ended = false;
		anim.decay = false;
		anim.text = text;
		// /----- Draw function ----
		anim.draw = function() {
			var finished = 0;
			var context = canvas.getContext("2d");
			this.step -= 1;
			if (!this.ended) {
				if (this.step > 0) {
					this.x += this.dx;
					this.y += this.dy;
				} else {
					finished++;
				}

				if (!this.decay) {
					if (this.op < 1.0) {
						this.op += this.dop;
					}
					// do not exceed decay temp
					if (this.temp > 0 && this.op > this.temp) {
						this.op = this.temp;
					}
				} else {
					// decay fade to transparent
					this.op -= this.dop * 2;
					if (this.op <= 0.0) {
						this.op = 0.0;
						finished++;
					}
				}
				if (finished > 0) {
					// both x and y are set to the end
					if (this.temp > 0) {
						if (!this.decay) {
							this.op = this.temp; // bigger value to do a
													// slower decay
							this.decay = true;
						}
					} else {
						finished++;
					}
				} else {
					// rotate
					this.r += this.dr;
				}
				if (finished >= 2) {
					this.ended = true;
				}
			}
			// effective display
			context.save();
			context.globalAlpha = anim.op;
			context.font = "bold " + anim.size + "pt arial";
			var textSize = context.measureText(this.text);
			var w = textSize.width * 1.2;
			var h = anim.size * 1.4;

			context.translate(this.x, this.y);
			var my_gradient = context.createLinearGradient(0, 0, 0, h);
			my_gradient.addColorStop(0, this.startColor);
			my_gradient.addColorStop(1, this.endColor);
			context.fillStyle = my_gradient;

			context.rotate(anim.r);
			context.translate(-w / 2, -h / 2);

			context.lineWidth = h / 10;
			context.strokeStyle = this.border;
			roundRect(context, 0, 0, w, h, h / 5, true, true);
			context.fillStyle = "black";
			context.fillText(this.text, w * 0.1, h - h / 4)

			context.restore();
		}
		animatedList.push(anim);
	}

	function clearAnimation() {
		if ( !canvas ) return;
		animatedList = new Array();
		canvas.getContext("2d").clearRect(0, 0, canvas.width, canvas.height);
	}

	/**
	 * Scrolling banner
	 */
	return {
		init : function() {
			initCanvas();
			return setInterval(drawAll, 50);
		},

		start : startAnimation,

		clear : function() {
			clearAnimation();
		}
	};
}();

window.addEventListener('load', epyxPafAnimation.init, false);
