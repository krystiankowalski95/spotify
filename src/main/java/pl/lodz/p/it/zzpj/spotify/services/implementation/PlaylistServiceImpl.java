package pl.lodz.p.it.zzpj.spotify.services.implementation;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import pl.lodz.p.it.zzpj.spotify.UtilsToRand;
import pl.lodz.p.it.zzpj.spotify.model.Playlist;
import pl.lodz.p.it.zzpj.spotify.HttpConfiguration;
import pl.lodz.p.it.zzpj.spotify.services.interfaces.PlaylistService;
import pl.lodz.p.it.zzpj.spotify.services.interfaces.UserService;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class PlaylistServiceImpl implements PlaylistService {

    @Autowired
    UserService userService;
    private HttpConfiguration httpConfiguration;

    @Override
    public List<Playlist> getPlaylists(OAuth2Authentication details) {
        this.httpConfiguration = new HttpConfiguration(details);

        ResponseEntity<Object> responseEntity = httpConfiguration.getRestTemplate().exchange(
                "https://api.spotify.com/v1/me/playlists/?offset=0&limit=50",
                HttpMethod.GET,
                httpConfiguration.getHttpEntity(),
                Object.class);
        return Playlist.makePlaylistsFromResponseEntity(responseEntity);
    }

    @Override
    public Playlist getPlaylist(OAuth2Authentication details, String playlistID) {
        this.httpConfiguration = new HttpConfiguration(details);
        return new Playlist((LinkedHashMap<String, Object>) httpConfiguration.getRestTemplate().exchange(
                MessageFormat.format("https://api.spotify.com/v1/playlists/{0}", playlistID),
                HttpMethod.GET,
                httpConfiguration.getHttpEntity(),
                Object.class).getBody());
    }

    @Override
    public void generateNewPlaylist(OAuth2Authentication details, String basePlaylistID) {

        Playlist chosenBasePlaylist = this.getPlaylist(details, basePlaylistID);
        List<String> stringList = this.getRecommendationsForPlaylist(details, basePlaylistID);
        String currentUserID = userService.getCurrentUserID(details);
        String createdPlaylistId = this.createNewPlaylist(details, chosenBasePlaylist.getName() + " Reimagined").getId();
        this.addNewTracksBasedOnRecommendation(details, createdPlaylistId, stringList);

    }

    @Override
    public Playlist createNewPlaylist(OAuth2Authentication details, String name) {
        this.httpConfiguration = new HttpConfiguration(details);
        JSONObject parametersMapForNewPlaylist = new JSONObject();
        parametersMapForNewPlaylist.put("name", name);
        parametersMapForNewPlaylist.put("description", "This is a test playlist for ZZPJ project");

        HttpEntity httpEntityForNewPlaylist = new HttpEntity(parametersMapForNewPlaylist.toString(), httpConfiguration.getHttpHeaders());
        ResponseEntity responseEntity = httpConfiguration.getRestTemplate().exchange(
                MessageFormat.format("https://api.spotify.com/v1/users/{0}/playlists", userService.getCurrentUserID(details)),
                HttpMethod.POST,
                httpEntityForNewPlaylist,
                Object.class
        );
        return Playlist.makePlaylistFromResponseEntity(responseEntity);
    }

    @Override
    public List<String> getRecommendationsForPlaylist(OAuth2Authentication details, String basePlaylistID) {
        this.httpConfiguration = new HttpConfiguration(details);
        ResponseEntity<Object> playlistTracks = httpConfiguration.getRestTemplate().exchange(
                MessageFormat.format("https://api.spotify.com/v1/playlists/{0}/tracks", basePlaylistID),
                HttpMethod.GET,
                httpConfiguration.getHttpEntity(),
                Object.class);
        LinkedHashMap playlistTrackBody = (LinkedHashMap) playlistTracks.getBody();
        ArrayList playlistTrackBodyItems = (ArrayList) playlistTrackBody.get("items");

        List<Integer> positions = UtilsToRand.getRandomFromRangeUnreapeated(playlistTrackBodyItems.size(),5);
        List<String> baseTrackNames = new ArrayList<>();

        for (Integer position: positions) {
            baseTrackNames.add((String) ((LinkedHashMap) (
                    (LinkedHashMap) playlistTrackBodyItems.get(position))
                .get("track"))
                .get("id"));
        }
        UriComponentsBuilder newTracksUriBuilder = UriComponentsBuilder
                    .fromHttpUrl("https://api.spotify.com/v1/recommendations")
                    .queryParam("seed_tracks", baseTrackNames);


        ArrayList<Object> tracksForNewPlaylist = (ArrayList<Object>) ((LinkedHashMap) httpConfiguration.getRestTemplate().exchange(
                newTracksUriBuilder.toUriString(),
                HttpMethod.GET,
                httpConfiguration.getHttpEntity(),
                Object.class
        ).getBody()).get("tracks");
        ArrayList<String> stringArrayList = new ArrayList<>();
        for(int i =0 ; i<tracksForNewPlaylist.size() ; i++){
            stringArrayList.add((String) (
                    (LinkedHashMap) tracksForNewPlaylist.get(i)
                    ).get("id")
            );
        }
        return stringArrayList;
    }

    @Override
    public void addNewTracksBasedOnRecommendation(OAuth2Authentication details, String playlistID, List<String> baseTracks) {
        this.httpConfiguration = new HttpConfiguration(details);
        JSONObject parametersMapForAddingTracks = new JSONObject();
        JSONArray trackList = new JSONArray();
        baseTracks.stream().forEach(track -> trackList.appendElement("spotify:track:"+track));
        parametersMapForAddingTracks.put("uris", trackList);

        HttpEntity httpEntityForAddingTracks = new HttpEntity(parametersMapForAddingTracks.toString(), httpConfiguration.getHttpHeaders());

        ResponseEntity<Object> addedTracksResponse =  httpConfiguration.getRestTemplate().exchange(
                MessageFormat.format("https://api.spotify.com/v1/playlists/{0}/tracks", playlistID),
                HttpMethod.POST,
                httpEntityForAddingTracks,
                Object.class
        );
    }

}
