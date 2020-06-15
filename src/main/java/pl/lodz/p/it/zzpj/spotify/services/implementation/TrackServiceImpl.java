package pl.lodz.p.it.zzpj.spotify.services.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import pl.lodz.p.it.zzpj.spotify.model.Item;
import pl.lodz.p.it.zzpj.spotify.HttpConfiguration;
import pl.lodz.p.it.zzpj.spotify.services.interfaces.TrackService;
import pl.lodz.p.it.zzpj.spotify.services.interfaces.UserService;

import java.util.List;


@Service
public class TrackServiceImpl implements TrackService {

    @Autowired
    UserService userService;
    private HttpConfiguration httpConfiguration;

    @Override
    public List<Item> getTracks(OAuth2Authentication details, @RequestParam("searchPhrase") String phrase) {
        this.httpConfiguration = new HttpConfiguration(details);
        
        ResponseEntity<Object> response = httpConfiguration.getRestTemplate().exchange(
                "https://api.spotify.com/v1/search?q="+phrase+"&limit=3&type=track",
                HttpMethod.GET,
                httpConfiguration.getHttpEntity(),
                Object.class);
        return Item.makeItemsFromResponseEntity(response);
    }

}
