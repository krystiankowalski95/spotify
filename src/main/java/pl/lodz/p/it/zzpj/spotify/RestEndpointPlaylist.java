package pl.lodz.p.it.zzpj.spotify;

import net.minidev.json.JSONObject;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.*;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import pl.lodz.p.it.zzpj.spotify.model.Playlist;
import pl.lodz.p.it.zzpj.spotify.model.Tracks;

import java.awt.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import java.util.Collection;
import java.util.List;

@RestController
public class RestEndpointPlaylist {
    //Get User's list of Playlist
    @GetMapping("/playlists")
    public List<Playlist> getPlaylists(OAuth2Authentication details) {
        String jwt = ((OAuth2AuthenticationDetails)details.getDetails()).getTokenValue();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + jwt);
        HttpEntity httpEntity = new HttpEntity(httpHeaders);
        ResponseEntity<Object> responseEntity = restTemplate.exchange("https://api.spotify.com/v1/me/playlists/?offset=0&limit=20",
                HttpMethod.GET,
                httpEntity,Object.class);

        return Playlist.makePlaylistsFromResponseEntity(responseEntity);
    }

    @GetMapping("/newPlaylist")
    public Playlist generateNewPlaylist(OAuth2Authentication details){
        String jwt = ((OAuth2AuthenticationDetails)details.getDetails()).getTokenValue();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + jwt);
        HttpEntity httpEntity = new HttpEntity(httpHeaders);
        ResponseEntity<Object> responseEntity = restTemplate.exchange("https://api.spotify.com/v1/me/playlists/?offset=0&limit=20",
                HttpMethod.GET,
                httpEntity,Object.class);
        List<Playlist> userCurrentPlaylists = Playlist.makePlaylistsFromResponseEntity(responseEntity);
        Playlist chosenBasePlaylist =  userCurrentPlaylists.get(0);
        String basePLaylistId = chosenBasePlaylist.getId();

        ResponseEntity<Object> playlistTracks = restTemplate.exchange(MessageFormat.format(
                "https://api.spotify.com/v1/playlists/{0}/tracks",basePLaylistId),
                HttpMethod.GET,
                httpEntity,
                Object.class);
        LinkedHashMap playlistTrackBody = (LinkedHashMap) playlistTracks.getBody();
        ArrayList playlistTrackBodyItems = (ArrayList) playlistTrackBody.get("items");
        LinkedHashMap baseTrack1 = (LinkedHashMap) ((LinkedHashMap) playlistTrackBodyItems.get(0)).get("track");
        LinkedHashMap baseTrack2 = (LinkedHashMap) ((LinkedHashMap) playlistTrackBodyItems.get(1)).get("track");

        String baseTrackId1 = (String) baseTrack1.get("id");
        String baseTrackId2 = (String) baseTrack2.get("id");

        UriComponentsBuilder newTracksUriBuilder = UriComponentsBuilder.fromHttpUrl("https://api.spotify.com/v1/recommendations")
                .queryParam("seed_tracks",baseTrackId1, baseTrackId2);

        ResponseEntity<Object> tracksForNewPlaylist = restTemplate.exchange(newTracksUriBuilder.toUriString(),
                HttpMethod.GET,
                httpEntity,
                Object.class
                );

        String currentUserID = (String) ((LinkedHashMap) restTemplate.exchange("https://api.spotify.com/v1/me",
                HttpMethod.GET,
                httpEntity,
                Object.class
                ).getBody()).get("id");
        




//
//        ResponseEntity<Object> newPlaylist = restTemplate.exchange(MessageFormat.format(
//                "https://api.spotify.com/v1/users/{0}/playlists",basePLaylistId),
//                HttpMethod.GET,
//                httpEntity,
//                Object.class);


        return Playlist.makePlaylistsFromResponseEntity(responseEntity).get(0);

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
