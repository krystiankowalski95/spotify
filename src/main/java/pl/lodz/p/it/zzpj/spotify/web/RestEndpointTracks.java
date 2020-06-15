package pl.lodz.p.it.zzpj.spotify.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import pl.lodz.p.it.zzpj.spotify.model.Tracks;
import pl.lodz.p.it.zzpj.spotify.services.TrackService;

@RestController
public class RestEndpointTracks {

    @Autowired
    TrackService trackService;



    @GetMapping("/track/")
    public ModelAndView getTracks(OAuth2Authentication details, @RequestParam("name")String phrase) {
        Tracks tracks = trackService.getTracks(details, phrase);


        return new ModelAndView("tracksView", "tracks", tracks);
    }


}
