package com.kodigo.alltodo_api.service;

import com.kodigo.alltodo_api.exception.ProjectCollectionException;
import com.kodigo.alltodo_api.exception.TodoCollectionException;
import com.kodigo.alltodo_api.exception.UserCollectionException;
import com.kodigo.alltodo_api.model.ProjectDTO;
import com.kodigo.alltodo_api.model.TodoDTO;
import com.kodigo.alltodo_api.model.UserDTO;
import com.kodigo.alltodo_api.repository.ProjectRepository;
import com.kodigo.alltodo_api.repository.UserRepository;
import com.kodigo.alltodo_api.service.interfaces.ProjectService;
import com.kodigo.alltodo_api.service.interfaces.TodoService;
import com.kodigo.alltodo_api.service.interfaces.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private TodoService todoService;

    @Override
    public void createProject(ProjectDTO project, String idClient) throws ConstraintViolationException, ProjectCollectionException, UserCollectionException {
        UserDTO optionalUserDTO = userService.getUserById(idClient);
        Optional<ProjectDTO> projectOptional = projectRepo.findByName( project.getName(), new ObjectId(idClient) );

       if (projectOptional.isPresent()){
           throw  new ProjectCollectionException( ProjectCollectionException.TodoAlreadyExist() );
       }else{
           project.setCreatedBy(optionalUserDTO);
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
    public void updateProject(String id, ProjectDTO project, String idClient) throws ProjectCollectionException, UserCollectionException {
        UserDTO optionalUserDTO = userService.getUserById(idClient);
        Optional<ProjectDTO> projectWithId = projectRepo.findById(id);
        Optional<ProjectDTO> projectWithSameName = projectRepo.findByName( project.getName(), new ObjectId(idClient) );

        if (projectWithId.isPresent()) {
            if (projectWithSameName.isPresent() && !projectWithSameName.get().getId().equals( id ) ) {
                throw new ProjectCollectionException( ProjectCollectionException.TodoAlreadyExist() );
            }
            ProjectDTO projectToUpdate = projectWithId.get();
            projectToUpdate.setName(project.getName());
            projectToUpdate.setDescription(project.getDescription());
            projectToUpdate.setUpdatedBy(optionalUserDTO);
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
            //todo deletemany todos by this project
            projectRepo.save(project_ToDelete);
        }

    }
}
