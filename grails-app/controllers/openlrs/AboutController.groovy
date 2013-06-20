package openlrs

import grails.converters.JSON;

class AboutController {

    def index() {
        response.status = 200;
        def data = ["version": ["1.0.0"]];
        render data as JSON;
    }
}
