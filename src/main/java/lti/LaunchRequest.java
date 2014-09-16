/**
 * 
 */
package lti;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * @author ggilbert
 *
 */
public class LaunchRequest extends LtiMessage {

	private String resource_link_id;
	
	/* Recommended Parameters */
	private String context_id;
	private String launch_presentation_document_target;
	private String launch_presentation_width;
	private String launch_presentation_height;
	private String launch_presentation_return_url;
	private String user_id;
	private String roles;

	/* Optional Parameters */
	private String context_type;
	private String launch_presentation_locale;
	private String launch_presentation_css_url;
	private String role_scope_mentor;
	private String user_image;
	private Map<String, String> custom;
	private Map<String, String> ext;
	
	/* extra Params (like button submits etc, that would be used in their oauth calc */
	private Map<String, String> extra;
	
	/* Deprecated Parameters */
	private String resource_link_title;
	private String resource_link_description;
	private String lis_person_name_given;
	private String lis_person_name_family;
	private String lis_person_name_full;
	private String lis_person_contact_email_primary;
	private String lis_outcome_service_url;
	private String lis_result_sourcedid;
	private String context_title;
	private String context_label;
	private String tool_consumer_info_product_family_code;
	private String tool_consumer_info_version;
	private String tool_consumer_instance_guid;
	private String tool_consumer_instance_name;
	private String tool_consumer_instance_description;
	private String tool_consumer_instance_url;
	private String tool_consumer_instance_contact_email;
	
	public LaunchRequest() {}
	
	public LaunchRequest(Map<String, String []> paramMap) {
		Map<String,String> flattenedParams = new TreeMap<String, String>();
		
		for (String key : paramMap.keySet()) {
			String [] values = paramMap.get(key);
			String value = null;
			if (values != null && values.length > 0) {
				for (String v : values) {
					if (value == null) {
						value = v;
					}
					else {
						value = value.concat(",");
						value = value.concat(v);
					}
				}
			}
			flattenedParams.put(key, value);
		}
		
		init(flattenedParams);
	}
	
	@JsonCreator
	public LaunchRequest(String lti_message_type, String lti_version,
			String resource_link_id, String context_id,
			String launch_presentation_document_target,
			String launch_presentation_width,
			String launch_presentation_height,
			String launch_presentation_return_url, String user_id,
			String roles, String context_type,
			String launch_presentation_locale,
			String launch_presentation_css_url, String role_scope_mentor,
			String user_image, Map<String, String> custom,
			Map<String, String> ext, String resource_link_title,
			String resource_link_description, String lis_person_name_given,
			String lis_person_name_family, String lis_person_name_full,
			String lis_person_contact_email_primary,
			String lis_outcome_service_url, String lis_result_sourcedid,
			String context_title, String context_label,
			String tool_consumer_info_product_family_code,
			String tool_consumer_info_version,
			String tool_consumer_instance_guid,
			String tool_consumer_instance_name,
			String tool_consumer_instance_description,
			String tool_consumer_instance_url,
			String tool_consumer_instance_contact_email,
			String oauth_consumer_key, String oauth_signature_method,
			String oauth_timestamp, String oauth_nonce, String oauth_version,
			String oauth_signature, String oauth_callback) {
		
		super(lti_message_type, lti_version,
				oauth_consumer_key, oauth_signature_method,
				oauth_timestamp, oauth_nonce, oauth_version,
				oauth_signature, oauth_callback);
		
		this.resource_link_id = resource_link_id;
		this.context_id = context_id;
		this.launch_presentation_document_target = launch_presentation_document_target;
		this.launch_presentation_width = launch_presentation_width;
		this.launch_presentation_height = launch_presentation_height;
		this.launch_presentation_return_url = launch_presentation_return_url;
		this.user_id = user_id;
		this.roles = roles;
		this.context_type = context_type;
		this.launch_presentation_locale = launch_presentation_locale;
		this.launch_presentation_css_url = launch_presentation_css_url;
		this.role_scope_mentor = role_scope_mentor;
		this.user_image = user_image;
		this.custom = custom;
		this.ext = ext;
		this.resource_link_title = resource_link_title;
		this.resource_link_description = resource_link_description;
		this.lis_person_name_given = lis_person_name_given;
		this.lis_person_name_family = lis_person_name_family;
		this.lis_person_name_full = lis_person_name_full;
		this.lis_person_contact_email_primary = lis_person_contact_email_primary;
		this.lis_outcome_service_url = lis_outcome_service_url;
		this.lis_result_sourcedid = lis_result_sourcedid;
		this.context_title = context_title;
		this.context_label = context_label;
		this.tool_consumer_info_product_family_code = tool_consumer_info_product_family_code;
		this.tool_consumer_info_version = tool_consumer_info_version;
		this.tool_consumer_instance_guid = tool_consumer_instance_guid;
		this.tool_consumer_instance_name = tool_consumer_instance_name;
		this.tool_consumer_instance_description = tool_consumer_instance_description;
		this.tool_consumer_instance_url = tool_consumer_instance_url;
		this.tool_consumer_instance_contact_email = tool_consumer_instance_contact_email;
	}

