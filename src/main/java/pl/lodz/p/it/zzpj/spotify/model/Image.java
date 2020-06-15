package pl.lodz.p.it.zzpj.spotify.model;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "height",
        "url",
        "width"
})
public class Image {

    @JsonProperty("height")
    private Integer height;
    @JsonProperty("url")
    private String url;
    @JsonProperty("width")
    private Integer width;
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

    public Image(LinkedHashMap<String, Object> linkedHashMap){
        this.height = (Integer) linkedHashMap.get("height");
        this.url = (String) linkedHashMap.get("url");
        this.width = (Integer) linkedHashMap.get("width");
    }

    public static List<Image> makeImages(LinkedHashMap<String, Object> linkedHashMap){
        List<Image> images = new ArrayList<>();
        for(Map.Entry<String, Object> map: linkedHashMap.entrySet()){
            images.add(new Image((LinkedHashMap<String, Object>) map));
        }
        return images;
    }
}