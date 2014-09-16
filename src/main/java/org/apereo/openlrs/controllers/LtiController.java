/**
 * 
 */
package org.apereo.openlrs.controllers;

import javax.servlet.http.HttpServletRequest;

import lti.LaunchRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author ggilbert
 *
 */
@Controller
@RequestMapping("/lti")
public class LtiController {
    
	@RequestMapping(method=RequestMethod.POST, value = {"", "/"})
    public String lti(HttpServletRequest request) {
		LaunchRequest launchRequest = new LaunchRequest(request.getParameterMap());
		return "dashboard";
    }
}
