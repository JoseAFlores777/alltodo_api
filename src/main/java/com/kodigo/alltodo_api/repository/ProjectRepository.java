package com.kodigo.alltodo_api.repository;

import com.kodigo.alltodo_api.model.ProjectDTO;
import com.kodigo.alltodo_api.model.TodoDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends MongoRepository <ProjectDTO, String> {

    @Query("{'name': ?0}")
    Optional<ProjectDTO> findByName(String name );

}
