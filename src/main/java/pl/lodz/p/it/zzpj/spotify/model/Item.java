package pl.lodz.p.it.zzpj.spotify.model;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

import java.util.*;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "album",
        "artists",
        "disc_number",
        "duration_ms",
        "explicit",
        "external_ids",
        "external_urls",
        "href",
        "id",
        "is_local",
        "is_playable",
        "name",
        "popularity",
        "preview_url",
        "track_number",
        "type",
        "uri"
})
public class Item {

    @JsonProperty("album")
    private Album album;
    @JsonProperty("artists")
    private List<Artist> artists = null;
    @JsonProperty("disc_number")
    private Integer discNumber;
    @JsonProperty("duration_ms")
    private Integer durationMs;
    @JsonProperty("explicit")
    private Boolean explicit;
    @JsonProperty("external_ids")
    private ExternalIds externalIds;
    @JsonProperty("external_urls")
    private ExternalUrls externalUrls;
    @JsonProperty("href")
    private String href;
    @JsonProperty("id")
    private String id;
    @JsonProperty("is_local")
    private Boolean isLocal;
    @JsonProperty("is_playable")
    private Boolean isPlayable;
    @JsonProperty("name")
    private String name;
    @JsonProperty("popularity")
    private Integer popularity;
    @JsonProperty("preview_url")
    private Object previewUrl;
    @JsonProperty("track_number")
    private Integer trackNumber;
    @JsonProperty("type")
    private String type;
    @JsonProperty("uri")
    private String uri;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Integer getDurationMs() {
        return durationMs;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public void makeItemFromLinkedHashMap(LinkedHashMap<String, Object> linkedHashMap) {
        href = (String) linkedHashMap.get("href");
    }

    public Item(LinkedHashMap<String, Object> linkedHashMap){
        this.album = new Album((LinkedHashMap<String, Object>) linkedHashMap.get("album"));
        this.artists = Artist.makeArtist((LinkedHashMap<String, Object>) linkedHashMap.get("artists"));
        this.discNumber = (Integer) linkedHashMap.get("disc_number");
        this.durationMs = (Integer) linkedHashMap.get("duration_ms");
        this.explicit = (Boolean) linkedHashMap.get("explicit");
        //this.externalIds = (Integer) linkedHashMap.get("duration_ms");
        this.externalUrls = new ExternalUrls((LinkedHashMap<String, Object>) linkedHashMap.get("external_urls"));
        this.href = (String) linkedHashMap.get("href");
        this.id = (String) linkedHashMap.get("id");
    }

}
