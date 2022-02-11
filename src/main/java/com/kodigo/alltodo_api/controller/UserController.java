package com.kodigo.alltodo_api.controller;

import com.kodigo.alltodo_api.exception.ProjectCollectionException;
import com.kodigo.alltodo_api.exception.UserCollectionException;
import com.kodigo.alltodo_api.model.dto.UserDTO;
import com.kodigo.alltodo_api.model.httpResponse.CommonResponse;
import com.kodigo.alltodo_api.service.DB.interfaces.DBservices.ProjectService;
import com.kodigo.alltodo_api.service.DB.interfaces.DBservices.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;


@RestController
public class UserController {


    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;


    @PostMapping("/users")
    public ResponseEntity<?> create(@RequestBody UserDTO user) {
        try {
            userService.createUser(user);
            return new ResponseEntity<UserDTO>(user, HttpStatus.OK);

        } catch (ConstraintViolationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (UserCollectionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getById( @PathVariable("id") String id ){
        try {
            return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
        } catch (UserCollectionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateById( @PathVariable("id") String id, @RequestBody UserDTO user ){
        try {

            userService.updateUser( id, user );

            return new ResponseEntity<>(new CommonResponse("true","Update User with id "+id), HttpStatus.OK);
        } catch (ConstraintViolationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (UserCollectionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteById( @PathVariable("id") String id ){
        try {
            userService.deleteUser(id);
            projectService.deleteAllByUser(id);
            return new ResponseEntity<>("Successfully deleted with id "+id, HttpStatus.OK );
        }catch (UserCollectionException | ProjectCollectionException e ){
            return new ResponseEntity<>( e.getMessage(), HttpStatus.NOT_FOUND );
        }
    }

}
