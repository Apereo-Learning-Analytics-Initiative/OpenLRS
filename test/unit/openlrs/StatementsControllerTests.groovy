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

import grails.test.mixin.*

import org.junit.*


/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(StatementsController)
class StatementsControllerTests {

    void testNothing() {
        assert true; // needed to avoid failure
    }

    /* 
     * grails is confused about how to autowire in the sample service so no tests for now
     * 
    void testSimpleRouting() {
        def control = mockFor(SampleService);
        control.demand.process {  }
        controller.sampleService = control.createMock();

                // test delete is not allowed
        request.method = "DELETE";
        controller.index();
        assert response.status == 405;

        request.method = "POST";
        request.bytes = '{ "actor": { "objectType": "Agent", "mbox": "mailto:learner@example.adlnet.gov" }, "verb": "http://adlnet.gov/expapi/verbs/experienced", "object": { "id": "http://adlnet.gov/xapi/ ", "definition": { "type": "http://adlnet.gov/expapi/activities/link" } } }'.bytes;
        controller.index();
        assert response.status == 200;

        request.method = "PUT";
        request.bytes = '{ "actor": { "objectType": "Agent", "mbox": "mailto:learner@example.adlnet.gov" }, "verb": "http://adlnet.gov/expapi/verbs/experienced", "object": { "id": "http://adlnet.gov/xapi/ ", "definition": { "type": "http://adlnet.gov/expapi/activities/link" } } }'.bytes;
        params.statementId = "1";
        controller.index();
        assert response.status == 204;

        request.method = "GET";
        params.statementId = "1";
        controller.index();
        assert response.status == 200;
    }
    */

}
