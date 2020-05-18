package pl.lodz.p.it.zzpj.spotify.model;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;

import java.util.*;

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

    public Playlist(LinkedHashMap<String, Object> linkedHashMap) {
        href = (String) linkedHashMap.get("href");
    }

    public static List<Playlist> makePlaylistsFromResponseEntity(ResponseEntity<Object> responseEntity) {
        List<Playlist> convertedPlaylists = new ArrayList<>();
        LinkedHashMap object = (LinkedHashMap)responseEntity.getBody();
        ArrayList playlistArray = (ArrayList) object.get("items");


        for (Object o : playlistArray) {
            LinkedHashMap element = (LinkedHashMap) o;
            convertedPlaylists.add(new Playlist(element));
        }
        return convertedPlaylists;
    }
}
