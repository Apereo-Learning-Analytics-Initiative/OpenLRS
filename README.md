OpenLRS
=======
Open source Java based Learning Record Store (http://en.wikipedia.org/wiki/Learning_Record_Store, http://tincanapi.com/learning-record-store/) which is compatible with TinCanAPI (http://tincanapi.com/) and Experience API (http://www.adlnet.gov/tla/experience-api).

*************************************************************************************
WORK IN PROGRESS
----------------
This project is still being developed and is not in a usable state yet but we welcome contributions, questions, and contributors.
*************************************************************************************

### Getting started #
* Download the source code (use the Download zip or a git/svn checkout)
    https://github.com/Unicon/OpenLRS
* Build with maven 3 (http://maven.apache.org/download.cgi):

    cd openLRS
    mvn clean install

* Copy the war file (from target/openlrs.war) into tomcat or other servlet container
* TODO more useful info here


Developers
----------
This is a grails application (http://grails.org/) which means it will run in the JVM as a war like any normal java web application. Grails also offers complete spring integration, groovy programming syntax, community support, known scalability, MVC structure, and loads of plugins.

### Getting started #
* Checkout the source code with git (read-only version):
    git clone https://github.com/Unicon/OpenLRS.git openLRS
* Install grails (at least 2.2):
    http://grails.org/doc/latest/guide/gettingStarted.html
* Install SpringToolsSuite (or some other IDE with grails support):
    http://www.springsource.org/sts
    * Make sure you install the version with Grails support
* Import the grails project into your IDE
	* Generally works best to create a new project and point it at the existing source code
* Run the project using the typical:

	grails run-app
* TODO more tips here


License
-------
ECL (a slightly more permissive ACL2)
http://opensource.org/licenses/ECL-2.0

Contacts
--------
Aaron Zeckoski (azeckoski @ vt.edu) (azeckoski @ unicon.net)
