package com.openclassrooms.go4lunch.models.pojofromjson;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Geometry implements Serializable {
    @SerializedName("location")
    private Location location;
    @SerializedName("viewport")
    private Viewport viewport;
    @SerializedName("additional_properties")
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Geometry(Location location, Viewport viewport, Map<String, Object> additionalProperties) {
        this.location = location;
        this.viewport = viewport;
        this.additionalProperties = additionalProperties;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    public Location getLocation() {
        return location;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }
}
