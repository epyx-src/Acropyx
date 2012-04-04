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

//-- Required modules --
var yanop = require('yanop');
var server = require('./lib/displayer');

// ------- read command line informations -------
var userOptions = yanop.simple({
		port : {
		    type: yanop.string
		   , short: 'p'
		   ,  description: 'Server port, default 80'
		   , required: false
		},
		bindAddress : {
		    type: yanop.string
		   , short: 'b'
		   , description: 'Server bind address default 0.0.0.0'
		   , required: false
		},
		useCluster : {
		     type: yanop.flag
		   , short: 'c'
		   , description: 'Set this to allow multi-core cluster server'
		   , required: false
		},
		persistent : {
		     type: yanop.flag
		   , short: 't'
		   , description: 'Set state persistence between restart'
		   , required: false
		},
		profiler : {
		     type: yanop.flag
		   , short: 'x'
		   , description: 'Display profiler info on each request'
		   , required: false
		}
});

//----- Start the server
server.startup(userOptions);