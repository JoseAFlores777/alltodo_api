package com.kodigo.alltodo_api.service.interfaces;

import com.kodigo.alltodo_api.exception.ProjectCollectionException;
import com.kodigo.alltodo_api.exception.UserCollectionException;
import com.kodigo.alltodo_api.model.UserDTO;

import javax.validation.ConstraintViolationException;

public interface UserService {

     void createUser(UserDTO user)  throws ConstraintViolationException, UserCollectionException;

     UserDTO getUserById(String id) throws UserCollectionException;

     void updateUser(String id, UserDTO user) throws UserCollectionException;

     void deleteUser(String id) throws UserCollectionException, ProjectCollectionException;

     boolean ifUserExists(String id);
}
