package com.kodigo.alltodo_api.service.DB.implementations.mongoDB.DBservice;

import com.kodigo.alltodo_api.exception.ProjectCollectionException;
import com.kodigo.alltodo_api.exception.TodoCollectionException;
import com.kodigo.alltodo_api.exception.UserCollectionException;
import com.kodigo.alltodo_api.model.dto.ProjectDTO;
import com.kodigo.alltodo_api.model.dto.UserDTO;
import com.kodigo.alltodo_api.service.DB.implementations.mongoDB.repository.ProjectRepository_MongoDB;
import com.kodigo.alltodo_api.service.DB.interfaces.DBservices.ProjectService;
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
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository_MongoDB projectRepo;


    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public void createProject(ProjectDTO project, UserDTO Client) throws ConstraintViolationException, ProjectCollectionException, UserCollectionException {

        Optional<ProjectDTO> projectOptional = projectRepo.findByName( project.getName(), new ObjectId(Client.getId()) );

       if (projectOptional.isPresent()){
           throw  new ProjectCollectionException( ProjectCollectionException.TodoAlreadyExist() );
       }else{
           project.setCreatedBy(Client);
           project.setCreatedAt( new Date( System.currentTimeMillis() ));
           projectRepo.save( project );
       }
    }

    @Override
    public List<ProjectDTO> getAllProjects(String idClient) {
        List<ProjectDTO> projects = projectRepo.findAllUserProyects(new ObjectId(idClient));
        if (projects.size() > 0) {
            return projects;
        }else {
            return new ArrayList<ProjectDTO>();
        }
    }

    @Override
    public ProjectDTO getProjectById(String id, String idClient) throws ProjectCollectionException {
        Optional<ProjectDTO> optionalProject = projectRepo.findUserProyectsById( id, new ObjectId(idClient) );
        if (!optionalProject.isPresent()) {
            throw new ProjectCollectionException( ProjectCollectionException.NotFoundException( id ) );
        }else {
            return optionalProject.get();
        }

    }

    @Override
    public void updateProject(String id, ProjectDTO project, UserDTO Client) throws ProjectCollectionException, UserCollectionException {

        Optional<ProjectDTO> projectWithId = projectRepo.findById(id);
        Optional<ProjectDTO> projectWithSameName = projectRepo.findByName( project.getName(), new ObjectId(Client.getId()) );

        if (projectWithId.isPresent()) {
            if (projectWithSameName.isPresent() && !projectWithSameName.get().getId().equals( id ) ) {
                throw new ProjectCollectionException( ProjectCollectionException.TodoAlreadyExist() );
            }
            ProjectDTO projectToUpdate = projectWithId.get();
            projectToUpdate.setName(project.getName());
            projectToUpdate.setDescription(project.getDescription());
            projectToUpdate.setUpdatedBy(Client);
            projectToUpdate.setUpdatedAt( new Date( System.currentTimeMillis() ) );
            projectRepo.save( projectToUpdate );
        }else{
            throw new ProjectCollectionException( ProjectCollectionException.NotFoundException( id ) );
        }
    }

    @Override
    public void deleteProject(String id, String idClient) throws ProjectCollectionException, TodoCollectionException {
        Optional<ProjectDTO> optionalProject = projectRepo.findUserProyectsById( id, new ObjectId(idClient) );
        if (!optionalProject.isPresent()) {
            throw new ProjectCollectionException( ProjectCollectionException.NotFoundException( id ) );
        }else {
            ProjectDTO project_ToDelete = optionalProject.get();
            project_ToDelete.setAvailable(false);

            projectRepo.save(project_ToDelete);
        }

    }

    @Override
    public void deleteAllByUser(String idUser) throws ProjectCollectionException {
        Query query = new Query(Criteria.where("createdBy").is(new ObjectId(idUser)));
        Update update = new Update().set("isAvailable",false);
        mongoTemplate.updateMulti(query,update, ProjectDTO.class);
    }


}
