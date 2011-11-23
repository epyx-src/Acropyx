var server = require('../lib/displayer');
var assert = require('assert');
var vows = require('vows');
var http = require('http');

var options = {
	port : 8234,
	bindAddress : "localhost"
};

server.startup( options , function ( displayerServer ) {
	
	vows.describe('Acropyx Displayer').addBatch({
		'Waiting navigation' : {
			'server' : {
				topic : function() {
					var client = http.createClient(options.port, options.bindAddress);
					var request = client.request('POST', '/navigate', {});
					var self = this;
					request.on('response', function(response) {
						 response.on('end', self.callback);
					});	
					request.write("waiting");
					request.end();
				},
				'after startup' : function ( param ) {
					console.log( "param1=",param );
				},				
			}
		}
	}).addBatch({
		'Server Shutdown' : {
			'server' : {
				topic : function() {
					console.log("X");
					this.callback ( null ) ;
				},
				'end everything' : function() {
					displayerServer.close();
				}
			}
		}
	}).run();
});
/*
suite.addBatch({
	'waiting page' : {
		topic: function() {
			return server.startup( options ,this.callback);
		},
		'after server creation' : { topic : function ( httpServer ) {
			var client = http.createClient(options.port, options.bindAddress);
			var request = client.request('POST', '/navigate', {});
			var self = this;
			console.log("this.callback=",this.callback);
			request.on('response', function(response) {
				 console.log("self.callback=",self.callback);
				 response.on('end', self.callback);
			});	
			request.write("waiting");
			request.end();
		}},
		'after post on /navigate (waiting)' : function () {
			var client = http.createClient(options.port, options.bindAddress);
			var request = client.request('GET', '/navigate', {});
			var self = this;
			request.on('response', function(response) {
				var body = "";
				response.on('data', function (data) {
					body += data;
				});
				response.on('end', function() {
					assert.isEqual(body,"waiting");
					self.callback();
				});
			});	
		},
		'after succesfull GET' : function() {
			topic.close();
		}
	}
});

suite.run();
*/