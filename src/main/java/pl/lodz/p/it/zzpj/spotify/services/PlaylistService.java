package pl.lodz.p.it.zzpj.spotify.services;

import org.springframework.security.oauth2.provider.OAuth2Authentication;
import pl.lodz.p.it.zzpj.spotify.model.Playlist;

import java.util.List;

public interface PlaylistService {
    List<Playlist> getPlaylists(OAuth2Authentication details);
    void generateNewPlaylist(OAuth2Authentication details, String basePlaylistID);
    String createNewPlaylist(OAuth2Authentication details, String name, String description);
    List<String> getRecommendationsForPlaylist(OAuth2Authentication details, String basePlaylistID);
    void addNewTracksBasedOnRecommendation(OAuth2Authentication details, String playlistID, List<String> baseTracks);

}
