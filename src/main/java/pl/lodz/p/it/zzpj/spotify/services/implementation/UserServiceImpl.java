package pl.lodz.p.it.zzpj.spotify.services.implementation;

import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.zzpj.spotify.services.AbstractService;
import pl.lodz.p.it.zzpj.spotify.services.interfaces.UserService;

import java.util.LinkedHashMap;

@Service
public class UserServiceImpl extends AbstractService implements UserService {

    @Override
    public String getCurrentUserID(OAuth2Authentication details) {
        super.init(details);
        return (String) ((LinkedHashMap) this.getRestTemplate().exchange(
                "https://api.spotify.com/v1/me",
                HttpMethod.GET,
                this.getHttpEntity(),
                Object.class
        ).getBody()).get("id");
    }
}
