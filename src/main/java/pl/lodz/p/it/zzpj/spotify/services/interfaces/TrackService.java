package pl.lodz.p.it.zzpj.spotify.services.interfaces;

import org.springframework.security.oauth2.provider.OAuth2Authentication;
import pl.lodz.p.it.zzpj.spotify.model.Tracks;

public interface TrackService {
    Tracks getTracks(OAuth2Authentication details, String phrase);
}
