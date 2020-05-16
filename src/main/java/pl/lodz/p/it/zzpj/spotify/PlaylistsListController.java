package pl.lodz.p.it.zzpj.spotify;


import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import pl.lodz.p.it.zzpj.spotify.model.Playlist;

import java.util.Arrays;
import java.util.List;

public class PlaylistsListController {

    private RestEndpointPlaylist playlists;
    private RestTemplate restTemplate;



    public List<Playlist> getPlaylists() {
        ResponseEntity<Playlist[]> response = restTemplate.getForEntity(
                "http://localhost:8080/playlists",
                Playlist[].class
        );

        Playlist[] playlists = response.getBody();
        return Arrays.asList(playlists);
    }
}
