package com.kodigo.alltodo_api.controller;


import com.kodigo.alltodo_api.exception.ProjectCollectionException;
import com.kodigo.alltodo_api.exception.UserCollectionException;
import com.kodigo.alltodo_api.model.dto.UserDTO;
import com.kodigo.alltodo_api.model.auth.AuthLoginRequest;
import com.kodigo.alltodo_api.model.auth.AuthResponse;
import com.kodigo.alltodo_api.model.auth.AuthSignupRequest;
import com.kodigo.alltodo_api.service.DB.interfaces.DBservices.UserAuthService;
import com.kodigo.alltodo_api.service.DB.interfaces.DBservices.UserService;
import com.kodigo.alltodo_api.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
    //@RequestMapping( value = "/auth" ,method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    //@CrossOrigin(origins = "http://localhost:4200")
    //@PostMapping(value = "/auth",  produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody AuthLoginRequest authLoginRequest)  {
        System.out.println(authLoginRequest.getEmail());
        final UserDetails userDetails;
        final String jwt;
        final UserDTO userDTO;
        try {
            authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(authLoginRequest.getEmail(), authLoginRequest.getPassword()) );

           userDetails = userAuthService.loadUserByUsername(authLoginRequest.getEmail());

            jwt = jwtTokenUtil.generateToken(userDetails);

            userDTO = userService.findByEmail(authLoginRequest.getEmail()).get();

        } catch (BadCredentialsException e){
            System.out.println('1');
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            System.out.println('2');
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
        System.out.println(jwt);
        return new ResponseEntity<>(new AuthResponse(jwt,userDTO), HttpStatus.OK);

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

    @GetMapping("auth/users/find/{email}")
    public ResponseEntity<?> isEmailExists( @PathVariable("email") String email){
        try {
                userService.findByEmail (email);

            return new ResponseEntity<>(true, HttpStatus.OK );
        }catch (UserCollectionException e ){
            return new ResponseEntity<>( false, HttpStatus.OK );
        }
    }


    @GetMapping("auth/users/find/{email}/{userIdException}")
    public ResponseEntity<?> isEmailExistsWithException( @PathVariable("email") String email,  @PathVariable("userIdException") String userIdException ){
        try {

                userService.findByEmailWithException(email, userIdException);

            return new ResponseEntity<>(true, HttpStatus.OK );
        }catch (UserCollectionException e ){
            return new ResponseEntity<>( false, HttpStatus.OK );
        }
    }

    @GetMapping("auth/users/is-verified/{email}")
    public ResponseEntity<?> isEmailVerified( @PathVariable("email") String email ){
        final UserDetails userDetails;
        String jwt = null;
        UserDTO userDTO = null;

        try {

                userDTO = userService.isEmailVerified(email);
                userDetails = userAuthService.loadUserByUsername(userDTO.getEmail());
                jwt = jwtTokenUtil.generateToken(userDetails);


        } catch (BadCredentialsException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<AuthResponse>(new AuthResponse(jwt, userDTO), HttpStatus.OK);

    }


    @GetMapping("/auth/renew")
    public ResponseEntity<?> renewToken( @RequestHeader("Authorization") String token ){
        final UserDetails userDetails;
        String jwt = null;
        UserDTO userDTO = null;
        final String email;
        try {

            token = token.substring(7);

            email = jwtTokenUtil.extractUserEmail(token);

            userDetails = userAuthService.loadUserByUsername(email);

            if (jwtTokenUtil.validateToken(token,userDetails)){
                userDTO = userService.findByEmail(email).get();
                jwt = jwtTokenUtil.generateToken(userDetails);
            }


        } catch (BadCredentialsException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<AuthResponse>(new AuthResponse(jwt, userDTO), HttpStatus.OK);

    }



}
