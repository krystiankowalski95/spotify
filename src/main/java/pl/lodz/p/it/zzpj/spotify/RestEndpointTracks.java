package pl.lodz.p.it.zzpj.spotify;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import pl.lodz.p.it.zzpj.spotify.model.Tracks;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RestEndpointTracks {

    @GetMapping("/track/{searchPhrase}")
    public ResponseEntity<Tracks[]> getTracks(OAuth2Authentication details, @PathVariable("searchPhrase")String phrase) {
        String jwt = ((OAuth2AuthenticationDetails)details.getDetails()).getTokenValue();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + jwt);
        HttpEntity httpEntity = new HttpEntity(httpHeaders);


        ResponseEntity<Tracks[]> tracks = restTemplate.exchange("https://api.spotify.com/v1/search?q="+phrase+"&limit=50&type=track",
                HttpMethod.GET,
                httpEntity,Tracks[].class);

        return tracks;
        /*return restTemplate.exchange("https://api.spotify.com/v1/search?q="+phrase+"&limit=50&type=track",
                HttpMethod.GET,
                httpEntity,Object.class);*/
    }
}
