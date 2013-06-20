/**
 * Copyright 2013 Unicon (R) Licensed under the
 * Educational Community License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package openlrs

import org.apereo.openlrs.SampleService;

import grails.converters.JSON;
import grails.util.Holders;

/**
 * Handles statements
 * The basic communication mechanism of the Experience API.
 * From: https://github.com/adlnet/xAPI-Spec/blob/1.0.0/xAPI.md#72-statement-api
 * 
PUT Statements
Example endpoint: http://example.com/xAPI/statements
Stores Statement with the given id.
Returns: 204 No Content

POST Statements
Example endpoint: http://example.com/xAPI/statements
Stores a Statement, or a set of Statements. Since the PUT method targets a specific Statement id, 
POST must be used rather than PUT to save multiple Statements, or to save one Statement without 
first generating a Statement id. An alternative for systems that generate a large amount of Statements 
is to provide the LRS side of the API on the AP, and have the LRS query that API for the list of updated 
(or new) Statements periodically. This will likely only be a realistic option for systems that provide a lot of data to the LRS.
Returns: 200 OK, statement id(s) (UUID).

GET Statements
Example endpoint: http://example.com/xAPI/statements
This method may be called to fetch a single Statement or multiple Statements. If the statementId or
voidedStatementId parameter is specified a single Statement is returned.
Otherwise returns: A StatementResult Object, a list of Statements in reverse chronological order based on "stored" time, 
subject to permissions and maximum list length. If additional results are available, a URL to retrieve them will be included in the StatementResult Object.
Returns: 200 OK, Statement or Statement Result (See section 4.2 for details)
 * 
 * @author azeckoski Aaron Zeckoski (azeckoski @ vt.edu)
 */
class StatementsController {

    static defaultAction = "index"; // don't show the /index on the end
    static config = Holders.config; // this allows static access to the grails config values

    // INJECTED
    def grailsApplication; // this is a map of grails app data: config has all the config values
    def SampleService sampleService; // this is auto-injected

    def index() {
        // process the request
        makeHeaders();
        def method = findMethod();
        if (method == 'PUT') {
            if (params.containsKey("statementId")) {
                // TODO actually implement this
                response.status = 204;
            } else {
                response.status = 400;
                data = ["error":"missing the statementId"];
            }
        } else if (method == 'POST') {
            // TODO actually implement this
            // this MIGHT be a GET so need to differentiate on inputs
            response.status = 200;
        } else if (method == 'DELETE') {
            response.status = 405; // not allowed
        } else {
            // assume GET in other cases
            def data;
            if (params.containsKey("statementId")) {
                // TODO actually implement this
                response.status = 200;
                data = handleStatementFetch(params.statementId);
            } else {
                response.status = 400;
                data = ["error":"missing the statementId"];
            }
            render data as JSON;
        }
    }

    private Map handleStatementFetch(String id) {
        // TODO go get this data from a service
        // sample call to sample service
        sampleService.process();
        return [
            "id": id,
            "actor": [
                "objectType":"Agent",
                "name":"Aaron Zeckoski",
                "mbox":"mailto:azeckoski@fake.email.com"
            ],
            "verb": [
                "id":"http://adlnet.gov/expapi/verbs/created",
                "display":[ "en-US":"created" ]
            ],
            "object": [
                "id":"http://example.adlnet.gov/xapi/example/activity"
            ],
        ]
    }

    private void makeHeaders() {
        response.setHeader('X-Experience-API-Version', grailsApplication.config.tincan.version);
        def now = new Date(System.currentTimeMillis());
        def now8601 = now.format("yyyy-MM-dd'T'HH:mm'Z'");
        response.setHeader('X-Experience-API-Consistent-Through', now8601);
    }

    /**
     * @return the method (as detected in params or by request type)
     */
    private String findMethod() {
        def method = 'GET';
        def incoming = 'GET';
        if (params.containsKey('method')) {
            incoming = params.method;
        } else {
            incoming = request.method;
        }
        incoming.toUpperCase();
        if (incoming == 'PUT' || incoming == 'POST' || incoming == 'DELETE' || incoming == 'GET' || incoming == 'HEAD') {
            method = incoming;
        }
        return method;
    }

}
