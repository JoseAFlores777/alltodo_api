package com.kodigo.alltodo_api.service;

import com.kodigo.alltodo_api.exception.ProjectCollectionException;
import com.kodigo.alltodo_api.exception.TodoCollectionException;
import com.kodigo.alltodo_api.exception.UserCollectionException;
import com.kodigo.alltodo_api.model.ProjectDTO;
import com.kodigo.alltodo_api.model.TodoDTO;
import com.kodigo.alltodo_api.model.UserDTO;
import com.kodigo.alltodo_api.repository.TodoRepository;
import com.kodigo.alltodo_api.repository.UserRepository;
import com.kodigo.alltodo_api.service.interfaces.ProjectService;
import com.kodigo.alltodo_api.service.interfaces.TodoService;
import com.kodigo.alltodo_api.service.interfaces.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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
    @Autowired
    private UserService userService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public void createTodo(TodoDTO todo, String idClient) throws ConstraintViolationException, TodoCollectionException, UserCollectionException {
        UserDTO optionalUserDTO = userService.getUserById(idClient);
       Optional<TodoDTO> todoOptional = todoRepo.findDuplicated( todo.getTitle(), new ObjectId(idClient), new ObjectId(todo.getProject().getId()) );
       if (todoOptional.isPresent()){
           throw  new TodoCollectionException( TodoCollectionException.TodoAlreadyExist() );
       }else{
           todo.setCreatedBy(optionalUserDTO);
           todo.setCreatedAt( new Date( System.currentTimeMillis() ));
           todoRepo.save( todo );
       }
    }

    @Override
    public List<TodoDTO> getAllTodos(String idClient)  {
        List<TodoDTO> todos = todoRepo.findAllUserTodos(new ObjectId(idClient));
        if (todos.size() > 0) {
            return todos;
        }else {
            return new ArrayList<TodoDTO>();
        }
    }

    @Override
    public TodoDTO getTodoById(String id, String idClient) throws TodoCollectionException {
        Optional<TodoDTO> optionalTodo = todoRepo.findUserTodosById( id, new ObjectId(idClient) );
        if (!optionalTodo.isPresent()) {
            throw new TodoCollectionException( TodoCollectionException.NotFoundException( id ) );
        }else {
            return optionalTodo.get();
        }

    }

    @Override
    public void updateTodo(String id, TodoDTO todo, String idClient) throws TodoCollectionException, UserCollectionException, ProjectCollectionException {
        UserDTO optionalUserDTO = userService.getUserById(idClient);
        ProjectDTO optionalProjectDTO = projectService.getProjectById(todo.getProject().getId(), idClient);
        Optional<TodoDTO> todoWithId = todoRepo.findById(id);
        Optional<TodoDTO> todoWithSameTitle = todoRepo.findDuplicated( todo.getTitle(), new ObjectId(idClient), new ObjectId(todo.getProject().getId()) );
        if (todoWithId.isPresent()) {
            if (todoWithSameTitle.isPresent() && !todoWithSameTitle.get().getId().equals( id ) ) {
                throw new TodoCollectionException( TodoCollectionException.TodoAlreadyExist() );
            }
            TodoDTO todoToUpdate = todoWithId.get();
            todoToUpdate.setTitle(todo.getTitle());
            todoToUpdate.setDescription(todo.getDescription());
            todoToUpdate.setCompleted(todo.getCompleted());
            todoToUpdate.setProject(optionalProjectDTO);
            todoToUpdate.setUpdatedBy(optionalUserDTO);
            todoToUpdate.setUpdatedAt( new Date( System.currentTimeMillis() ) );
            todoRepo.save( todoToUpdate );
        }else{
            throw new TodoCollectionException( TodoCollectionException.NotFoundException( id ) );
        }
    }

    @Override
    public void deleteTodo(String id, String idClient) throws TodoCollectionException {
        Optional<TodoDTO> optionalTodo = todoRepo.findUserTodosById(id, new ObjectId(idClient));
        if (!optionalTodo.isPresent()) {
            throw new TodoCollectionException( TodoCollectionException.NotFoundException( id ) );
        }else {
            TodoDTO todo_ToDelete = optionalTodo.get();
            todo_ToDelete.setAvailable(false);
            todoRepo.save(todo_ToDelete);
        }

    }

    @Override
    public void deleteAllByProject(String idProject) throws TodoCollectionException {
        Query query = new Query(Criteria.where("project").is(new ObjectId(idProject)));
        Update update = new Update().set("isAvailable",false);
        mongoTemplate.updateMulti(query,update,TodoDTO.class);
    }


}
