package com.kodigo.alltodo_api.service.interfaces;

import com.kodigo.alltodo_api.exception.ProjectCollectionException;
import com.kodigo.alltodo_api.exception.TodoCollectionException;
import com.kodigo.alltodo_api.exception.UserCollectionException;
import com.kodigo.alltodo_api.model.ProjectDTO;
import com.kodigo.alltodo_api.model.TodoDTO;

import javax.validation.ConstraintViolationException;
import java.util.List;

public interface ProjectService {

     void createProject(ProjectDTO project, String idClient) throws ConstraintViolationException, ProjectCollectionException, UserCollectionException;

     List<ProjectDTO> getAllProjects(String idClient);

     ProjectDTO getProjectById(String id, String idClient) throws ProjectCollectionException;

     void updateProject(String id, ProjectDTO project, String idClient) throws ProjectCollectionException, UserCollectionException;

     void deleteProject(String id, String idClient) throws ProjectCollectionException, TodoCollectionException;

     void deleteAllByUser(String idUser) throws ProjectCollectionException;
}
