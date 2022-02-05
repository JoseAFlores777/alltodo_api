package com.kodigo.alltodo_api.controller;


import com.kodigo.alltodo_api.model.dto.UserDTO;
import com.kodigo.alltodo_api.model.auth.AuthLoginRequest;
import com.kodigo.alltodo_api.model.auth.AuthResponse;
import com.kodigo.alltodo_api.model.auth.AuthSignupRequest;
import com.kodigo.alltodo_api.service.DB.interfaces.DBservices.UserAuthService;
import com.kodigo.alltodo_api.service.DB.interfaces.DBservices.UserService;
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
    public ResponseEntity<?> login(@RequestBody AuthLoginRequest authLoginRequest)  {

        final UserDetails userDetails;
        final String jwt;
        final UserDTO userDTO;
        try {
            authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(authLoginRequest.getEmail(), authLoginRequest.getPassword()) );

           userDetails = userAuthService.loadUserByUsername(authLoginRequest.getEmail());

            jwt = jwtTokenUtil.generateToken(userDetails);

            userDTO = userService.findByEmail(authLoginRequest.getEmail()).get();

        } catch (BadCredentialsException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<AuthResponse>(new AuthResponse(jwt,userDTO), HttpStatus.OK);

    }

    @PostMapping("/auth/signup")
    public ResponseEntity<?> signup(@RequestBody AuthSignupRequest authSignupRequest)  {

        final UserDetails userDetails;
        final String jwt;
        final UserDTO userDTO = new UserDTO();
        final UserDTO userSaved;

        try {
            userDTO.setFirstName(authSignupRequest.getFirstName());
            userDTO.setLastName(authSignupRequest.getLastName());
            userDTO.setGender(authSignupRequest.getGender());
            userDTO.setEmail(authSignupRequest.getEmail());
            userDTO.setPassword(authSignupRequest.getPassword());

            userSaved = userService.createUser(userDTO);

            authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(authSignupRequest.getEmail(), authSignupRequest.getPassword()) );

            userDetails = userAuthService.loadUserByUsername(authSignupRequest.getEmail());

            jwt = jwtTokenUtil.generateToken(userDetails);

        } catch (BadCredentialsException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<AuthResponse>(new AuthResponse(jwt,userSaved), HttpStatus.OK);

    }




}
