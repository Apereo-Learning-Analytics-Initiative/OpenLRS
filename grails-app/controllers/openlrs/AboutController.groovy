package openlrs

import grails.converters.JSON;

/**
 * From: https://github.com/adlnet/xAPI-Spec/blob/1.0.0/xAPI.md#77-about-resource
 */
class AboutController {

    def index() {
        response.status = 200;
        def data = ["version": ["1.0.0"]];
        render data as JSON;
    }
}
