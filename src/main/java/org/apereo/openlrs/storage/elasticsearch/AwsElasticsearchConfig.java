/**
 * 
 */
package org.apereo.openlrs.storage.elasticsearch;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;

import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

/**
 * @author ggilbert
 *
 */
@ConditionalOnProperty(name="openlrs.reader", havingValue="AwsElasticsearchReader")
@Configuration
public class AwsElasticsearchConfig {
  @Value("${aws.es.connectionUrl}")
  private String connectionUrl;
  
  @Bean
  public JestClient jestClient() {
    Gson gson = new GsonBuilder().registerTypeAdapter(DateTime.class, new com.google.gson.JsonDeserializer<DateTime>() {
      @Override
      public DateTime deserialize(JsonElement json, java.lang.reflect.Type typeOfT, com.google.gson.JsonDeserializationContext context)
              throws JsonParseException {
          return new DateTime(json.getAsString());
      }
    }).create();
    
    JestClientFactory factory = new JestClientFactory();
    factory.setHttpClientConfig(new HttpClientConfig
      .Builder(connectionUrl)
      .gson(gson)
      .multiThreaded(true)
      .discoveryFrequency(60, TimeUnit.SECONDS)
      .build());
    return factory.getObject();
  }

}
