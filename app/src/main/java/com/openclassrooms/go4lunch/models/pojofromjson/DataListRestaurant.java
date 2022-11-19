package com.openclassrooms.go4lunch.models.pojofromjson;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Generated("jsonschema2pojo")
public class DataListRestaurant implements Serializable {
    @SerializedName("html_attributions")
    private List<Object> htmlAttributions = null;
    @SerializedName("results")
    private List<Result> results = null;
    @SerializedName("status")
    private String status;
    @SerializedName("additional_properties")
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public DataListRestaurant(List<Object> htmlAttributions, List<Result> results, String status, Map<String, Object> additionalProperties) {
        this.htmlAttributions = htmlAttributions;
        this.results = results;
        this.status = status;
        this.additionalProperties = additionalProperties;
    }

    public List<Object> getHtmlAttributions() {
        return htmlAttributions;
    }
    public List<Result> getResults() {
        return results;
    }
    public String getStatus() {
        return status;
    }

    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setHtmlAttributions(List<Object> htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }
}
