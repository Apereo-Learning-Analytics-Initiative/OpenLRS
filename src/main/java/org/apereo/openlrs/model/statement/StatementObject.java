package org.apereo.openlrs.model.statement;

import java.util.Map;

/**
 * Holds a representation of a statement object
 *
 * @author Robert E. Long (rlong @ unicon.net)
 */
public class StatementObject {

    /**
     * see https://github.com/adlnet/xAPI-Spec/blob/master/xAPI.md#activity-id-requirements
     *
     * @author Robert E. Long (rlong @ unicon.net)
     */
    protected class Activity {
        private Map<String, String> name;
        private Map<String, String> description;
        private String type;
        private String moreInfo;
        private Map<String, Object> extensions;

        public Map<String, String> getName() {
            return name;
        }

        public void setName(Map<String, String> name) {
            this.name = name;
        }

        public Map<String, String> getDescription() {
            return description;
        }

        public void setDescription(Map<String, String> description) {
            this.description = description;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getMoreInfo() {
            return moreInfo;
        }

        public void setMoreInfo(String moreInfo) {
            this.moreInfo = moreInfo;
        }

        public Map<String, java.lang.Object> getExtensions() {
            return extensions;
        }

        public void setExtensions(Map<String, Object> extensions) {
            this.extensions = extensions;
        }
    }

    /**
     * see https://github.com/adlnet/xAPI-Spec/blob/master/xAPI.md#interaction-activities
     *
     * @author Robert E. Long (rlong @ unicon.net)
     */
    protected class InteractionActivity extends Activity {
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    private void determineObjectType() {
        // TODO implement this
    }

}
