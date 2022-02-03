package com.kodigo.alltodo_api.repository;

import com.kodigo.alltodo_api.model.TodoDTO;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TodoRepository extends MongoRepository <TodoDTO, String> {

    @Query("{'title': ?0}")
    Optional<TodoDTO> findByTitle(String title );

    @Query("{'title': ?0, 'asignatedTo.id': ?1, 'project.id': ?2 }")
    Optional<TodoDTO> findDuplicated(String title, String asignedToId, String idProject );

    @Query("{'project': ?0 }")
    void deleteAllByProject( ObjectId idProject );



}
