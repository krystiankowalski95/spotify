package pl.lodz.p.it.zzpj.spotify.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.lodz.p.it.zzpj.spotify.model.Playlist;

import java.util.List;

@Service
public class PlaylistServiceImpl implements PlaylistService {
    @Override
    public List<Playlist> getPlaylists(OAuth2Authentication details) {
        String jwt = ((OAuth2AuthenticationDetails)details.getDetails()).getTokenValue();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + jwt);
        HttpEntity httpEntity = new HttpEntity(httpHeaders);
        ResponseEntity<Object> responseEntity = restTemplate.exchange("https://api.spotify.com/v1/me/playlists/?offset=0&limit=50",
                HttpMethod.GET,
                httpEntity,Object.class);
        return Playlist.makePlaylistsFromResponseEntity(responseEntity);
    }

    @Override
    public void generateNewPlaylist(OAuth2Authentication details, String basePlaylistID) {

    }

    @Override
    public String createNewPlaylist(OAuth2Authentication details, String name, String description) {
        return null;
    }

    @Override
    public List<String> getRecommendationsForPlaylist(OAuth2Authentication details, String basePlaylistID) {
        return null;
    }

    @Override
    public void addNewTracksBasedOnRecommendation(OAuth2Authentication details, String playlistID, List<String> baseTracks) {

    }
}
