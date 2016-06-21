/**
 * 
 */
package org.apereo.openlrs.storage.elasticsearch;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * @author ggilbert
 *
 */
@ConditionalOnProperty(name="openlrs.reader", havingValue="ElasticsearchReader")
@Configuration
@EnableAutoConfiguration(exclude = {HibernateJpaAutoConfiguration.class,
    DataSourceAutoConfiguration.class})
public class ElasticsearchConfig {
    
}
