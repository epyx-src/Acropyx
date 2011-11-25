Acropyx
=======

The goal of this project is to provide acrobatic paragliding live competition results with a tool for managing competitors, marks/results and show the results on a big screen.

Projects
--------

1. acropyx_displayer

	Contains a Node.js projects for the displayer.

	The displayer role is only to show results, "pafs" and other information via a big screen. It's a node.js / jquery web site. You can interact with it from virtually anywhere by REST calls.

    See [README.txt](acropyx_displayer/README.txt)


2. acropyx_admin

	Contains a Grails project for administrating the acropyx.

	The admin site allow you to manage competitors, runs. It allows you to send commands to the displayer service.

    See [README.txt](acropyx_admin/README.txt)


Licensing
---------

Please see the file called LICENSE.txt