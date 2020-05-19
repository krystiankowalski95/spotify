package pl.lodz.p.it.zzpj.spotify.model;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;

import java.util.*;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "external_urls",
        "href",
        "id",
        "name",
        "type",
        "uri"
})
public class Artist {

    @JsonProperty("external_urls")
    private ExternalUrls externalUrls;
    @JsonProperty("href")
    private String href;
    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
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

    public Artist(LinkedHashMap<String, Object> linkedHashMap){
        this.externalUrls = new ExternalUrls((LinkedHashMap<String, Object>) linkedHashMap.get("external_urls"));
        this.href = (String) linkedHashMap.get("href");
        this.id = (String) linkedHashMap.get("id");
        this.name = (String) linkedHashMap.get("name");
        this.type = (String) linkedHashMap.get("type");
        this.uri = (String) linkedHashMap.get("uri");
    }

    public static List<Artist> makeArtist(LinkedHashMap<String, Object> linkedHashMap){
        List<Artist> artists = new ArrayList<>();
        for(Map.Entry<String, Object> map: linkedHashMap.entrySet()){
            artists.add(new Artist((LinkedHashMap<String, Object>) map));
        }
        return artists;
    }
}