package org.apereo.openlrs.model.statement;

/**
 * Holds a representation of a statement result
 *
 * @author Robert E. Long (rlong @ unicon.net)
 */
public class LRSResult {

    /*
     * The result field represents a measured outcome related to the statement, such as completion, success, or score. 
     * It is also extendible to allow for arbitrary measurements to be included.
     * NOTE: the API score fields types are unclear in the spec (maybe int or float)
     */
    /**
     * Score from -1.0 to 1.0 where 0=0% and 1.0=100%
     */
    private Float scaled;

    /**
     * Raw score - any number
     */
    private Number raw;

    /**
     * Minimum score (range) - any number
     */
    private Number min;

    /**
     * Maximum score (range) - any number
     */
    private Number max;

    /**
     * string representation of the grade (e.g. A, B, C, D, F, pass, fail, first, second, etc.)
     * NOTE: this should be encoded into the XAPI extension for the result. Example for "A":
     * "extensions": {
     *    "http://sakaiproject.org/xapi/activities/grade": "A"
     * }
     * 
     * Or the more complex and supposedly portable way (lowercase, strip spaces, and append in the id):
     * "result" : {
     *     .....
     *     "extensions" : {
     *         "http://sakaiproject.org/xapi/extensions/result/classification" : {
     *             "objectType" : "activity",
     *             "id":"http://sakaiproject.org/xapi/activities/grade-a",
     *             "definition" : { 
     *                "type" : "http://sakaiproject.org/xapi/activitytypes/grade_classification",
     *                "name" : {
     *                    "en-US":"A"
     *                }
     *             }
     *         }
     *     }
     * }
     */
    private String grade;

    /**
     * true if successful, false if not, or null for unknown
     */
    private Boolean success;

    /**
     * true if completed, false if not, or null for unknown
     */
    private Boolean completion;

    /**
     * Duration of the activity in seconds
     * Have to convert this to https://en.wikipedia.org/wiki/ISO_8601#Durations for sending to the Experience API,
     * ignore the value if it is less than 0
     */
    private int duration = -1;

    /**
     * A string response appropriately formatted for the given activity.
     */
    private String response;

    /**
     * empty constructor
     */
    public LRSResult() {
    }

    /**
     * Simplest possible result, only indicates if it was completed or not,
     * generally should be used only when nothing else will fit
     * @param completion true if completed, false if not (cannot be null)
     */
    public LRSResult(boolean completion) {
        this();
        this.completion = completion;
    }

    /**
     * Simplest possible result, only indicates if it was completed or not,
     * generally should be used only when nothing else will fit
     * @param completion true if completed, false if not (cannot be null)
     */
    public LRSResult(String response) {
        this();
        this.response = response;
    }

    /**
     * @param scaled Score from -1.0 to 1.0 where 0=0% and 1.0=100%
     * @param success true if successful, false if not, or null for not specified
     * @throws IllegalArgumentException if scaled is not valid
     */
    public LRSResult(Float scaled, Boolean success) {
        this();
        if (scaled == null) {
            throw new IllegalArgumentException("LRSResult scaled cannot be null");
        }
        setScore(scaled);
        this.success = success;
    }

    /**
     * @param raw Raw score - any number, must be >= min and <= max (if they are set)
     * @param min Minimum score (range) - any number (can be null)
     * @param max Maximum score (range) - any number (can be null)
     * @param success true if successful, false if not, or null for not specified
     * @throws IllegalArgumentException if the minimum is not less than (or equal to) the maximum OR raw is not within the range OR all values are null 
     */
    public LRSResult(Number raw, Number min, Number max, Boolean success) {
        this();
        if (raw == null) {
            throw new IllegalArgumentException("LRSResult raw cannot be null");
        }
        setScore(null, raw, min, max);
        this.success = success;
    }

    /**
     * NOTE: always use the numeric score when possible, this is only to be used when you cannot convert to a numeric score
     * @param grade a string grade value (will be stored as an extension), cannot be null or empty
     * @param success true if successful, false if not, or null for not specified
     * @see #grade
     */
    public LRSResult(String grade, Boolean success) {
        this();
        if (grade == null || "".equals(grade)) {
            throw new IllegalArgumentException("LRSResult grade cannot be null or empty");
        }
        this.success = success;
    }

    // TODO optional extensions?

