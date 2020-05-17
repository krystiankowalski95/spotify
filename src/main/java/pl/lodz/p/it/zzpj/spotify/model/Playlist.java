package pl.lodz.p.it.zzpj.spotify.model;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true)
public class Playlist {
    @JsonProperty("external_urls")
    private ExternalUrls externalUrls;

    @JsonProperty("href")
    private String href;

    @JsonProperty("id")
    private String id;

    @JsonProperty("images")
    private List<Image> images = null;

    @JsonProperty("name")
    private String name;

    @JsonProperty("owner")
    private Owner owner;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();


    @JsonPropertyOrder("tracks")
    private Tracks tracks;


    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @JsonAnyGetter
    public String getName() {
        return name;
    }

    @JsonAnySetter
    public void setName(String name) {
        this.name = name;
    }
}
