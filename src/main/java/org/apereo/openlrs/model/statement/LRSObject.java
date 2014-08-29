package org.apereo.openlrs.model.statement;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Holds a representation of a statement object
 *
 * @author Robert E. Long (rlong @ unicon.net)
 */
@JsonInclude(Include.NON_NULL)
public class LRSObject {

    @NotNull private String id;
    private XApiObjectTypes objectType;
    private XApiObjectDefinition definition;
    
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the objectType
	 */
	public XApiObjectTypes getObjectType() {
		return objectType;
	}
	/**
	 * @param objectType the objectType to set
	 */
	public void setObjectType(XApiObjectTypes objectType) {
		this.objectType = objectType;
	}
	/**
	 * @return the definition
	 */
	public XApiObjectDefinition getDefinition() {
		return definition;
	}
	/**
	 * @param definition the definition to set
	 */
	public void setDefinition(XApiObjectDefinition definition) {
		this.definition = definition;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LRSObject [id=" + id + ", objectType=" + objectType
				+ ", definition=" + definition + "]";
	}
}
