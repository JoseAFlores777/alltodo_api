package com.kodigo.alltodo_api.service;

import com.kodigo.alltodo_api.exception.TodoCollectionException;
import com.kodigo.alltodo_api.model.TodoDTO;
import com.kodigo.alltodo_api.repository.TodoRepository;
import com.kodigo.alltodo_api.service.interfaces.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TodoServiceImpl implements TodoService {

    @Autowired
    private TodoRepository todoRepo;

    @Override
    public void createTodo(TodoDTO todo) throws ConstraintViolationException,TodoCollectionException {
       Optional<TodoDTO> todoOptional = todoRepo.findByTitle( todo.getTitle() );
       if (todoOptional.isPresent()){
           throw  new TodoCollectionException( TodoCollectionException.TodoAlreadyExist() );
       }else{

           todo.setCreatedAt( new Date( System.currentTimeMillis() ));
           todoRepo.save( todo );
       }
    }

    @Override
    public List<TodoDTO> getAllTodos() {
        List<TodoDTO> todos = todoRepo.findAll();
        if (todos.size() > 0) {
            return todos;
        }else {
            return new ArrayList<TodoDTO>();
        }
    }

    @Override
    public TodoDTO getTodoById(String id) throws TodoCollectionException {
        Optional<TodoDTO> optionalTodo = todoRepo.findById( id );
        if (!optionalTodo.isPresent()) {
            throw new TodoCollectionException( TodoCollectionException.NotFoundException( id ) );
        }else {
            return optionalTodo.get();
        }

    }

    @Override
    public void updateTodo(String id, TodoDTO todo) throws TodoCollectionException {
        Optional<TodoDTO> todoWithId = todoRepo.findById(id);
        Optional<TodoDTO> todoWithSameTitle = todoRepo.findByTitle( todo.getTitle() );
        if (todoWithId.isPresent()) {
            if (todoWithSameTitle.isPresent() && !todoWithSameTitle.get().getId().equals( id ) ) {
                throw new TodoCollectionException( TodoCollectionException.TodoAlreadyExist() );
            }
            TodoDTO todoToUpdate = todoWithId.get();
            todoToUpdate.setTitle(todo.getTitle());
            todoToUpdate.setDescription(todo.getDescription());
            todoToUpdate.setCompleted(todo.getCompleted());
            todoToUpdate.setUpdateAt( new Date( System.currentTimeMillis() ) );
            todoRepo.save( todoToUpdate );
        }else{
            throw new TodoCollectionException( TodoCollectionException.NotFoundException( id ) );
        }
    }

    @Override
    public void deleteTodo(String id) throws TodoCollectionException {
        Optional<TodoDTO> optionalTodo = todoRepo.findById(id);
        if (!optionalTodo.isPresent()) {
            throw new TodoCollectionException( TodoCollectionException.NotFoundException( id ) );
        }else {
            todoRepo.deleteById(id);
        }

    }
}
