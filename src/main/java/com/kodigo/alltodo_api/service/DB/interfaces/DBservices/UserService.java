package com.kodigo.alltodo_api.service.DB.interfaces.DBservices;

import com.kodigo.alltodo_api.exception.ProjectCollectionException;
import com.kodigo.alltodo_api.exception.UserCollectionException;
import com.kodigo.alltodo_api.model.dto.UserDTO;

import javax.validation.ConstraintViolationException;
import java.util.Optional;

public interface UserService {

     UserDTO createUser(UserDTO user)  throws ConstraintViolationException, UserCollectionException;

     UserDTO getUserById(String id) throws UserCollectionException;

     void updateUser(String id, UserDTO user) throws UserCollectionException;

     void deleteUser(String id) throws UserCollectionException, ProjectCollectionException;

     Optional<UserDTO> findByEmail(String email );

     void verifyUserEmail(String id) throws UserCollectionException;
}
