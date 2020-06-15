package pl.lodz.p.it.zzpj.spotify;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.client.RestTemplate;

public @Data class HttpConfiguration {
    private String jwt;
    private RestTemplate restTemplate;
    private HttpHeaders httpHeaders;
    private HttpEntity httpEntity;

    public HttpConfiguration(OAuth2Authentication details) {
        this.jwt = ((OAuth2AuthenticationDetails) details.getDetails()).getTokenValue();
        this.restTemplate = new RestTemplate();
        this.httpHeaders = new HttpHeaders();
        this.httpHeaders.add("Authorization", "Bearer " + jwt);
        this.httpEntity = new HttpEntity(httpHeaders);
    }
}
