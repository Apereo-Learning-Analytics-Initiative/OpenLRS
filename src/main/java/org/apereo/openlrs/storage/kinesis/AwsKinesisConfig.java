/**
 * Copyright 2015 Unicon (R) Licensed under the
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
package org.apereo.openlrs.storage.kinesis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.services.kinesis.AmazonKinesisClient;

/**
 * @author ggilbert
 *
 */
@ConditionalOnProperty(name="openlrs.writer", havingValue="AwsKinesisWriter")
@Configuration
public class AwsKinesisConfig {

  @Value("${aws.kinesis.endpoint}") 
  private String endpoint;

  @Bean
  public AmazonKinesisClient kinesisClient() {
    /* 
     * Note we expect that the api key and secret
     * are available as system parameters either:
     * Environment Variables - AWS_ACCESS_KEY_ID and AWS_SECRET_KEY
     * Java System Properties - aws.accessKeyId and aws.secretKey
     */
    AmazonKinesisClient kinesisClient = new AmazonKinesisClient();
    kinesisClient.setEndpoint(this.endpoint);
    return kinesisClient;
  }
}
