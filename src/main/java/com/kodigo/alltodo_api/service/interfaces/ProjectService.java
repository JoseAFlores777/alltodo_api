package com.kodigo.alltodo_api.service.interfaces;

import com.kodigo.alltodo_api.exception.ProjectCollectionException;
import com.kodigo.alltodo_api.exception.TodoCollectionException;
import com.kodigo.alltodo_api.model.ProjectDTO;
import com.kodigo.alltodo_api.model.TodoDTO;

import javax.validation.ConstraintViolationException;
import java.util.List;

public interface ProjectService {

     void createProject(ProjectDTO project)  throws ConstraintViolationException, ProjectCollectionException;

     List<ProjectDTO> getAllProjects();

     ProjectDTO getProjectById(String id) throws ProjectCollectionException;

     void updateProject(String id, ProjectDTO project) throws ProjectCollectionException;

     void deleteProject(String id) throws ProjectCollectionException;
}
