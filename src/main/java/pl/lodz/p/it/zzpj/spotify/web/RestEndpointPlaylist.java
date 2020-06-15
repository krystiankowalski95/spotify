package pl.lodz.p.it.zzpj.spotify.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import pl.lodz.p.it.zzpj.spotify.proxy.PlaylistsProxy;
import pl.lodz.p.it.zzpj.spotify.services.PlaylistService;

@RestController
public class RestEndpointPlaylist {

    @Autowired
    PlaylistService playlistService;
    @Autowired
    PlaylistsProxy playlistsProxy;

    //Get User's list of Playlist
    @GetMapping("/playlists")
    public ModelAndView getPlaylists(OAuth2Authentication details) {
        return new ModelAndView("playlistsView", "playlist", playlistsProxy.getPlaylists(details));
    }

    @GetMapping("/newPlaylist/{playlistID}")
    public ModelAndView generateNewPlaylist(OAuth2Authentication details, @PathVariable String playlistID){
        playlistService.generateNewPlaylist(details, playlistID);
        playlistsProxy.newPlaylists();

        return new ModelAndView("playlistsView", "playlist", playlistsProxy.getPlaylists(details));
    }

    @GetMapping("/addNew/")
    public ModelAndView addPlaylists(OAuth2Authentication details, @RequestParam(value = "name")String playlistName) {
        playlistService.createNewPlaylist(details,playlistName);

        playlistsProxy.newPlaylists();

        return new ModelAndView("playlistsView", "playlist", playlistsProxy.getPlaylists(details));
    }

}
