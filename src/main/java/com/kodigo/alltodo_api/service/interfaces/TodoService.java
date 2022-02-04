package com.kodigo.alltodo_api.service.interfaces;

import com.kodigo.alltodo_api.exception.ProjectCollectionException;
import com.kodigo.alltodo_api.exception.TodoCollectionException;
import com.kodigo.alltodo_api.exception.UserCollectionException;
import com.kodigo.alltodo_api.model.TodoDTO;
import com.kodigo.alltodo_api.model.UserDTO;

import javax.validation.ConstraintViolationException;
import java.util.List;

public interface TodoService {

     void createTodo(TodoDTO todo, UserDTO idClient) throws ConstraintViolationException, TodoCollectionException, UserCollectionException;

     List<TodoDTO> getAllTodos(String idClient) throws UserCollectionException;

     TodoDTO getTodoById(String id, String idClient) throws TodoCollectionException;

     void updateTodo(String id, TodoDTO todo, UserDTO idClient) throws TodoCollectionException, UserCollectionException, ProjectCollectionException;

     void deleteTodo(String id, String idClient) throws TodoCollectionException;

     void deleteAllByProject(String idProject) throws TodoCollectionException;
}
