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
    public ResponseEntity<?> login(@RequestBody AuthReq authReq) throws Exception {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authReq.getEmail(), authReq.getPassword())
            );
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }

        final UserDetails userDetails = userAuthServiceImpl.loadUserByUsername(authReq.getEmail());

        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return new ResponseEntity<AuthRes>(new AuthRes(jwt), HttpStatus.OK);

    }

}
