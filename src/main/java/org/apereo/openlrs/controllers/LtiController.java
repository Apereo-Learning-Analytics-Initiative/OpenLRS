/**
 * 
 */
package org.apereo.openlrs.controllers;

import javax.servlet.http.HttpServletRequest;

import lti.LaunchRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String lti(HttpServletRequest request, Model model) {
		LaunchRequest launchRequest = new LaunchRequest(request.getParameterMap());
		model.addAttribute("context_title", launchRequest.getContext_title());
		return "dashboard";
    }
}
