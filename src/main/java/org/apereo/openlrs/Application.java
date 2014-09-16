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

import lti.oauth.OAuthFilter;

import org.apereo.openlrs.conditions.RedisEnabledCondition;
import org.apereo.openlrs.repositories.statements.ElasticSearchStatementRepository;
import org.apereo.openlrs.repositories.statements.RedisStatementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.integration.redis.store.RedisMessageStore;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author ggilbert
 *
 */

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages={"org.apereo.openlrs","lti"})
public class Application {
	
	@Autowired private OpenLRSAuthenticationFilter openLRSAuthenticationFilter;
	@Autowired private XAPIRequestValidationFilter xapiRequestValidationFilter;
	@Autowired private CORSFilter corsFilter;
	@Autowired private OAuthFilter oAuthFilter;
	
	public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }
	
	@Bean
    @Primary
    public ObjectMapper objectMapper(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        return mapper;
    }
	
	@Bean
	public FilterRegistrationBean corsFilterBean() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		registrationBean.setFilter(corsFilter);
		List<String> urls = new ArrayList<String>(1);
		urls.add("/xAPI/*");
		registrationBean.setUrlPatterns(urls);
		registrationBean.setOrder(1);
		return registrationBean;
	}
	
	@Bean
	public FilterRegistrationBean securityFilterBean() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		registrationBean.setFilter(openLRSAuthenticationFilter);
		List<String> urls = new ArrayList<String>(2);
		urls.add("/xAPI/statements");
		urls.add("/xAPI/statements/*");
		registrationBean.setUrlPatterns(urls);
		registrationBean.setOrder(2);
		return registrationBean;
	}
	
	@Bean
	public FilterRegistrationBean validationFilterBean() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		registrationBean.setFilter(xapiRequestValidationFilter);
		List<String> urls = new ArrayList<String>(1);
		urls.add("/xAPI/*");
		registrationBean.setUrlPatterns(urls);
		registrationBean.setOrder(3);
		return registrationBean;
	}
	
	@Bean
	public FilterRegistrationBean oAuthFilterBean() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		registrationBean.setFilter(oAuthFilter);
		List<String> urls = new ArrayList<String>(1);
		urls.add("/lti");
		registrationBean.setUrlPatterns(urls);
		registrationBean.setOrder(4);
		return registrationBean;
	}
		
	@Conditional(RedisEnabledCondition.class)
	@Bean
	RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
			MessageListenerAdapter listenerAdapter) {

		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.addMessageListener(listenerAdapter, new PatternTopic(RedisStatementRepository.TOPIC));

		return container;
	}
	
	@Bean
	MessageListenerAdapter listenerAdapter(ElasticSearchStatementRepository receiver) {
		return new MessageListenerAdapter(receiver, "onMessage");
	}
	
	@Bean
	public StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
		return new StringRedisTemplate(connectionFactory);
	}
	
	@Bean
	public RedisMessageStore redisMessageStore(RedisConnectionFactory connectionFactory) {
		return new RedisMessageStore(connectionFactory);
	}
}
