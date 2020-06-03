package pl.lodz.p.it.zzpj.spotify;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;
import pl.lodz.p.it.zzpj.spotify.model.Playlist;
import pl.lodz.p.it.zzpj.spotify.services.PlaylistService;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
public class RestEndpointPlaylist {

    @Autowired
    PlaylistService playlistService;

    //Get User's list of Playlist
    @GetMapping("/playlists")
    public ModelAndView getPlaylists(OAuth2Authentication details) {
        return new ModelAndView("playlistsView", "playlist", playlistService.getPlaylists(details));
        //return Playlist.makePlaylistsFromResponseEntity(responseEntity);
    }

    @GetMapping("/newPlaylist/{playlistID}")
    public ModelAndView generateNewPlaylist(OAuth2Authentication details, @PathVariable String playlistID){
        String jwt = ((OAuth2AuthenticationDetails)details.getDetails()).getTokenValue();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + jwt);
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        Playlist chosenBasePlaylist = playlistService.getPlaylist(details, playlistID);


        ResponseEntity<Object> playlistTracks = restTemplate.exchange(MessageFormat.format(
                "https://api.spotify.com/v1/playlists/{0}/tracks",playlistID),
                HttpMethod.GET,
                httpEntity,
                Object.class);
        LinkedHashMap playlistTrackBody = (LinkedHashMap) playlistTracks.getBody();
        ArrayList playlistTrackBodyItems = (ArrayList) playlistTrackBody.get("items");

        List<Integer> possitions = TempUtils.getRandomFromRangeUnreapeated(playlistTrackBodyItems.size(),5);
        List<String> baseTrackNames = new ArrayList<>();
        for (Integer possition: possitions) {
            baseTrackNames.add((String) ((LinkedHashMap) ((LinkedHashMap) playlistTrackBodyItems.get(possition))
                    .get("track")).get("id"));
        }

        UriComponentsBuilder newTracksUriBuilder = UriComponentsBuilder.fromHttpUrl("https://api.spotify.com/v1/recommendations")
                .queryParam("seed_tracks", baseTrackNames);


        ArrayList<Object> tracksForNewPlaylist = (ArrayList<Object>) ((LinkedHashMap)restTemplate.exchange(newTracksUriBuilder.toUriString(),
                HttpMethod.GET,
                httpEntity,
                Object.class
                ).getBody()).get("tracks");

        String currentUserID = playlistService.getCurrentUserID(details);

        String createdPlaylistId = playlistService.createNewPlaylist(details, chosenBasePlaylist.getName() + " Reimagined").getId();


        JSONObject parametersMapForAddingTracks = new JSONObject();
        JSONArray trackList = new JSONArray();
//        trackList.appendElement();

        for(int i =0 ; i<tracksForNewPlaylist.size() ; i++){
            trackList.appendElement( "spotify:track:"+
                    ((LinkedHashMap)tracksForNewPlaylist.get(i)).get("id")
            );
        }
        parametersMapForAddingTracks.put("uris",trackList);

        HttpEntity httpEntityForAddingTracks = new HttpEntity(parametersMapForAddingTracks.toString(), httpHeaders);

        ResponseEntity<Object> addedTracksResponse =  restTemplate.exchange(MessageFormat.format(
                "https://api.spotify.com/v1/playlists/{0}/tracks",createdPlaylistId),
                HttpMethod.POST,
                httpEntityForAddingTracks,
                Object.class
                );


        ResponseEntity<Object> regeneratedPlaylists = restTemplate.exchange("https://api.spotify.com/v1/me/playlists/?offset=0&limit=20",
                HttpMethod.GET,
                httpEntity,Object.class);


        //return Playlist.makePlaylistsFromResponseEntity(regeneratedPlaylists);
        return new ModelAndView("playlistsView", "playlist", Playlist.makePlaylistsFromResponseEntity(regeneratedPlaylists));
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
