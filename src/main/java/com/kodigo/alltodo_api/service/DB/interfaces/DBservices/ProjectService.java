package com.kodigo.alltodo_api.service.DB.interfaces.DBservices;

import com.kodigo.alltodo_api.exception.ProjectCollectionException;
import com.kodigo.alltodo_api.exception.TodoCollectionException;
import com.kodigo.alltodo_api.exception.UserCollectionException;
import com.kodigo.alltodo_api.model.dto.ProjectDTO;
import com.kodigo.alltodo_api.model.dto.UserDTO;

import javax.validation.ConstraintViolationException;
import java.util.List;

public interface ProjectService {

     void createProject(ProjectDTO project, UserDTO idClient) throws ConstraintViolationException, ProjectCollectionException, UserCollectionException;

     List<ProjectDTO> getAllProjects(String idClient);

     ProjectDTO getProjectById(String id, String idClient) throws ProjectCollectionException;

     void updateProject(String id, ProjectDTO project, UserDTO idClient) throws ProjectCollectionException, UserCollectionException;

     void deleteProject(String id, String idClient) throws ProjectCollectionException, TodoCollectionException;

     void deleteAllByUser(String idUser) throws ProjectCollectionException;
}
