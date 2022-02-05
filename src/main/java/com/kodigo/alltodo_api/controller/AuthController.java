package com.kodigo.alltodo_api.controller;


import com.kodigo.alltodo_api.model.UserDTO;
import com.kodigo.alltodo_api.model.auth.AuthLoginReq;
import com.kodigo.alltodo_api.model.auth.AuthRes;
import com.kodigo.alltodo_api.model.auth.AuthSignupReq;
import com.kodigo.alltodo_api.service.interfaces.UserAuthService;
import com.kodigo.alltodo_api.service.interfaces.UserService;
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
    private UserAuthService userAuthService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @PostMapping("/auth")
    public ResponseEntity<?> login(@RequestBody AuthLoginReq authLoginReq)  {

        final UserDetails userDetails;
        final String jwt;
        final UserDTO userDTO;
        try {
            authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(authLoginReq.getEmail(), authLoginReq.getPassword()) );

           userDetails = userAuthService.loadUserByUsername(authLoginReq.getEmail());

            jwt = jwtTokenUtil.generateToken(userDetails);

            userDTO = userService.findByEmail(authLoginReq.getEmail()).get();

        } catch (BadCredentialsException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<AuthRes>(new AuthRes(jwt,userDTO), HttpStatus.OK);

    }

    @PostMapping("/auth/signup")
    public ResponseEntity<?> signup(@RequestBody AuthSignupReq authSignupReq)  {

        final UserDetails userDetails;
        final String jwt;
        final UserDTO userDTO = new UserDTO();
        final UserDTO userSaved;

        try {
            userDTO.setFirstName(authSignupReq.getFirstName());
            userDTO.setLastName(authSignupReq.getLastName());
            userDTO.setGender(authSignupReq.getGender());
            userDTO.setEmail(authSignupReq.getEmail());
            userDTO.setPassword(authSignupReq.getPassword());

            userSaved = userService.createUser(userDTO);

            authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(authSignupReq.getEmail(), authSignupReq.getPassword()) );

            userDetails = userAuthService.loadUserByUsername(authSignupReq.getEmail());

            jwt = jwtTokenUtil.generateToken(userDetails);

        } catch (BadCredentialsException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<AuthRes>(new AuthRes(jwt,userSaved), HttpStatus.OK);

    }




}
