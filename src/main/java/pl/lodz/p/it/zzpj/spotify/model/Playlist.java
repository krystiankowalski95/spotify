package pl.lodz.p.it.zzpj.spotify.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Playlist {
    String name;
    String owner;
    String id;
}
