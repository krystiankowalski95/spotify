package pl.lodz.p.it.zzpj.spotify.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import pl.lodz.p.it.zzpj.spotify.model.User;
import pl.lodz.p.it.zzpj.spotify.services.UserService;

import java.security.Principal;

@RestController
public class RestUserEndpoint {


    @Autowired
    private UserService userService;

    @GetMapping("/user")
    public Principal getUser(Principal principal) {
        return principal;
    }


    @GetMapping("/userDetails")
    public Object getUserDetails(OAuth2Authentication details) {

        User currentUser = userService.getCurrentUser(details);

        return new ModelAndView("userView", "currentUser", currentUser);
    }

}
