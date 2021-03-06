package com.kodigo.alltodo_api.service.DB.implementations.mongoDB.DBservice;

import com.kodigo.alltodo_api.exception.ProjectCollectionException;
import com.kodigo.alltodo_api.exception.TodoCollectionException;
import com.kodigo.alltodo_api.exception.UserCollectionException;
import com.kodigo.alltodo_api.model.dto.TodoDTO;
import com.kodigo.alltodo_api.model.dto.UserDTO;
import com.kodigo.alltodo_api.service.DB.implementations.mongoDB.repository.TodoRepository_MongoDB;
import com.kodigo.alltodo_api.service.DB.interfaces.DBservices.TodoService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
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
    private TodoRepository_MongoDB todoRepo;

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public void createTodo(TodoDTO todo, UserDTO Client) throws ConstraintViolationException, TodoCollectionException, UserCollectionException {

        ObjectId projectId = todo.getProject() == null ? null : new ObjectId(todo.getProject().getId());

       Optional<TodoDTO> todoOptional = todoRepo.findDuplicated( todo.getTitle(), new ObjectId(Client.getId()), projectId );

       if (todoOptional.isPresent()){
           throw  new TodoCollectionException( TodoCollectionException.TodoAlreadyExist() );
       }else{
           todo.setCreatedBy(Client);
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
        Optional<TodoDTO> optionalTodo = todoRepo.findUserTodosById( new ObjectId(id), new ObjectId(idClient) );
        if (!optionalTodo.isPresent()) {
            throw new TodoCollectionException( TodoCollectionException.NotFoundException( id ) );
        }else {
            return optionalTodo.get();
        }

    }

    @Override
    public void updateTodo( String id, TodoDTO todo, UserDTO Client) throws TodoCollectionException, UserCollectionException, ProjectCollectionException {

        Optional<TodoDTO> todoWithId = todoRepo.findByID_custom(new ObjectId(id));


        if (todoWithId.isPresent()) {

            TodoDTO todoToUpdate = todoWithId.get();
            todoToUpdate.setTitle(todo.getTitle());
            todoToUpdate.setDescription(todo.getDescription());
            todoToUpdate.setExpirationDate(todo.getExpirationDate());
            //todoToUpdate.setCompleted(todo.getCompleted());
            if (todo.getProject() != null){
                todoToUpdate.setProject(todo.getProject());
            }
            todoToUpdate.setUpdatedBy(Client);
            todoToUpdate.setUpdatedAt( new Date( System.currentTimeMillis() ) );
            todoRepo.save( todoToUpdate );
        }else{
            throw new TodoCollectionException( TodoCollectionException.NotFoundException( id ) );
        }
    }


    @Override
    public void updateTodoStatus( String id, TodoDTO todo, UserDTO Client) throws TodoCollectionException, UserCollectionException, ProjectCollectionException {

        Optional<TodoDTO> todoWithId = todoRepo.findByID_custom(new ObjectId(id));

        if (todoWithId.isPresent()) {

            TodoDTO todoToUpdate = todoWithId.get();
            todoToUpdate.setCompleted(todo.getCompleted());
            if (todo.getProject() != null){
                todoToUpdate.setProject(todo.getProject());
            }
            todoToUpdate.setUpdatedBy(Client);
            todoToUpdate.setUpdatedAt( new Date( System.currentTimeMillis() ) );
            todoRepo.save( todoToUpdate );
        }else{
            throw new TodoCollectionException( TodoCollectionException.NotFoundException( id ) );
        }
    }

    @Override
    public void deleteTodo(String id, String idClient) throws TodoCollectionException {
        Optional<TodoDTO> optionalTodo = todoRepo.findUserTodosById(new ObjectId(id), new ObjectId(idClient));
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
