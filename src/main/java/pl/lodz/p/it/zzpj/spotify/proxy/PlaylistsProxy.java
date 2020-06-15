package pl.lodz.p.it.zzpj.spotify.proxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Repository;
import pl.lodz.p.it.zzpj.spotify.model.Playlist;
import pl.lodz.p.it.zzpj.spotify.services.interfaces.PlaylistService;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PlaylistsProxy {

    @Autowired
    private PlaylistService playlistService;

    List<Playlist> playlists = new ArrayList<>();

    private boolean changed = true;
    private int limit = 0;


    public List<Playlist> getPlaylists(OAuth2Authentication details) {
        if(changed){
            this.playlists = playlistService.getPlaylists(details);
            this.changed = false;
            this.limit = 0;
        } else {
            this.limit++;
            if(limit == 3){
                this.playlists = playlistService.getPlaylists(details);
                this.limit = 0;
            }
        }
        return playlists;
    }

    public void newPlaylists(){
        this.changed = true;
    }
}