    /**
     * Set the score to a floating point scaled range value
     * 
     * @param scaled Score from -1.0 to 1.0 where 0=0% and 1.0=100%
     * @throws IllegalArgumentException if the scaled value is outside the -1 to 1 (inclusive) range
     */
    public void setScore(Float scaled) {
        this.scaled = scaled;
        if (scaled != null) {
            if (scaled.floatValue() < -1.0f) {
                throw new IllegalArgumentException("LRSResult scaled cannot be < -1");
            } else if (scaled.floatValue() > 1.0f) {
                throw new IllegalArgumentException("LRSResult scaled cannot be > 1");
            }
        }
    }

    /**
     * @param raw Raw score - any number (can be null), must be >= min and <= max (if they are set)
     * @throws IllegalArgumentException if raw is not within the min-max range
     */
    public void setRawScore(Number raw) {
        if (raw != null) {
            if (this.min != null && raw.floatValue() < min.floatValue()) {
                throw new IllegalArgumentException("score raw ("+raw+") must not be less than min ("+this.min+")");
            }
            if (this.max != null && raw.floatValue() > max.floatValue()) {
                throw new IllegalArgumentException("score raw ("+raw+") must not be greater than max ("+this.max+") inclusive");
            }
        }
        this.raw = raw;
    }

    /**
     * Set up a completely detailed score,
     * NOTE: scaled MUST be within the range of -1 to 1 inclusive
     * NOTE: raw MUST be within the range of min to max inclusive
     * 
     * @param scaled Score from -1.0 to 1.0 where 0=0% and 1.0=100%
     * @param raw Raw score - any number (can be null), must be >= min and <= max (if they are set)
     * @param min Minimum score (range) - any number (can be null)
     * @param max Maximum score (range) - any number (can be null)
     * @throws IllegalArgumentException if the scaled value is outside the -1 to 1 (inclusive) range OR if the minimum is not less than (or equal to) the maximum OR raw is not within the range OR all values are null 
     */
    public void setScore(Float scaled, Number raw, Number min, Number max) {
        if (scaled == null && raw == null && min == null && max == null) {
            throw new IllegalArgumentException("score inputs cannot all be null");
        }
        setScore(scaled);
        this.min = min;
        this.max = max;
        if (this.min != null && this.max != null) {
            if (min.floatValue() > max.floatValue()) {
                throw new IllegalArgumentException("score min ("+this.min+") must be less than max ("+this.max+")");
            }
        }
        setRawScore(raw);
    }

    /**
     * NOTE: always use the numeric score when possible, this is only to be used when you cannot convert to a numeric score
     * @param grade a string grade value (will be stored as an extension), null to clear
     * @see #grade
     */
    public void setGrade(String grade) {
        this.grade = grade;
    }

    /**
     * @param success true if successful, false if not, or null if not specified
     * @see #success
     */
    public void setSuccess(Boolean success) {
        this.success = success;
    }

    /**
     * @param completion true if completed, false if not, or null if not specified
     * @see #completion
     */
    public void setCompletion(Boolean completion) {
        this.completion = completion;
    }

    /**
     * @param duration Time spent on the activity in seconds, set to -1 to clear this
     * @see #duration
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * @param A response appropriately formatted for the given Activity.
     * @see #response
     */
    public void setResponse(String response) {
        this.response = response;
    }

    /**
     * @see #scaled
     */
    public Float getScaled() {
        return scaled;
    }

    /**
     * @see #raw
     */
    public Number getRaw() {
        return raw;
    }

    /**
     * @see #min
     */
    public Number getMin() {
        return min;
    }

    /**
     * @see #max
     */
    public Number getMax() {
        return max;
    }

    /**
     * @see #success
     */
    public Boolean getSuccess() {
        return success;
    }

    /**
     * @see #completion
     */
    public Boolean getCompletion() {
        return completion;
    }

    /**
     * @see #duration
     */
    public int getDuration() {
        return duration;
    }

    /**
     * @see #response
     */
    public String getResponse() {
        return response;
    }

    /**
     * @see #grade
     */
    public String getGrade() {
        return grade;
    }

    public void setScaled(Float scaled) {
        this.scaled = scaled;
    }

    public void setRaw(Number raw) {
        this.raw = raw;
    }

    public void setMin(Number min) {
        this.min = min;
    }

    public void setMax(Number max) {
        this.max = max;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        String points = "";
        if (scaled != null) {
            points = "scaled=" + scaled;
        }
        if (raw != null) {
            points += ",raw=" + scaled;
        }
        if (min != null && max != null) {
            points += ",min=" + min + ",max=" + max;
        }

        return "Result["+points+(grade!=null?" "+grade:"")+(response!=null?" response="+response:"")+(success!=null?(success?" success":" fail"):"")+(completion!=null?(completion?" complete":" incomplete"):"")+ "]";
    }

}
