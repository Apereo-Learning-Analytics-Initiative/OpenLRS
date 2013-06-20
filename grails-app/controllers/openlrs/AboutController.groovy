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

/**
 * From: https://github.com/adlnet/xAPI-Spec/blob/1.0.0/xAPI.md#77-about-resource
 */
class AboutController {

    static defaultAction = "index"; // don't show the /index on the end

    // INJECTED
    def grailsApplication; // this is a map of grails app data: config has all the config values

    def index() {
        response.status = 200;
        def data = ["version": grailsApplication.config.tincan.versions];
        render data as JSON;
    }

}
