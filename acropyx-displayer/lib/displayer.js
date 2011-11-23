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

var express = require('express');
var fs = require('fs');
var path = require('path');
var socket_io = require('socket.io');
var util = require('util');
var url = require('url');
var jqtpl = require('jqtpl');
var i18n = require('i18n');

// Local requirements
var controller = require('./controller');
var utils = require('./utils');

//------- create configuration -------
var __ = i18n.__;


/**
 * Startup the displayer server
 * @param userOptions Option for starting server
 * @option : port Port for starting the server
 * @param callback Callback function when startup finished.
 */
var startup = function(userOptions, callback) {
	var options = {
		port : 8080
	  , bindAddress : "0.0.0.0"
	  , persistent :  false
	  , name : "default"
	};
	
	if (userOptions) {
		for (prop in userOptions) {
			if (userOptions.hasOwnProperty(prop)) {
		 	  	if (userOptions[prop]) {
		 	 		options[prop] = userOptions[prop];
		 	 	}
			}
		}
	}
	
	// Create express.js servers
    var server = express.createServer();	
	var displayerServer = express.createServer();
	var controllerServer = express.createServer();
	
    // Attach socket.io to the main http server
    var io = socket_io.listen(server);
    io.set('log level', 2);
	
	// enable i18n
	displayerServer.helpers({
		  __: i18n.__
	});
	
	function tenantFromHostname(hostname) {
        return hostname.split(".")[0];
	};
	
	var displayerControllers = new Object();
	getOrCreateController = function(tenant) {
	    current = displayerControllers[tenant];
	    if (!current) {
	        console.log("Creating controller '" + tenant + "'");
	        current = new controller.Controller(options, tenant);
	        var socket = io.of("/" + tenant).on("connection", function(socket) {
	            console.log("+ Client connected (" + tenant + "): " + socket.id);
	            // create message dispatcher
	            socket.on("disconnect", function(){
	                console.log("- Client disconnected (" + tenant + "): " + socket.id);
	            });
	        });
	        current.init(socket, path.join(process.cwd, "views"));
	        displayerControllers[tenant] = current;
	        
	    }
	    return current;
	};
	
	displayerServer.configure(function() {
	    //displayerServer.use(express.logger());
	    displayerServer.use(express.errorHandler({ dumpExceptions: true, showStack: true }));
		if (options.profiler) {
			util.log("Profiler enabled");
			displayerServer.use(express.profiler());
		}
		
		// i18n support to read prefered language header
		displayerServer.use(i18n.init);
		
		// enable jquery template
		displayerServer.set("view engine", "html");
		displayerServer.register(".html", require("jqtpl").express);


		// redirect root to index.html
		displayerServer.get("/", function(request, response) {
		    getOrCreateController(tenantFromHostname(request.header("Host"))).render(response);
		});
		
		// root path displays results
		displayerServer.get("/", function(request, response) {
		    response.end(getOrCreateController(tenantFromHostname(request.header("Host"))).getStep(), 200);
		});
		
	      // Serves public directory
		displayerServer.use(express.static(path.join(process.cwd, 'public')));
	});
		
	// handle post for JSON RPC
	controllerServer.configure(function() {
		var controllerHandler = function(viewName) {
			return function (request, response) {
				utils.readRequestContent(request, response , function(content) {
					try {
						var json = content.replace(new RegExp( "\\\n", "g" ), " ");
					   	var obj = JSON.parse(json);
					   	getOrCreateController(request.header("Tenant")).setStep(viewName , obj);
				    } catch ( ex ) {
					    console.log(content);
						console.log(ex);
					}
				});
			};
		};
		
		var pafHandler = function (request, response) {
			utils.readRequestContent(request, response , function(content) {
				var obj = JSON.parse(content);
				if (obj.style) {
				    getOrCreateController(request.header("Tenant")).emitScriptExecution(" epyxPafAnimation.start('"+obj.text+"','"+obj.style+"');");
				} else {
				    getOrCreateController(request.header("Tenant")).emitScriptExecution(" epyxPafAnimation.start('"+obj.text+"','');");
				}
				response.end("Success",200);
			});
		};
		
		var pafCleanHandler = function (request, response) {
		    getOrCreateController(request.header("Tenant")).emitScriptExecution("epyxPafAnimation.clear();");
			response.end("Success",200);
		};
		
		controllerServer.post("/currentFlightSolo", controllerHandler('InFlightSolo'));
		controllerServer.post("/currentFlightTeam", controllerHandler('InFlightTeam'));
		controllerServer.post("/resultFlightSolo", controllerHandler('ResultFlightSolo'));
		controllerServer.post("/resultFlightTeam", controllerHandler('ResultFlightTeam'));
		controllerServer.post("/resultRun", controllerHandler('ResultRun'));
		controllerServer.post("/waiting", controllerHandler('Waiting'));
	
		controllerServer.post("/paf", pafHandler);
		controllerServer.post("/pafclean", pafCleanHandler);
	});
	
	// Create express.js vhosts
    server.use(express.vhost('localhost', controllerServer));
    server.use(express.vhost('*', displayerServer));
	
    // Start express.js main server
	server.listen(parseInt(options.port), function() {
		// callback when the server has started
	    util.log("Acropyx displayer server running on  http://" + options.bindAddress + ":" + options.port);
	});

	return server;
};

module.exports = {
	 startup : startup
};