	public SortedMap<String, String> toSortedMap() {
		SortedMap<String,String> sm = new TreeMap<String, String>();
		
		if (custom != null && !custom.isEmpty()) {
			Set<String> keys = custom.keySet();
			for (String key : keys) {
				sm.put(key, custom.get(key));
			}
		}
		
		if (ext != null && !ext.isEmpty()) {
			Set<String> keys = ext.keySet();
			for (String key : keys) {
				sm.put(key, ext.get(key));
			}
		}
		
		if (extra != null && !extra.isEmpty()) {
			Set<String> keys = extra.keySet();
			for (String key : keys) {									
				sm.put(key, extra.get(key));				
			}
		}

		if (lti_message_type != null) {
			sm.put("lti_message_type", lti_message_type);
		}
		if (lti_version != null) {
			sm.put("lti_version", lti_version);
		}
		if (resource_link_id != null) {
			sm.put("resource_link_id", resource_link_id);
		}
		if (resource_link_title != null) {
			sm.put("resource_link_title", resource_link_title);
		}
		if (resource_link_description != null) {
			sm.put("resource_link_description", resource_link_description);
		}
		if (user_id != null) {
			sm.put("user_id", user_id);
		}
		if (user_image != null) {
			sm.put("user_image", user_image);
		}
		if (roles != null) {
			sm.put("roles", roles);
		}
		if (role_scope_mentor != null) {
			sm.put("role_scope_mentor", role_scope_mentor);
		}
		if (lis_person_name_given != null) {
			sm.put("lis_person_name_given", lis_person_name_given);
		}
		if (lis_person_name_family != null) {
			sm.put("lis_person_name_family", lis_person_name_family);
		}
		if (lis_person_name_full != null) {
			sm.put("lis_person_name_full", lis_person_name_full);
		}
		if (lis_person_contact_email_primary != null) {
			sm.put("lis_person_contact_email_primary", lis_person_contact_email_primary);
		}
		if (lis_outcome_service_url != null) {
			sm.put("lis_outcome_service_url", lis_outcome_service_url);
		}
		if (lis_result_sourcedid != null) {
			sm.put("lis_result_sourcedid", lis_result_sourcedid);
		}
		if (context_id != null) {
			sm.put("context_id", context_id);
		}
		if (context_type != null) {
			sm.put("context_type", context_type);
		}
		if (context_title != null) {
			sm.put("context_title", context_title);
		}
		if (context_label != null) {
			sm.put("context_label", context_label);
		}
		if (launch_presentation_locale != null) {
			sm.put("launch_presentation_locale", launch_presentation_locale);
		}
		if (launch_presentation_document_target != null) {
			sm.put("launch_presentation_document_target", launch_presentation_document_target);
		}
		if (launch_presentation_css_url != null) {
			sm.put("launch_presentation_css_url", launch_presentation_css_url);
		}
		if (launch_presentation_width != null) {
			sm.put("launch_presentation_width", launch_presentation_width);
		}
		if (launch_presentation_height != null) {
			sm.put("launch_presentation_height", launch_presentation_height);
		}
		if (launch_presentation_return_url != null) {
			sm.put("launch_presentation_return_url", launch_presentation_return_url);
		}
		if (tool_consumer_info_product_family_code != null) {
			sm.put("tool_consumer_info_product_family_code", tool_consumer_info_product_family_code);
		}
		if (tool_consumer_info_version != null) {
			sm.put("tool_consumer_info_version", tool_consumer_info_version);
		}
		if (tool_consumer_instance_guid != null) {
			sm.put("tool_consumer_instance_guid", tool_consumer_instance_guid);
		}
		if (tool_consumer_instance_name != null) {
			sm.put("tool_consumer_instance_name", tool_consumer_instance_name);
		}
		if (tool_consumer_instance_description != null) {
			sm.put("tool_consumer_instance_description", tool_consumer_instance_description);
		}
		if (tool_consumer_instance_url != null) {
			sm.put("tool_consumer_instance_url", tool_consumer_instance_url);
		}
		if (tool_consumer_instance_contact_email != null) {
			sm.put("tool_consumer_instance_contact_email", tool_consumer_instance_contact_email);
		}
		if (oauth_consumer_key != null) {
			sm.put("oauth_consumer_key", oauth_consumer_key);
		}
		if (oauth_signature_method != null) {
			sm.put("oauth_signature_method", oauth_signature_method);
		}
		if (oauth_timestamp != null) {
			sm.put("oauth_timestamp", oauth_timestamp);
		}
		if (oauth_nonce != null) {
			sm.put("oauth_nonce", oauth_nonce);
		}
		if (oauth_version != null) {
			sm.put("oauth_version", oauth_version);
		}
		if (oauth_signature != null) {
			sm.put("oauth_signature", oauth_signature);
		}
		if (oauth_callback != null) {
			sm.put("oauth_callback", oauth_callback);
		}
		
		

		
		return sm;
	}

