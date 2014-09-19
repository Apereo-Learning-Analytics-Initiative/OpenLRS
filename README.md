OpenLRS
=======
Open source Java based Learning Record Store (http://en.wikipedia.org/wiki/Learning_Record_Store, http://tincanapi.com/learning-record-store/) which is compatible with TinCanAPI (http://tincanapi.com/) and Experience API (http://www.adlnet.gov/tla/experience-api).

*************************************************************************************
WORK IN PROGRESS
----------------
This project is still being developed and is not in a usable state yet but we welcome contributions, questions, and contributors (we are especially looking for any funding or resources to help move things forward more quickly).
*************************************************************************************
## Technical Overview
OpenLRS is a Java application built with Spring Boot (http://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#boot-documentation). OpenLRS manages configuration using Spring profiles. Currently there are two available profiles:

default - which uses in-memory datastorage

redisElasticsearch - which uses redis for tier1 storage and elasticsearch for tier2 persistent storage

OpenLRS is deployed as an executable jar file with Tomcat 7 embedded. Conversion to a war file is possible (http://spring.io/guides/gs/convert-jar-to-war-maven/).

### Requirements
* JDK 7+
* Maven 3+

### Using the default profile
#### Build
* mvn clean package (this produces openlrs.jar in the target folder)

#### Run (in place for development purposes)
* mvn clean package spring-boot:run

#### Deploy
* java -jar openlrs.jar

This starts OpenLRS on port 8080. Changing the server port (and other properties) can be done on the command line (http://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html)

### Using the redisElasticsearch profile
#### Build
* mvn clean package (this produces openlrs.jar in the target folder)

#### Run (in place for development purposes)
* mvn clean package spring-boot:run -Drun.jvmArguments="-Dspring.profiles.active=redisElasticsearch"

#### Deploy
java -jar -Dspring.profiles.active=redisElasticsearch openlrs.jar

License
-------
ECL (a slightly less permissive Apache2)
http://opensource.org/licenses/ECL-2.0

Contact
-------
Developed in the Apereo Learning Analytics (https://confluence.sakaiproject.org/display/LAI/) community.

Send questions or comments to the mailing list: analytics@apereo.org
