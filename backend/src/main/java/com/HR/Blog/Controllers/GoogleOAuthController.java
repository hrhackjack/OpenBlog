package com.HR.Blog.Controllers;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.HR.Blog.Entities.User;
import com.HR.Blog.Repositories.UserRepo;
import org.springframework.web.util.UriComponentsBuilder.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class GoogleOAuthController {

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/oauth/google")
    public RedirectView googleOAuth() {
        String url = "https://accounts.google.com/o/oauth2/auth?client_id={client_id}&redirect_uri={redirect_uri}&scope={scope}&response_type=code";
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("client_id", "{your_google_client_id}");
        uriVariables.put("redirect_uri", "{your_redirect_uri}");
        uriVariables.put("scope", "openid email profile");
        return new RedirectView(url, true, true, true);//.expand(uriVariables);
    }

    @GetMapping("/oauth/google/callback")
    public String googleOAuthCallback(@RequestParam("code") String code, Model model) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded");
        String requestBody = "code=" + code + "&client_id={your_google_client_id}&client_secret={your_google_client_secret}&redirect_uri={your_redirect_uri}&grant_type=authorization_code";
        HttpEntity<String> entity = new HttpEntity<String>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.exchange("https://oauth2.googleapis.com/token", HttpMethod.POST, entity, Map.class);
        String accessToken = (String) response.getBody().get("access_token");
        HttpHeaders userInfoHeaders = new HttpHeaders();
        userInfoHeaders.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> userInfoEntity = new HttpEntity<String>(userInfoHeaders);
        ResponseEntity<Map> userInfoResponse = restTemplate.exchange("https://www.googleapis.com/oauth2/v3/userinfo", HttpMethod.GET, userInfoEntity, Map.class);
        Map<String, String> userInfo = userInfoResponse.getBody();
        String email = userInfo.get("email");
        String name = userInfo.get("given_name") + " " + userInfo.get("family_name");
        User user = userRepo.findByEmail(email).get();
        if (user == null) {
            user = new User();
            user.setName(name);
            user.setEmail(email);
//            user.setAvatarPath(userInfo.get("picture"));
//            user.setAuthMethod(AuthMethod.GOOGLE);
            userRepo.save(user);
//        } else if (user.getAuthMethod() == AuthMethod.EMAIL) {
//            throw new RuntimeException("This account uses email & password for authentication");
//        } else {
            user.setName(name);
//            user.setAvatarPath(userInfo.get("picture"));
            userRepo.save(user);
        }
        model.addAttribute("user", user);
        return "redirect:/home";
    }

}