	@Override
	public String toString() {
		return "LaunchRequest [resource_link_id=" + resource_link_id
				+ ", context_id=" + context_id
				+ ", launch_presentation_document_target="
				+ launch_presentation_document_target
				+ ", launch_presentation_width=" + launch_presentation_width
				+ ", launch_presentation_height=" + launch_presentation_height
				+ ", launch_presentation_return_url="
				+ launch_presentation_return_url + ", user_id=" + user_id
				+ ", roles=" + roles + ", context_type=" + context_type
				+ ", launch_presentation_locale=" + launch_presentation_locale
				+ ", launch_presentation_css_url="
				+ launch_presentation_css_url + ", role_scope_mentor="
				+ role_scope_mentor + ", user_image=" + user_image
				+ ", custom=" + custom + ", ext=" + ext + ", extra=" + extra
				+ ", oauth_consumer_key=" + oauth_consumer_key
				+ ", oauth_signature_method=" + oauth_signature_method
				+ ", oauth_timestamp=" + oauth_timestamp + ", oauth_nonce="
				+ oauth_nonce + ", oauth_version=" + oauth_version
				+ ", oauth_signature=" + oauth_signature + ", oauth_callback="
				+ oauth_callback + ", resource_link_title="
				+ resource_link_title + ", resource_link_description="
				+ resource_link_description + ", lis_person_name_given="
				+ lis_person_name_given + ", lis_person_name_family="
				+ lis_person_name_family + ", lis_person_name_full="
				+ lis_person_name_full + ", lis_person_contact_email_primary="
				+ lis_person_contact_email_primary
				+ ", lis_outcome_service_url=" + lis_outcome_service_url
				+ ", lis_result_sourcedid=" + lis_result_sourcedid
				+ ", context_title=" + context_title + ", context_label="
				+ context_label + ", tool_consumer_info_product_family_code="
				+ tool_consumer_info_product_family_code
				+ ", tool_consumer_info_version=" + tool_consumer_info_version
				+ ", tool_consumer_instance_guid="
				+ tool_consumer_instance_guid
				+ ", tool_consumer_instance_name="
				+ tool_consumer_instance_name
				+ ", tool_consumer_instance_description="
				+ tool_consumer_instance_description
				+ ", tool_consumer_instance_url=" + tool_consumer_instance_url
				+ ", tool_consumer_instance_contact_email="
				+ tool_consumer_instance_contact_email + ", lti_message_type="
				+ lti_message_type + ", lti_version=" + lti_version + "]";
	}

