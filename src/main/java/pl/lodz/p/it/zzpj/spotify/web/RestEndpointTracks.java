package pl.lodz.p.it.zzpj.spotify.web;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import pl.lodz.p.it.zzpj.spotify.model.Tracks;

@RestController
public class RestEndpointTracks {

    @GetMapping("/track/")
    public ModelAndView getTracks(OAuth2Authentication details, @RequestParam("searchPhrase")String phrase) {
        String jwt = ((OAuth2AuthenticationDetails)details.getDetails()).getTokenValue();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + jwt);
        HttpEntity httpEntity = new HttpEntity(httpHeaders);


        ResponseEntity<Object> response = restTemplate.exchange("https://api.spotify.com/v1/search?q="+phrase+"&limit=50&type=track",
                HttpMethod.GET,
                httpEntity,Object.class);
        return new ModelAndView("tracksView", "tracks", Tracks.makeTracksFromResponseEntity(response));
//        return response;
    }
}
