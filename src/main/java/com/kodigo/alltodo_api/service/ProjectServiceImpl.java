package com.kodigo.alltodo_api.service;

import com.kodigo.alltodo_api.exception.ProjectCollectionException;
import com.kodigo.alltodo_api.exception.TodoCollectionException;
import com.kodigo.alltodo_api.model.ProjectDTO;
import com.kodigo.alltodo_api.model.TodoDTO;
import com.kodigo.alltodo_api.repository.ProjectRepository;
import com.kodigo.alltodo_api.service.interfaces.ProjectService;
import com.kodigo.alltodo_api.service.interfaces.TodoService;
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

    @Override
    public void createProject(ProjectDTO project) throws ConstraintViolationException, ProjectCollectionException {
       Optional<ProjectDTO> projectOptional = projectRepo.findByName( project.getName() );
       if (projectOptional.isPresent()){
           throw  new ProjectCollectionException( ProjectCollectionException.TodoAlreadyExist() );
       }else{
           project.setCreatedAt( new Date( System.currentTimeMillis() ));
           projectRepo.save( project );
       }
    }

    @Override
    public List<ProjectDTO> getAllProjects() {
        List<ProjectDTO> projects = projectRepo.findAll();
        if (projects.size() > 0) {
            return projects;
        }else {
            return new ArrayList<ProjectDTO>();
        }
    }

    @Override
    public ProjectDTO getProjectById(String id) throws ProjectCollectionException {
        Optional<ProjectDTO> optionalProject = projectRepo.findById( id );
        if (!optionalProject.isPresent()) {
            throw new ProjectCollectionException( ProjectCollectionException.NotFoundException( id ) );
        }else {
            return optionalProject.get();
        }

    }

    @Override
    public void updateProject(String id, ProjectDTO project) throws ProjectCollectionException {
        Optional<ProjectDTO> projectWithId = projectRepo.findById(id);
        Optional<ProjectDTO> projectWithSameName = projectRepo.findByName( project.getName() );
        if (projectWithId.isPresent()) {
            if (projectWithSameName.isPresent() && !projectWithSameName.get().getId().equals( id ) ) {
                throw new ProjectCollectionException( ProjectCollectionException.TodoAlreadyExist() );
            }
            ProjectDTO projectToUpdate = projectWithId.get();
            projectToUpdate.setName(project.getName());
            projectToUpdate.setDescription(project.getDescription());
            projectToUpdate.setFinishAt(project.getFinishAt());
            projectToUpdate.setUpdateAt( new Date( System.currentTimeMillis() ) );
            projectRepo.save( projectToUpdate );
        }else{
            throw new ProjectCollectionException( ProjectCollectionException.NotFoundException( id ) );
        }
    }

    @Override
    public void deleteProject(String id) throws ProjectCollectionException {
        Optional<ProjectDTO> optionalProject = projectRepo.findById(id);
        if (!optionalProject.isPresent()) {
            throw new ProjectCollectionException( ProjectCollectionException.NotFoundException( id ) );
        }else {
            projectRepo.deleteById(id);
        }

    }
}
