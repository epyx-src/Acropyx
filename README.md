Acropyx
=======

The goal of this project is to provide acrobatic paragliding live competition results with a tool for managing competitors, marks/results and show the results on a big screen.

Projects
--------

1. acropyx-displayer

	Contains a [node.js](http://nodejs.org) projects for the HTML5 displayer.

	The displayer role is only to show results, "pafs" and other information via a big screen. It's a [node.js](http://nodejs.org) / [jquery](http://jquery.com) web site. Real-time update use [socket.io](http://socket.io/). You can interact with it from virtually anywhere by REST calls.

    See [README.txt](https://github.com/epyx-src/Acropyx/blob/master/acropyx-displayer/README.txt)


2. acropyx-admin

	Contains a [Grails](http://grails.org) project for administrating the acropyx.

	The admin site allow you to manage competitors, runs. It allows you to send commands to the displayer service.

    See [README.txt](https://github.com/epyx-src/Acropyx/blob/master/acropyx-admin/README.txt)


Licensing
---------

Please see the file called [LICENSE.txt](https://github.com/epyx-src/Acropyx/blob/master/LICENSE.txt)