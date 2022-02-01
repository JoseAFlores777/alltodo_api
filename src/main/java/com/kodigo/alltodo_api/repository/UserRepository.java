package com.kodigo.alltodo_api.repository;

import com.kodigo.alltodo_api.model.TodoDTO;
import com.kodigo.alltodo_api.model.UserDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository <UserDTO, String> {

    @Query("{'email': ?0}")
    Optional<UserDTO> findByEmail(String title );

}
