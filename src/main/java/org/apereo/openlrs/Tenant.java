/**
 * 
 */
package org.apereo.openlrs;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author ggilbert
 *
 */
@Entity
@Table(name="tenants")
public class Tenant implements Serializable {

  private static final long serialVersionUID = -2859508737158536445L;
  
  @Id
  @GeneratedValue
  private Long id;
  
  @Column(name = "name", nullable = false)
  private String name;
  
  @Column(name = "consumer_key", nullable = false)
  private String consumerKey;
  
  @Column(name = "secret", nullable = false)
  private String secret;
  
  @Column(name = "active", nullable = false)
  private boolean active;
  
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "created", nullable = false)
  private Date created;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "updated", nullable = false)
  private Date updated;

  @PrePersist
  protected void onCreate() {
    updated = created = new Date();
  }

  @PreUpdate
  protected void onUpdate() {
    updated = new Date();
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getConsumerKey() {
    return consumerKey;
  }

  public void setConsumerKey(String consumerKey) {
    this.consumerKey = consumerKey;
  }

  public String getSecret() {
    return secret;
  }

  public void setSecret(String secret) {
    this.secret = secret;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public Date getUpdated() {
    return updated;
  }

  public void setUpdated(Date updated) {
    this.updated = updated;
  }

}
