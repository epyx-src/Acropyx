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

//--- Required modules ---
var util = require('util');

/**
 * Execute serially multiple tests on http requests
 * @param handlers Array of handler : 
 * 		handler is an object with 3 properties ; 2 functions 'onSuccess' and 'onError' and 1 object 'options'
 * @param end
 * @returns
 */
module.exports.testHttpRequest = function( handlers,end ) {
	var fn = __getNextTest( handlers,0,end);
	fn();
};

var __getNextTest = function(handlers,pos, end) {
	return (pos ==  handlers.length)?end:function() {
		__testHttpRequest(  handlers[pos],__getNextTest(handlers,pos+1,end),end);
	};
};

/**
 * HTTP test 
 * @param handler object with 3 properties ; 2 funtions 'onSuccess' and 'onError' and 1 object 'options'
 * @returns
 */
var __testHttpRequest = function( handler,next,end ) {
	var timeouter;
	var clientOption = handler.options ;
	var onSuccess = function( res,content) {
		try {
			handler.onSuccess && handler.onSuccess( res,content);
			setTimeout(next,0);
		} catch ( err ) {
			onError( err );
		}
	};
	
	var onError = function( err ) {
		try {
			handler.onError && handler.onError( err );
		} finally {
			end();		
			throw err;
		}
	};
	
	try {
		if ( typeof handler === 'function') {
			handler( function() {
				onSuccess(null,null);
			});
			return;
		}
		
		handler.name && console.log("Running http request test : "+handler.name);
		
		if ( clientOption.timeout ) {
			timeouter = setTimeout( function() { 
				throw "Timeout"; ender(); 
			},clientOption.timeout);
		}
	
	
		var req = require('http').request(clientOption,function(res) {
			var content = "";
			res.on('data',function( chunk ){
				content+=chunk;
			});
			res.on('end',function(){
				timeouter && clearTimeout( timeouter );
				onSuccess( res, content);
			});		
		}).on('error' , function( err ) {
			timeouter && clearTimeout( timeouter );
			onError( err );
		});
		
		if ( clientOption.body ) {
			if (  typeof clientOption.body === 'function' ) {
				req.write( clientOption.body() );
			} if (  typeof clientOption.body === 'object' ) {
				req.write( JSON.stringify(clientOption.body) );
			} else {
				req.write( clientOption.body );
			}
		}
		req.end();
	} catch ( err ) {
		timeouter && clearTimeout( timeouter );
		onError( err );
	}
};