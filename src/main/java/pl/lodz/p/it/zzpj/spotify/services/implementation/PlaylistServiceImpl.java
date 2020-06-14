package pl.lodz.p.it.zzpj.spotify.services.implementation;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.lodz.p.it.zzpj.spotify.UtilsToRand;
import pl.lodz.p.it.zzpj.spotify.model.Playlist;
import pl.lodz.p.it.zzpj.spotify.services.AbstractService;
import pl.lodz.p.it.zzpj.spotify.services.interfaces.PlaylistService;
import pl.lodz.p.it.zzpj.spotify.services.interfaces.UserService;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class PlaylistServiceImpl extends AbstractService implements PlaylistService {

    @Autowired
    UserService userService;

    @Override
    public List<Playlist> getPlaylists(OAuth2Authentication details) {
        super.init(details);

        ResponseEntity<Object> responseEntity = this.getRestTemplate().exchange(
                "https://api.spotify.com/v1/me/playlists/?offset=0&limit=50",
                HttpMethod.GET,
                this.getHttpEntity(),
                Object.class);
        return Playlist.makePlaylistsFromResponseEntity(responseEntity);
    }

    @Override
    public Playlist getPlaylist(OAuth2Authentication details, String playlistID) {
        super.init(details);
        return new Playlist((LinkedHashMap<String, Object>) this.getRestTemplate().exchange(
                MessageFormat.format("https://api.spotify.com/v1/playlists/{0}", playlistID),
                HttpMethod.GET,
                this.getHttpEntity(),
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
        super.init(details);
        JSONObject parametersMapForNewPlaylist = new JSONObject();
        parametersMapForNewPlaylist.put("name", name);
        parametersMapForNewPlaylist.put("description", "This is a test playlist for ZZPJ project");

        HttpEntity httpEntityForNewPlaylist = new HttpEntity(parametersMapForNewPlaylist.toString(), this.getHttpHeaders());
        ResponseEntity responseEntity = this.getRestTemplate().exchange(
                MessageFormat.format("https://api.spotify.com/v1/users/{0}/playlists", userService.getCurrentUserID(details)),
                HttpMethod.POST,
                httpEntityForNewPlaylist,
                Object.class
        );
        return Playlist.makePlaylistFromResponseEntity(responseEntity);
    }

    @Override
    public List<String> getRecommendationsForPlaylist(OAuth2Authentication details, String basePlaylistID) {
        super.init(details);
        ResponseEntity<Object> playlistTracks = this.getRestTemplate().exchange(
                MessageFormat.format("https://api.spotify.com/v1/playlists/{0}/tracks", basePlaylistID),
                HttpMethod.GET,
                this.getHttpEntity(),
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


        ArrayList<Object> tracksForNewPlaylist = (ArrayList<Object>) ((LinkedHashMap) this.getRestTemplate().exchange(
                newTracksUriBuilder.toUriString(),
                HttpMethod.GET,
                this.getHttpEntity(),
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
        super.init(details);
        JSONObject parametersMapForAddingTracks = new JSONObject();
        JSONArray trackList = new JSONArray();
        baseTracks.stream().forEach(track -> trackList.appendElement("spotify:track:"+track));
        parametersMapForAddingTracks.put("uris", trackList);

        HttpEntity httpEntityForAddingTracks = new HttpEntity(parametersMapForAddingTracks.toString(), this.getHttpHeaders());

        ResponseEntity<Object> addedTracksResponse =  this.getRestTemplate().exchange(
                MessageFormat.format("https://api.spotify.com/v1/playlists/{0}/tracks", playlistID),
                HttpMethod.POST,
                httpEntityForAddingTracks,
                Object.class
        );
    }

}
