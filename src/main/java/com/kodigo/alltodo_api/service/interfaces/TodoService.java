package com.kodigo.alltodo_api.service.interfaces;

import com.kodigo.alltodo_api.exception.ProjectCollectionException;
import com.kodigo.alltodo_api.exception.TodoCollectionException;
import com.kodigo.alltodo_api.model.TodoDTO;

import javax.validation.ConstraintViolationException;
import java.util.List;

public interface TodoService {

     void createTodo(TodoDTO todo)  throws ConstraintViolationException,TodoCollectionException;

     List<TodoDTO> getAllTodos();

     TodoDTO getTodoById(String id) throws TodoCollectionException;

     void updateTodo(String id, TodoDTO todo) throws TodoCollectionException;

     void deleteTodo(String id) throws TodoCollectionException;
}
