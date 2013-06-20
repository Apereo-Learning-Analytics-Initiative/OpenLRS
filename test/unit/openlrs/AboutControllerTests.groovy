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

import grails.converters.JSON;
import grails.test.mixin.*

import org.codehaus.groovy.grails.web.json.JSONObject;
import org.junit.*


/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(AboutController)
class AboutControllerTests {

    void testIndex() {
        request.method = "GET";
        controller.index();
        assert response.status == 200;
        assert response.text.contains("version");
        def jso = JSON.parse(response.text);
        assert jso instanceof JSONObject
    }

}
