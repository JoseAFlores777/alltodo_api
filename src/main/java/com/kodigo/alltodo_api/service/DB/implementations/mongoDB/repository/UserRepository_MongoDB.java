package com.kodigo.alltodo_api.service.DB.implementations.mongoDB.repository;

import com.kodigo.alltodo_api.model.dto.UserDTO;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository_MongoDB extends MongoRepository <UserDTO, String> {

    @Query("{'email': ?0, isAvailable: true}")
    Optional<UserDTO> findByEmail(String email );

    @Query("{'email':?0, '_id':{ $ne : ?1 } , 'isAvailable': true, }")
    Optional<UserDTO> findByEmailWithException(String email, ObjectId userIdException );

    @Query("{'id': ?0, isAvailable: true}")
    Optional<UserDTO> findUserById(String id );

}