	public String getResource_link_id() {
		return resource_link_id;
	}

	public String getContext_id() {
		return context_id;
	}

	public String getLaunch_presentation_document_target() {
		return launch_presentation_document_target;
	}

	public String getLaunch_presentation_width() {
		return launch_presentation_width;
	}

	public String getLaunch_presentation_height() {
		return launch_presentation_height;
	}

	public String getLaunch_presentation_return_url() {
		return launch_presentation_return_url;
	}

	public String getUser_id() {
		return user_id;
	}

	public String getRoles() {
		return roles;
	}

	public String getContext_type() {
		return context_type;
	}

	public String getLaunch_presentation_locale() {
		return launch_presentation_locale;
	}

	public String getLaunch_presentation_css_url() {
		return launch_presentation_css_url;
	}

	public String getRole_scope_mentor() {
		return role_scope_mentor;
	}

	public String getUser_image() {
		return user_image;
	}

	public Map<String, String> getCustom() {
		return custom;
	}

	public Map<String, String> getExt() {
		return ext;
	}
	
	public Map<String, String> getExtra() {
		return ext;
	}

	public String getResource_link_title() {
		return resource_link_title;
	}

	public void setResource_link_title(String resource_link_title) {
		this.resource_link_title = resource_link_title;
	}

	public String getResource_link_description() {
		return resource_link_description;
	}

	public String getLis_person_name_given() {
		return lis_person_name_given;
	}

	public String getLis_person_name_family() {
		return lis_person_name_family;
	}

	public void setLis_person_name_family(String lis_person_name_family) {
		this.lis_person_name_family = lis_person_name_family;
	}

	public String getLis_person_name_full() {
		return lis_person_name_full;
	}

	public String getLis_person_contact_email_primary() {
		return lis_person_contact_email_primary;
	}

	public String getLis_outcome_service_url() {
		return lis_outcome_service_url;
	}

	public String getLis_result_sourcedid() {
		return lis_result_sourcedid;
	}

	public String getContext_title() {
		return context_title;
	}

	public String getContext_label() {
		return context_label;
	}

	public String getTool_consumer_info_product_family_code() {
		return tool_consumer_info_product_family_code;
	}

	public String getTool_consumer_info_version() {
		return tool_consumer_info_version;
	}

	public String getTool_consumer_instance_guid() {
		return tool_consumer_instance_guid;
	}

	public String getTool_consumer_instance_name() {
		return tool_consumer_instance_name;
	}

	public String getTool_consumer_instance_description() {
		return tool_consumer_instance_description;
	}

	public String getTool_consumer_instance_url() {
		return tool_consumer_instance_url;
	}

	public String getTool_consumer_instance_contact_email() {
		return tool_consumer_instance_contact_email;
	}
	
