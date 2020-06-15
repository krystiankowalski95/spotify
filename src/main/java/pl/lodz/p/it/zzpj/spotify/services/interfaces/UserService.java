package pl.lodz.p.it.zzpj.spotify.services.interfaces;

import org.springframework.security.oauth2.provider.OAuth2Authentication;
import pl.lodz.p.it.zzpj.spotify.model.User;

public interface UserService {
    String getCurrentUserID(OAuth2Authentication details);
    User getCurrentUser(OAuth2Authentication details);

}
