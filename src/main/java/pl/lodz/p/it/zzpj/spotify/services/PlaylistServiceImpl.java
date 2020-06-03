package pl.lodz.p.it.zzpj.spotify.services;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.lodz.p.it.zzpj.spotify.TempUtils;
import pl.lodz.p.it.zzpj.spotify.model.Playlist;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
    public Playlist getPlaylist(OAuth2Authentication details, String playlistID) {
        String jwt = ((OAuth2AuthenticationDetails)details.getDetails()).getTokenValue();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + jwt);
        HttpEntity httpEntity = new HttpEntity(httpHeaders);
        return new Playlist((LinkedHashMap<String, Object>) restTemplate.exchange(MessageFormat.format(
                "https://api.spotify.com/v1/playlists/{0}",playlistID),
                HttpMethod.GET,
                httpEntity,
                Object.class).getBody());
    }

    @Override
    public void generateNewPlaylist(OAuth2Authentication details, String basePlaylistID) {

        Playlist chosenBasePlaylist = this.getPlaylist(details, basePlaylistID);

        List<String> stringList = this.getRecommendationsForPlaylist(details, basePlaylistID);

        String currentUserID = this.getCurrentUserID(details);

        String createdPlaylistId = this.createNewPlaylist(details, chosenBasePlaylist.getName() + " Reimagined").getId();

        this.addNewTracksBasedOnRecommendation(details, createdPlaylistId, stringList);

    }

    @Override
    public Playlist createNewPlaylist(OAuth2Authentication details, String name) {
        String jwt = ((OAuth2AuthenticationDetails)details.getDetails()).getTokenValue();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + jwt);
        HttpEntity httpEntity = new HttpEntity(httpHeaders);
        JSONObject parametersMapForNewPlaylist = new JSONObject();
        parametersMapForNewPlaylist.put("name", name);
        parametersMapForNewPlaylist.put("description", "This is a test playlist for ZZPJ project");

        HttpEntity httpEntityForNewPlaylist = new HttpEntity(parametersMapForNewPlaylist.toString(), httpHeaders);
        ResponseEntity responseEntity = restTemplate.exchange(MessageFormat.format(
                "https://api.spotify.com/v1/users/{0}/playlists",this.getCurrentUserID(details)),
                HttpMethod.POST,
                httpEntityForNewPlaylist,
                Object.class
        );
        return Playlist.makePlaylistFromResponseEntity(responseEntity);
    }

    @Override
    public List<String> getRecommendationsForPlaylist(OAuth2Authentication details, String basePlaylistID) {
        String jwt = ((OAuth2AuthenticationDetails)details.getDetails()).getTokenValue();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + jwt);
        HttpEntity httpEntity = new HttpEntity(httpHeaders);
        ResponseEntity<Object> playlistTracks = restTemplate.exchange(MessageFormat.format(
                "https://api.spotify.com/v1/playlists/{0}/tracks",basePlaylistID),
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
        ArrayList<String> stringArrayList = new ArrayList<>();
        for(int i =0 ; i<tracksForNewPlaylist.size() ; i++){
            stringArrayList.add((String) ((LinkedHashMap)tracksForNewPlaylist.get(i)).get("id")
            );
        }
        return stringArrayList;
    }

    @Override
    public void addNewTracksBasedOnRecommendation(OAuth2Authentication details, String playlistID, List<String> baseTracks) {
        String jwt = ((OAuth2AuthenticationDetails)details.getDetails()).getTokenValue();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + jwt);
        HttpEntity httpEntity = new HttpEntity(httpHeaders);
        JSONObject parametersMapForAddingTracks = new JSONObject();
        JSONArray trackList = new JSONArray();
//        trackList.appendElement();

        for(int i =0 ; i<baseTracks.size() ; i++){
            trackList.appendElement( "spotify:track:"+
                    baseTracks.get(i)
            );
        }
        parametersMapForAddingTracks.put("uris",trackList);

        HttpEntity httpEntityForAddingTracks = new HttpEntity(parametersMapForAddingTracks.toString(), httpHeaders);

        ResponseEntity<Object> addedTracksResponse =  restTemplate.exchange(MessageFormat.format(
                "https://api.spotify.com/v1/playlists/{0}/tracks",playlistID),
                HttpMethod.POST,
                httpEntityForAddingTracks,
                Object.class
        );
    }

    @Override
    public String getCurrentUserID(OAuth2Authentication details) {
        String jwt = ((OAuth2AuthenticationDetails)details.getDetails()).getTokenValue();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + jwt);
        HttpEntity httpEntity = new HttpEntity(httpHeaders);
        return (String) ((LinkedHashMap) restTemplate.exchange("https://api.spotify.com/v1/me",
                HttpMethod.GET,
                httpEntity,
                Object.class
        ).getBody()).get("id");
    }
}
