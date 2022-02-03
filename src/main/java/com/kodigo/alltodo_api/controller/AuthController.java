package com.kodigo.alltodo_api.controller;


import com.kodigo.alltodo_api.model.auth.AuthReq;
import com.kodigo.alltodo_api.model.auth.AuthRes;
import com.kodigo.alltodo_api.service.UserAuthServiceImpl;
import com.kodigo.alltodo_api.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserAuthServiceImpl userAuthServiceImpl;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @PostMapping("/auth")
    public ResponseEntity<?> login(@RequestBody AuthReq authReq)  {

        final UserDetails userDetails;
        final String jwt;

        try {
            authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(authReq.getEmail(), authReq.getPassword()) );

           userDetails = userAuthServiceImpl.loadUserByUsername(authReq.getEmail());

            jwt = jwtTokenUtil.generateToken(userDetails);

        } catch (BadCredentialsException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<AuthRes>(new AuthRes(jwt), HttpStatus.OK);

    }

}
