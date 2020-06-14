package pl.lodz.p.it.zzpj.spotify.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import pl.lodz.p.it.zzpj.spotify.model.Item;

import java.util.List;


@Service
public class TrackServiceImpl implements TrackService {

    @Autowired
    UserService userService;

    @Override
    public List<Item> getTracks(OAuth2Authentication details, @RequestParam("searchPhrase")String phrase) {
        String jwt = ((OAuth2AuthenticationDetails)details.getDetails()).getTokenValue();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + jwt);
        HttpEntity httpEntity = new HttpEntity(httpHeaders);


        ResponseEntity<Object> response = restTemplate.exchange("https://api.spotify.com/v1/search?q="+phrase+"&limit=3&type=track",
                HttpMethod.GET,
                httpEntity,Object.class);
        return Item.makeItemsFromResponseEntity(response);
    }

}
