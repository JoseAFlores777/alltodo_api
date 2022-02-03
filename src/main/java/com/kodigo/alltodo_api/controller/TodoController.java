package com.kodigo.alltodo_api.controller;

import com.kodigo.alltodo_api.exception.TodoCollectionException;
import com.kodigo.alltodo_api.model.TodoDTO;
import com.kodigo.alltodo_api.service.interfaces.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.kodigo.alltodo_api.repository.TodoRepository;
import javax.validation.ConstraintViolationException;
import java.util.List;


@RestController
public class TodoController {

    @Autowired
    private TodoService todoService;

    @GetMapping("/todos")
    public ResponseEntity<?> getAll(){
        List<TodoDTO> todos = todoService.getAllTodos();
        return new ResponseEntity<>( todos, todos.size() > 0 ? HttpStatus.OK : HttpStatus.NOT_FOUND );
    }

    @PostMapping("/todos")
    public ResponseEntity<?> create(@RequestBody TodoDTO todo, @RequestAttribute("uid")String id) {
        try {
            todoService.createTodo(todo, id);
            return new ResponseEntity<TodoDTO>(todo, HttpStatus.OK);

        } catch (ConstraintViolationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (TodoCollectionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/todos/{id}")
    public ResponseEntity<?> getById( @PathVariable("id") String id ){
        try {
            return new ResponseEntity<>(todoService.getTodoById(id), HttpStatus.OK);
        } catch (TodoCollectionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/todos/{id}")
    public ResponseEntity<?> updateById( @PathVariable("id") String id, @RequestBody TodoDTO todo ){
        try {
            todoService.updateTodo( id, todo );
            return new ResponseEntity<>("Update Todo with id "+id, HttpStatus.OK);
        } catch (ConstraintViolationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (TodoCollectionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/todos/{id}")
    public ResponseEntity<?> deleteById( @PathVariable("id") String id ){
        try {
            todoService.deleteTodo(id);
            return new ResponseEntity<>("Successfully deleted with id "+id, HttpStatus.OK );
        }catch ( TodoCollectionException e ){
            return new ResponseEntity<>( e.getMessage(), HttpStatus.NOT_FOUND );
        }
    }

}
