package pl.lodz.p.it.zzpj.spotify.services.interfaces;

import org.springframework.security.oauth2.provider.OAuth2Authentication;
import pl.lodz.p.it.zzpj.spotify.model.Playlist;

import java.util.List;

public interface PlaylistService {
    Playlist getPlaylist(OAuth2Authentication details, String plylistID);
    List<Playlist> getPlaylists(OAuth2Authentication details);
    void generateNewPlaylist(OAuth2Authentication details, String basePlaylistID);
    void unfollowPlaylist(OAuth2Authentication details, String playListID);
    Playlist createNewPlaylist(OAuth2Authentication details, String name);
    List<String> getRecommendationsForPlaylist(OAuth2Authentication details, String basePlaylistID);
    void addNewTracksBasedOnRecommendation(OAuth2Authentication details, String playlistID, List<String> baseTracks);
}
