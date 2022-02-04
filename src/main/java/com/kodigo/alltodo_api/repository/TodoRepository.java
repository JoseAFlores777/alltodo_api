package com.kodigo.alltodo_api.repository;

import com.kodigo.alltodo_api.model.ProjectDTO;
import com.kodigo.alltodo_api.model.TodoDTO;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TodoRepository extends MongoRepository <TodoDTO, String> {

    @Query("{'title': ?0}")
    Optional<TodoDTO> findByTitle(String title );

    @Query("{'title': ?0, 'createdBy': ?1, 'project': ?2 }")
    Optional<TodoDTO> findDuplicated(String title, ObjectId createdBy, ObjectId idProject );

    @Query("{'createdBy': ?0, isAvailable: true  }")
    List<TodoDTO> findAllUserTodos(ObjectId createdBy);

    @Query("{'id': ?0, 'createdBy': ?1, isAvailable: true }")
    Optional<TodoDTO> findUserTodosById(String id, ObjectId createdBy);

    @Query("{'project': ?0 }")
    void deleteAllByProject( ObjectId idProject );



}
