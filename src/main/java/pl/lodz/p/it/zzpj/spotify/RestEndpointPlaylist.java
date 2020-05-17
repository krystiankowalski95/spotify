package pl.lodz.p.it.zzpj.spotify;

import net.minidev.json.JSONObject;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.http.*;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import pl.lodz.p.it.zzpj.spotify.model.Playlist;
import pl.lodz.p.it.zzpj.spotify.model.PlaylistList;

import java.util.Collection;
import java.util.List;

@EnableOAuth2Client
@RestController
public class RestEndpointPlaylist {
    //Get User's list of Playlist
    @GetMapping("/playlists")
    public ModelAndView getPlaylists(OAuth2Authentication details) {
        String jwt = ((OAuth2AuthenticationDetails)details.getDetails()).getTokenValue();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + jwt);
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        /*PlaylistList playlistList = restTemplate.getForObject("https://api.spotify.com/v1/me/playlists/?offset=0&limit=20",
                PlaylistList.class,
                httpEntity);
        List<Playlist> playlists = playlistList.getPlaylists();*/
        var tmp =  restTemplate.exchange("https://api.spotify.com/v1/me/playlists/?offset=0&limit=20",
                HttpMethod.GET,
                httpEntity,
                PlaylistList.class);
        return new ModelAndView("allPlayLists", "playList", tmp.getBody().getPlaylists());
        //return playlistList.getBody().getPlaylists();
        /*return restTemplate.exchange("https://api.spotify.com/v1/me/playlists/?offset=0&limit=20",
                HttpMethod.GET,
                httpEntity,
                Object.class);*/
    }

    @PutMapping("/playlist/{name}")
    public void addPlaylists(OAuth2Authentication details, @PathVariable("name")String playlistName) {
        String jwt = ((OAuth2AuthenticationDetails)details.getDetails()).getTokenValue();
        HttpEntity<String> requestEntity;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + jwt);
        JSONObject playlistNameJsonObject = new JSONObject();
        playlistNameJsonObject.put("name",playlistName);
        requestEntity = new HttpEntity<>(playlistNameJsonObject.toString(), httpHeaders);


        restTemplate.exchange("https://api.spotify.com/v1/users/me/playlists",
                HttpMethod.PUT,
                requestEntity,
                Object.class);
    }

}
