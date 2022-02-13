package com.kodigo.alltodo_api.controller;

import com.kodigo.alltodo_api.exception.ProjectCollectionException;
import com.kodigo.alltodo_api.exception.TodoCollectionException;
import com.kodigo.alltodo_api.exception.UserCollectionException;
import com.kodigo.alltodo_api.model.dto.TodoDTO;
import com.kodigo.alltodo_api.model.dto.UserDTO;
import com.kodigo.alltodo_api.model.httpResponse.CommonResponse;
import com.kodigo.alltodo_api.model.mail.MailRequest;
import com.kodigo.alltodo_api.service.DB.interfaces.DBservices.ProjectService;
import com.kodigo.alltodo_api.service.DB.interfaces.DBservices.TodoService;
import com.kodigo.alltodo_api.service.DB.interfaces.DBservices.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.List;


@RestController
public class TodoController {

    @Autowired
    private TodoService todoService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private EmailMessengerController emailMessengerController;


    @GetMapping("/todos")
    public ResponseEntity<?> getAll(@RequestAttribute("uid") String id) throws UserCollectionException {
        List<TodoDTO> todos = todoService.getAllTodos(id);
        return new ResponseEntity<>( todos, HttpStatus.OK );
    }

    @PostMapping("/todos")
    public ResponseEntity<?> create(@RequestBody TodoDTO todo, @RequestAttribute("uid")String id) {
        try {
            MailRequest request = new MailRequest();
            UserDTO optionalUserDTO = userService.getUserById(id);

            if (todo.getProject() != null) {
                todo.setProject(projectService.getProjectById(todo.getProject().getId(), id));
            }

            todoService.createTodo(todo, optionalUserDTO);
            request.setTo(optionalUserDTO.getEmail());
            request.setEntity(todo);
            request.setSubject("New Todo");
            emailMessengerController.newTodoEmail(request);
            return new ResponseEntity<TodoDTO>(todo, HttpStatus.OK);

        } catch (ConstraintViolationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (TodoCollectionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (UserCollectionException | ProjectCollectionException  e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/todos/{id}")
    public ResponseEntity<?> getById( @PathVariable("id") String id, @RequestAttribute("uid") String uid ){
        try {
            return new ResponseEntity<>(todoService.getTodoById(id, uid), HttpStatus.OK);
        } catch (TodoCollectionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/todos/{id}")
    public ResponseEntity<?> updateById( @PathVariable("id") String id, @RequestBody TodoDTO todo, @RequestAttribute("uid") String uid  ){
        try {
            UserDTO optionalUserDTO = userService.getUserById(uid);
            if (todo.getProject() != null){

                projectService.getProjectById(todo.getProject().getId(), uid);
            }
            todoService.updateTodo( id, todo, optionalUserDTO );
            return new ResponseEntity<>(new CommonResponse("200","Update Todo with id "+id), HttpStatus.OK);
        } catch (ConstraintViolationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (TodoCollectionException | UserCollectionException | ProjectCollectionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/status/todo/{id}")
    public ResponseEntity<?> updateStatusById( @PathVariable("id") String id, @RequestBody TodoDTO todo, @RequestAttribute("uid") String uid  ){
        try {
            UserDTO optionalUserDTO = userService.getUserById(uid);

            todoService.updateTodoStatus( id, todo, optionalUserDTO );
            return new ResponseEntity<>(new CommonResponse("200","Update Todo with id "+id), HttpStatus.OK);
        } catch (ConstraintViolationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (TodoCollectionException | UserCollectionException | ProjectCollectionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/todos/{id}")
    public ResponseEntity<?> deleteById( @PathVariable("id") String id, @RequestAttribute("uid") String uid ){
        try {
            todoService.deleteTodo(id, uid);
            return new ResponseEntity<>(new CommonResponse("200","Successfully deleted with id "+id), HttpStatus.OK );
        }catch ( TodoCollectionException e ){
            return new ResponseEntity<>( e.getMessage(), HttpStatus.NOT_FOUND );
        }
    }

}
