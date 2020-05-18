package pl.lodz.p.it.zzpj.spotify.model;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;

import java.util.*;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "href",
        "items",
        "limit",
        "next",
        "offset",
        "previous",
        "total"
})
public class Tracks {

    @JsonProperty("href")
    private String href;
    @JsonProperty("items")
    private List<Item> items = null;
    @JsonProperty("limit")
    private Integer limit;
    @JsonProperty("next")
    private String next;
    @JsonProperty("offset")
    private Integer offset;
    @JsonProperty("previous")
    private String previous;
    @JsonProperty("total")
    private Integer total;
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

    public Tracks(LinkedHashMap<String, Object> linkedHashMap){
        href = (String) linkedHashMap.get("href");
        items = (List<Item>) linkedHashMap.get("items");
        limit = (Integer) linkedHashMap.get("limit");
        next = (String) linkedHashMap.get("next");
        offset = (Integer) linkedHashMap.get("offset");
        previous = (String) linkedHashMap.get("previous");
        total = (Integer) linkedHashMap.get("total");
    }

    public static List<Tracks> makeTracks(LinkedHashMap<String, Object> linkedHashMap){
        List<Tracks> tracks = new ArrayList<>();
        for(Map.Entry<String, Object> map: linkedHashMap.entrySet()){
            tracks.add(new Tracks((LinkedHashMap<String, Object>) map));
        }
        return tracks;
    }
}