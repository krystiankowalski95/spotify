package pl.lodz.p.it.zzpj.spotify.services.interfaces;

import org.springframework.security.oauth2.provider.OAuth2Authentication;

public interface UserService {
    String getCurrentUserID(OAuth2Authentication details);

}
