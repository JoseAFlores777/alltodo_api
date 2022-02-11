package com.kodigo.alltodo_api.controller;

import com.kodigo.alltodo_api.model.auth.CustomUserDetails;
import com.kodigo.alltodo_api.model.dto.UserDTO;
import com.kodigo.alltodo_api.service.DB.interfaces.DBservices.UserService;
import com.kodigo.alltodo_api.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

/*    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;*/

    @RequestMapping("/")
    public String login() {
        return "home.html";
    }

/*    @RequestMapping("/auth/reset-password/{token}")
    public String resetPasswordForm(@PathVariable("token") String token, Model model){
        try {

            String jwt = token.substring(7);

            String userId = jwtUtil.extractUserId(jwt);

            UserDTO userDTO = userService.getUserById(userId);
            CustomUserDetails customUserDetails = new CustomUserDetails(userDTO);

            if (!jwtUtil.validateToken(jwt,customUserDetails)) {
                throw new BadCredentialsException("invalid jwt");
            }

            model.addAttribute("id",userDTO.getId());

        return "resetPassword.html";

        }catch (Exception e){
            e.getStackTrace();
        return e.getMessage();
        }
        // TODO: 7/2/22
    }*/

}
