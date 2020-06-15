package pl.lodz.p.it.zzpj.spotify.services;

import net.minidev.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.lodz.p.it.zzpj.spotify.model.Playlist;
import pl.lodz.p.it.zzpj.spotify.model.User;

import java.text.MessageFormat;
import java.util.LinkedHashMap;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public User getCurrentUser(OAuth2Authentication details) {
        String jwt = ((OAuth2AuthenticationDetails)details.getDetails()).getTokenValue();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + jwt);
        HttpEntity httpEntity = new HttpEntity(httpHeaders);
        ResponseEntity responseEntity = restTemplate.exchange("https://api.spotify.com/v1/me",
                HttpMethod.GET,
                httpEntity,
                User.class
        );
        return (User) responseEntity.getBody();
    }

    @Override
    public String getCurrentUserID(OAuth2Authentication details) {
        String jwt = ((OAuth2AuthenticationDetails)details.getDetails()).getTokenValue();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + jwt);
        HttpEntity httpEntity = new HttpEntity(httpHeaders);
        return (String) ((LinkedHashMap) restTemplate.exchange("https://api.spotify.com/v1/me",
                HttpMethod.GET,
                httpEntity,
                Object.class
        ).getBody()).get("id");
    }
}
