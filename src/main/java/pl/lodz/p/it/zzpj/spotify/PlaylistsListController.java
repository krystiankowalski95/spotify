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
        String url = "http://localhost:8080/playlists";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Playlist> response
                = restTemplate.getForEntity(url, Playlist.class);
        return (List<Playlist>) response;
    }
}
