package pl.lodz.p.it.zzpj.spotify.proxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Repository;
import pl.lodz.p.it.zzpj.spotify.model.Item;
import pl.lodz.p.it.zzpj.spotify.services.interfaces.TrackService;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TracksProxy {

    @Autowired
    private TrackService trackService;

    List<Item> tracks = new ArrayList<>();

    private boolean changed = true;
    private int limit = 0;


    public List<Item> getTracks(OAuth2Authentication details,String phrase) {
        if(changed){
            this.tracks = trackService.getTracks(details,phrase);
            this.changed = false;
            this.limit = 0;
        } else {
            this.limit++;
            if(limit == 3){
                this.tracks = trackService.getTracks(details,phrase);
                this.limit = 0;
            }
        }
        return tracks;
    }

    public void newTrack(){
        this.changed = true;
    }
}
