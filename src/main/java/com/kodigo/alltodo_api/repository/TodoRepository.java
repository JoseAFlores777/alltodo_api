package com.kodigo.alltodo_api.repository;

import com.kodigo.alltodo_api.model.TodoDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TodoRepository extends MongoRepository <TodoDTO, String> {

    @Query("{'title': ?0}")
    Optional<TodoDTO> findByTitle(String title );

}
