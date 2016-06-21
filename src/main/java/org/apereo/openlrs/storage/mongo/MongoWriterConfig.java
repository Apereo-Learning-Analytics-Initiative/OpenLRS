/**
 * 
 */
package org.apereo.openlrs.storage.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

/**
 * @author ggilbert
 *
 */
@ConditionalOnProperty(name="openlrs.writer", havingValue="MongoWriter")
@Configuration
public class MongoWriterConfig {
  @Autowired
  private MongoDbFactory mongoFactory;

  @Autowired
  private MongoMappingContext mongoMappingContext;

  @Bean
  public MappingMongoConverter mongoConverter() throws Exception {
    DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoFactory);
    MappingMongoConverter mongoConverter = new MappingMongoConverter(dbRefResolver, mongoMappingContext);
    //this is my customization
    mongoConverter.setMapKeyDotReplacement("_");
    mongoConverter.afterPropertiesSet();
    return mongoConverter;
  }

}