	private void init(Map<String, String> paramMap) {
		this.lti_message_type = paramMap.get("lti_message_type");
		this.lti_version = paramMap.get("lti_version");
		this.resource_link_id = paramMap.get("resource_link_id");
		this.resource_link_title = paramMap.get("resource_link_title");
		this.resource_link_description = paramMap.get("resource_link_description");
		this.user_id = paramMap.get("user_id");
		this.user_image = paramMap.get("user_image");
		this.roles = paramMap.get("roles");
		this.role_scope_mentor = paramMap.get("role_scope_mentor");
		this.lis_person_name_given = paramMap.get("lis_person_name_given");
		this.lis_person_name_family = paramMap.get("lis_person_name_family");
		this.lis_person_name_full = paramMap.get("lis_person_name_full");
		this.lis_person_contact_email_primary = paramMap.get("lis_person_contact_email_primary");
		this.lis_outcome_service_url = paramMap.get("lis_outcome_service_url");
		this.lis_result_sourcedid = paramMap.get("lis_result_sourcedid");
		this.context_id = paramMap.get("context_id");
		this.context_type = paramMap.get("context_type");
		this.context_title = paramMap.get("context_title");
		this.context_label = paramMap.get("context_label");
		this.launch_presentation_locale = paramMap.get("launch_presentation_locale");
		this.launch_presentation_document_target = paramMap.get("launch_presentation_document_target");
		this.launch_presentation_css_url = paramMap.get("launch_presentation_css_url");
		this.launch_presentation_width = paramMap.get("launch_presentation_width");
		this.launch_presentation_width = paramMap.get("launch_presentation_width");
		this.launch_presentation_return_url = paramMap.get("launch_presentation_return_url");
		this.tool_consumer_info_product_family_code = paramMap.get("tool_consumer_info_product_family_code");
		this.tool_consumer_info_version = paramMap.get("tool_consumer_info_version");
		this.tool_consumer_instance_guid = paramMap.get("tool_consumer_instance_guid");
		this.tool_consumer_instance_name = paramMap.get("tool_consumer_instance_name");
		this.tool_consumer_instance_description = paramMap.get("tool_consumer_instance_description");
		this.tool_consumer_instance_url = paramMap.get("tool_consumer_instance_url");
		this.tool_consumer_instance_contact_email = paramMap.get("tool_consumer_instance_contact_email");
		this.oauth_consumer_key = paramMap.get("oauth_consumer_key");
		this.oauth_timestamp = paramMap.get("oauth_timestamp");
		this.oauth_nonce = paramMap.get("oauth_nonce");
		this.oauth_signature = paramMap.get("oauth_signature");
		this.oauth_signature_method = paramMap.get("oauth_signature_method");
		this.oauth_version = paramMap.get("oauth_version");
		this.oauth_callback = paramMap.get("oauth_callback");
		
		
		//I'm not excited about this solution, but the only way to find unknown params
		//is to check to see if they already exist. Get the sorted map at this point
		//and check
		SortedMap<String,String> currentSM = this.toSortedMap();
		
		if (paramMap != null && !paramMap.isEmpty()) {
			Set<String> keys = paramMap.keySet();
			if (keys != null && !keys.isEmpty()) {
				for (String key : keys) {
					if (key != null && key.startsWith("custom_")) {
						if (this.custom == null) {
							this.custom = new HashMap<String, String>();
						}

						this.custom.put(key, paramMap.get(key));
					}
					else if (key != null && key.startsWith("ext_")) {
						if (this.ext == null) {
							this.ext = new HashMap<String, String>();
						}

						this.ext.put(key, paramMap.get(key));
					}
					else {
						
						//If we don't have the key at this point, it's likely a 
						//unknown type that we didn't account for. We still need
						//to add it for the oauth comparison
						if (currentSM != null && !currentSM.containsValue(key))
						{
							if (this.extra == null) {
								this.extra = new HashMap<String, String>();
							}
							this.extra.put(key, paramMap.get(key));
						}
					}
				}
			}
		}
	}
}
