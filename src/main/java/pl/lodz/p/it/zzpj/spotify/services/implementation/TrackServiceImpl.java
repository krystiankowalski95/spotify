package pl.lodz.p.it.zzpj.spotify.services.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import pl.lodz.p.it.zzpj.spotify.HttpConfiguration;
import pl.lodz.p.it.zzpj.spotify.model.SearchingTracks;
import pl.lodz.p.it.zzpj.spotify.model.Tracks;
import pl.lodz.p.it.zzpj.spotify.services.interfaces.TrackService;
import pl.lodz.p.it.zzpj.spotify.services.interfaces.UserService;


@Service
public class TrackServiceImpl implements TrackService {

    @Autowired
    UserService userService;
    private HttpConfiguration httpConfiguration;

    @Override
    public Tracks getTracks(OAuth2Authentication details, @RequestParam("searchPhrase")String phrase) {
        this.httpConfiguration = new HttpConfiguration(details);

        ResponseEntity<SearchingTracks> response = httpConfiguration.getRestTemplate().exchange(
                "https://api.spotify.com/v1/search?q="+phrase+"&limit=15&type=track",
                HttpMethod.GET,
                httpConfiguration.getHttpEntity(),
                SearchingTracks.class);

        return response.getBody().getTracks();
    }

}
