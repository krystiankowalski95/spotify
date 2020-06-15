package pl.lodz.p.it.zzpj.spotify.model;


import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true)
@NoArgsConstructor
public class User {

    @JsonProperty("country")
    private String countryName;

    @JsonProperty("display_name")
    private String firstNameAndLastName;

    @JsonProperty("external_urls")
    private ExternalUrls externalUrls;

    @JsonProperty("email")
    private String email;

    @JsonProperty("href")
    private String href;

    @JsonProperty("images")
    private List<Image> images = null;

    @JsonProperty("type")
    private String type;

    @JsonProperty("uri")
    private String uri;

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

}
