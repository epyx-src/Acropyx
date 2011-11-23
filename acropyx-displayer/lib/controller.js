/*******************************************************************************
 * Copyright (c) 2011 epyx SA.
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License along
 * with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/

/*
 * Controller module is reponsible to keep the state of the displayer with
 * currentStep and currentObject
 *  
 */

//--- Required modules ----
var jqtpl = require('jqtpl');
var fs = require('fs');
var path = require('path');
var i18n = require('i18n');

//--- Constants and configurations ---
var _DEFAULT_VIEW_NAME = "empty";

//--- Module variables ---
var currentStep = "";
var currentObject = "";
var socket;
var viewDir = ".";


//--- private functions ---


/**
 * Initialize the controller with a ioSocker for push notification and view location
 * @param ioSocket Socket to emit notifications
 * @param viewLocation Where find jstmpl templates
 * @returns none
 */
var Controller = function(opt, tenant) {
	var options = opt || {};
	this.name = options.name || "default";
	this.persistent = options.persistent || false ;
	this.currentStep = "";
	this.currentObject = "";
	this.socket = null;
	this.viewDir = null ;	
	this.tmpFile = this.name+".state.tmp";
	this.tenant = tenant;
	
	var self = this;

	// look if a previous state file exists and load it if the file exists
	if ( this.persistent ) {
		path.exists(self.tmpFile, function(exists) {
			if (exists) {
				console.log("Found a previous state file, loading it...");
				fs.readFile(self.tmpFile, function (err, data) {
					 if (err) return;
					 var obj = JSON.parse( data );
					 self.currentStep = obj.currentStep;
					 self.currentObject = obj.currentObject;
				});
			}
		});
	}
};

//Store a simple file to manage state
Controller.prototype.storeState = function( fn ) {
	if ( this.persistent ) {
		var obj = {
			"currentStep" : this.currentStep
		  , "currentObject" : this.currentObject
		};
		fs.writeFile(this.tmpFile,JSON.stringify( obj ), fn );
	}
};

Controller.prototype.init = function(ioSocket , viewLocation) {
	if ( this.socket ) {
		throw "Controller already initialized";
	}
	this.socket = ioSocket;
	this.viewDir = viewLocation;		
};

/**
 * Enhance the object with 2 methods for templating and localization
 */
Controller.prototype.enhanceObject = function ( currentObject ) {
	var self = this;
	currentObject.__ = function ( text ) {
		return i18n.__( text );
	};
	currentObject.__template = function( name,data ) {
		var dataObject = data || currentObject;
		self.enhanceObject( dataObject );
		var content = fs.readFileSync( path.join(self.viewDir,name+".html"),"utf-8" );
		return jqtpl.tmpl(content,dataObject);
	};
};

Controller.prototype.currentView = function() {
	var view = this.currentStep;
	if ( !view  ) {
		view = _DEFAULT_VIEW_NAME;
	}
	return view;
};	

Controller.prototype.render = function (response ) {
	response.render(this.currentView(), this.currentObject);
};

Controller.prototype.setStep = function (step, object) {
	this.currentStep = step;
	this.currentObject = object;
	this.storeState();
	
	var self = this;
	
	var view = this.currentView();
	var viewPath = path.join(self.viewDir,view+".html");
	
	fs.readFile(viewPath,"utf-8", function(err,fileContent){
		if (err) throw err;
		self.enhanceObject( self.currentObject );	
		try {
			self.emitMainChange ( jqtpl.tmpl(fileContent,self.currentObject) );
		} catch ( err2 ) {
			console.error( err2 );
		}
	});
};

Controller.prototype.getStep = function() {
	return this.currentStep;
};

Controller.prototype.emitScriptExecution = function(script) {
	if (this.socket) {
		this.socket.emit('js-exec',script);
	}
};

Controller.prototype.emitMainChange = function(content) {
	if (this.socket) {
		this.socket.emit('set-content', content);
	}
};




//--- public functions ---
module.exports = {
	Controller : Controller
};