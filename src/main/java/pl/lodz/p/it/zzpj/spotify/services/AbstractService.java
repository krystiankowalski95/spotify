package pl.lodz.p.it.zzpj.spotify.services;

import lombok.Data;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.client.RestTemplate;

public @Data
abstract class AbstractService {
    private String jwt;
    private RestTemplate restTemplate;
    private HttpHeaders httpHeaders;
    private HttpEntity httpEntity;

    public void init(OAuth2Authentication details) {
        this.jwt = ((OAuth2AuthenticationDetails) details.getDetails()).getTokenValue();
        this.restTemplate = new RestTemplate();
        this.httpHeaders = new HttpHeaders();
        this.httpHeaders.add("Authorization", "Bearer " + jwt);
        this.httpEntity = new HttpEntity(httpHeaders);
    }
}
