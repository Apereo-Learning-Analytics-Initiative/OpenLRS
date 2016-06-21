/**
 * 
 */
package org.apereo.openlrs;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ggilbert
 * @author scody
 *
 */
@ConditionalOnProperty(name="openlrs.keyManager", havingValue="DatabaseKeyManager")
@Repository
public interface TenantRepository extends CrudRepository<Tenant, Long> {
  Tenant findByConsumerKey(String consumerKey);
}
