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
package org.apereo.openlrs.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author ggilbert
 *
 */
@Document(indexName="openlrsstatementmetadata", type="statement_metadata", refreshInterval="60s", replicas=1, shards=5)
@JsonInclude(Include.NON_NULL)
public class StatementMetadata implements OpenLRSEntity {

    private static final long serialVersionUID = 1L;
    @JsonIgnore public static final String OBJECT_KEY = "STATEMENT_METADATA";
    
    @Id private String id;
    @Field(type=FieldType.String,index=FieldIndex.not_analyzed) private String statementId;
    @Field(type=FieldType.String,index=FieldIndex.not_analyzed) private String context;
    @Field(type=FieldType.String,index=FieldIndex.not_analyzed) private String user;
    @Field(type=FieldType.String,index=FieldIndex.not_analyzed) private String sor;

	@Override
	@JsonIgnore
	public String getKey() {
		return id;
	}

	@Override
	@JsonIgnore
	public String getObjectKey() {
		return OBJECT_KEY;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStatementId() {
		return statementId;
	}

	public void setStatementId(String statementId) {
		this.statementId = statementId;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getSor() {
		return sor;
	}

	public void setSor(String sor) {
		this.sor = sor;
	}

}
