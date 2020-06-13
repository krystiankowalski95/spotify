package pl.lodz.p.it.zzpj.spotify.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.GsonJsonParser;
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
import pl.lodz.p.it.zzpj.spotify.model.Item;
import pl.lodz.p.it.zzpj.spotify.model.Tracks;
import pl.lodz.p.it.zzpj.spotify.proxy.PlaylistsProxy;
import pl.lodz.p.it.zzpj.spotify.proxy.TracksProxy;
import pl.lodz.p.it.zzpj.spotify.services.PlaylistService;
import pl.lodz.p.it.zzpj.spotify.services.TrackService;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
public class RestEndpointTracks {

    @Autowired
    TrackService trackService;
    @Autowired
    TracksProxy tracksProxy;



    @GetMapping("/track/")
    public Object getTracks(OAuth2Authentication details, @RequestParam("name")String phrase) {
        String jwt = ((OAuth2AuthenticationDetails)details.getDetails()).getTokenValue();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + jwt);
        HttpEntity httpEntity = new HttpEntity(httpHeaders);


        LinkedHashMap response = ((LinkedHashMap)restTemplate.exchange("https://api.spotify.com/v1/search?q="+phrase+"&limit=3&type=track",
                HttpMethod.GET,
                httpEntity,Object.class).getBody());


        LinkedHashMap items = (LinkedHashMap) response.get("tracks");
        LinkedHashMap item = (LinkedHashMap) items.get("items");
        System.out.println(item);
        return response.get("tracks");

//        List<Integer> possitions = TempUtils.getRandomFromRangeUnreapeated(playlistTrackBodyItems.size(),5);
//        List<String> baseTrackNames = new ArrayList<>();
//
//        for (Integer possition: possitions) {
//            baseTrackNames.add((String) ((LinkedHashMap) ((LinkedHashMap) playlistTrackBodyItems.get(possition))
//                    .get("track")).get("id"));
//        }

//        return Item.makeItemsFromResponseEntity(response);
    }


}
