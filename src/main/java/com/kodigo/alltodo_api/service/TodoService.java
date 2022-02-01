package com.kodigo.alltodo_api.service;

import com.kodigo.alltodo_api.exception.TodoCollectionException;
import com.kodigo.alltodo_api.model.TodoDTO;

import javax.validation.ConstraintViolationException;
import java.util.List;

public interface TodoService {

    public void createTodo(TodoDTO todo)  throws ConstraintViolationException,TodoCollectionException;

    public List<TodoDTO> getAllTodos();

    public TodoDTO getTodoById(String id) throws TodoCollectionException;

    public void updateTodo(String id, TodoDTO todo) throws TodoCollectionException;

    public void deleteTodo(String id) throws TodoCollectionException;
}
