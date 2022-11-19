package com.openclassrooms.go4lunch.models.pojofromjson;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Southwest implements Serializable {
        @SerializedName("lat")
        private Double lat;
        @SerializedName("lng")
        private Double lng;
        @SerializedName("additional_properties")
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        public Double getLat() {
                return lat;
        }

        public void setLat(Double lat) {
                this.lat = lat;
        }

        public Double getLng() {
                return lng;
        }

        public void setLng(Double lng) {
                this.lng = lng;
        }

        public Map<String, Object> getAdditionalProperties() {
                return additionalProperties;
        }

        public void setAdditionalProperties(Map<String, Object> additionalProperties) {
                this.additionalProperties = additionalProperties;
        }
}
