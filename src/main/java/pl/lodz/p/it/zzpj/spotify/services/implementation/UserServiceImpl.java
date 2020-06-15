package pl.lodz.p.it.zzpj.spotify.services.implementation;

import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.zzpj.spotify.HttpConfiguration;
import pl.lodz.p.it.zzpj.spotify.services.interfaces.UserService;

import java.util.LinkedHashMap;

@Service
public class UserServiceImpl implements UserService {

    private HttpConfiguration httpConfiguration;

    @Override
    public String getCurrentUserID(OAuth2Authentication details) {
        this.httpConfiguration = new HttpConfiguration(details);
        return (String) ((LinkedHashMap) httpConfiguration.getRestTemplate().exchange(
                "https://api.spotify.com/v1/me",
                HttpMethod.GET,
                httpConfiguration.getHttpEntity(),
                Object.class
        ).getBody()).get("id");
    }
}
