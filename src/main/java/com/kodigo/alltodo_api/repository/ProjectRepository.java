package com.kodigo.alltodo_api.repository;

import com.kodigo.alltodo_api.model.ProjectDTO;
import com.kodigo.alltodo_api.model.TodoDTO;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends MongoRepository <ProjectDTO, String> {


    @Query("{'name': ?0, 'createdBy': ?1, isAvailable: true }")
    Optional<ProjectDTO> findByName(String name, ObjectId createdBy );

    //@Query("{'createdBy': ?0, isAvailable: true },{ 'name': 1 }")
    @Query(value = "{'createdBy': ?0, isAvailable: true }")
    List<ProjectDTO> findAllUserProyects(ObjectId createdBy);

    @Query("{'id': ?0, 'createdBy': ?1, isAvailable: true }")
    Optional<ProjectDTO> findUserProyectsById(String id, ObjectId createdBy);





}
