package pl.lodz.p.it.zzpj.spotify.web;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class RestUserEndpoint {

    @GetMapping("/user")
    public Principal getUser(Principal principal) {
        return principal;
    }
}
