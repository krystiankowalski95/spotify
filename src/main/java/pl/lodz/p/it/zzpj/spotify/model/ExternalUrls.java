package pl.lodz.p.it.zzpj.spotify.model;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "spotify"
})
public class ExternalUrls {

    @JsonProperty("spotify")
    private String spotify;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();


    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public ExternalUrls(LinkedHashMap<String, Object> linkedHashMap) {
        this.spotify = (String) linkedHashMap.get("spotify");
    }
}