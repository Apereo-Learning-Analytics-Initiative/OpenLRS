/**
 * Copyright 2014 Unicon (R) Licensed under the
 * Educational Community License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.osedu.org/licenses/ECL-2.0

 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 *
 */
package org.apereo.openlrs;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author ggilbert
 *
 */

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages="org.apereo.openlrs")
public class Application {
	
	@Autowired private OpenLRSAuthenticationFilter openLRSAuthenticationFilter;
	@Autowired private XAPIRequestValidationFilter xapiRequestValidationFilter;
	
	public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }
	
	@Bean
	public FilterRegistrationBean securityFilterBean() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		registrationBean.setFilter(openLRSAuthenticationFilter);
		List<String> urls = new ArrayList<String>(2);
		urls.add("/xAPI/statements");
		urls.add("/xAPI/statements/*");
		registrationBean.setUrlPatterns(urls);
		return registrationBean;
	}
	
	@Bean
	public FilterRegistrationBean validationFilterBean() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		registrationBean.setFilter(xapiRequestValidationFilter);
		List<String> urls = new ArrayList<String>(1);
		urls.add("/xAPI/*");
		registrationBean.setUrlPatterns(urls);
		return registrationBean;
	}
}
