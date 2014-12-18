OpenLRS
=======
Open source Java based Learning Record Store (http://en.wikipedia.org/wiki/Learning_Record_Store, http://tincanapi.com/learning-record-store/) which is compatible with TinCanAPI (http://tincanapi.com/) and Experience API (http://www.adlnet.gov/tla/experience-api).

*************************************************************************************
Current Status
----------------
### xAPI Support

#### General
* Support for Basic HTTP Auth
* Support for OAuth 1.0a 
* Support for X-Experience-API-Version header

#### Statement API
* Support for POST
* Support for GET and the following filters:
 * statementId
 * agent
 * verb
 * activity
 * since
 * until
 * limit
 
#### About API
* Supported

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

Note: to use the redisElasticsearch profile you should have access to redis (2.6+) and elasticsearch (1.3.x +) instances. If redis and elasticsearch are not running on localhost with the default ports you will need to update or override the relevant redis and elasticsearch properties - see the Overriding properties section below.

#### Build
* mvn clean package (this produces openlrs.jar in the target folder)

#### Run (in place for development purposes)
* mvn clean package spring-boot:run -Drun.jvmArguments="-Dspring.profiles.active=redisElasticsearch"

#### Run in debug mode
* mvn clean package spring-boot:run -Drun.jvmArguments="-Dspring.profiles.active=redisElasticsearch -agentlib:jdwp=transport=dt_socket,address=8000,server=y,suspend=n"


#### Deploy
java -jar -Dspring.profiles.active=redisElasticsearch openlrs.jar

### Overriding properties

Details on externalizing configuration can be found [here] (http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html)

Some examples

* mvn clean package spring-boot:run -Drun.jvmArguments="-Dspring.config.location=/export/home/prod.properties"
* java -jar -Dspring.config.location=/export/home/prod.properties openlrs.jar

Note to activate the redisElasticsearch profile be sure to include spring.profiles.active=redisElasticsearch in your external properties file. 

A list of Spring-Boot properties can be found [here] (http://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html) Some common properties of interest are listed below.

### Tomcat Configuration Options
* server.port=8080
* server.address= # bind to a specific NIC
* server.session-timeout= # session timeout in seconds
* server.context-path= # the context path, defaults to '/'
* server.servlet-path= # the servlet path, defaults to '/'
* server.tomcat.access-log-pattern= # log pattern of the access log
* server.tomcat.access-log-enabled=false # is access logging enabled
* server.tomcat.protocol-header=x-forwarded-proto # ssl forward headers
* server.tomcat.remote-ip-header=x-forwarded-for
* server.tomcat.basedir=/tmp # base dir (usually not needed, defaults to tmp)
* server.tomcat.background-processor-delay=30; # in seconds
* server.tomcat.max-threads = 0 # number of threads in protocol handler
* server.tomcat.uri-encoding = UTF-8 # character encoding to use for URL decoding

### Redis Configuration Options
* spring.redis.host=localhost # server host
* spring.redis.password= # server password
* spring.redis.port=6379 # connection port
* spring.redis.pool.max-idle=8 # pool settings ...
* spring.redis.pool.min-idle=0
* spring.redis.pool.max-active=8
* spring.redis.pool.max-wait=-1

### Elasticsearch Configuration Options
* spring.data.elasticsearch.cluster-name= # The cluster name (defaults to elasticsearch)
* spring.data.elasticsearch.cluster-nodes= # The address(es) of the server node (comma-separated; if not specified starts a client node)
* spring.data.elasticsearch.repositories.enabled=true # if spring data repository support is enabled

License
-------
ECL (a slightly less permissive Apache2)
http://opensource.org/licenses/ECL-2.0

Contact
-------
Developed in the Apereo Learning Analytics (https://confluence.sakaiproject.org/display/LAI/) community.

Send questions or comments to the mailing list: analytics@apereo.org
