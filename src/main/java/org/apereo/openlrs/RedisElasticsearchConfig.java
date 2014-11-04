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

import org.apereo.openlrs.conditions.RedisEnabledCondition;
import org.apereo.openlrs.repositories.statements.ElasticSearchStatementRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * @author ggilbert
 *
 */
@Configuration
@EnableAutoConfiguration
@Profile("redisElasticsearch")
public class RedisElasticsearchConfig {
	
	@Value("${instance.name}")
	private String instanceName;
	
	@Conditional(RedisEnabledCondition.class)
	@Bean
	public StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
		return new StringRedisTemplate(connectionFactory);
	}
	
	@Conditional(RedisEnabledCondition.class)
	@Bean
	RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
			MessageListenerAdapter listenerAdapter) {
		
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.addMessageListener(listenerAdapter, new ChannelTopic(channelName()));

		return container;
	}

	@Conditional(RedisEnabledCondition.class)
	@Bean
	MessageListenerAdapter listenerAdapter(ElasticSearchStatementRepository receiver) {
		return new MessageListenerAdapter(receiver, "onMessage");
	}
	
	@Conditional(RedisEnabledCondition.class)
	@Bean
	public String channelName() {
		return "Statements" + "-" + instanceName;
	}
}
