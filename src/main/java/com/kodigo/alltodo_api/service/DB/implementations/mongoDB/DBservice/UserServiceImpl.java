package com.kodigo.alltodo_api.service.DB.implementations.mongoDB.DBservice;

import com.kodigo.alltodo_api.exception.ProjectCollectionException;
import com.kodigo.alltodo_api.exception.UserCollectionException;
import com.kodigo.alltodo_api.model.dto.UserDTO;
import com.kodigo.alltodo_api.service.DB.implementations.mongoDB.repository.UserRepository_MongoDB;

import com.kodigo.alltodo_api.service.DB.interfaces.DBservices.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository_MongoDB userRepo;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDTO createUser(UserDTO user) throws ConstraintViolationException, UserCollectionException {
       Optional<UserDTO> userOptional = userRepo.findByEmail( user.getEmail() );

       if (userOptional.isPresent()){
           throw  new UserCollectionException( UserCollectionException.UserAlreadyExist() );
       }else{
           user.setPassword(this.passwordEncoder.encode(user.getPassword()));
           user.setCreatedAt( new Date( System.currentTimeMillis() ));
           userRepo.save( user );
           return userRepo.findByEmail( user.getEmail() ).get() ;
       }
    }


    @Override
    public UserDTO getUserById(String id) throws UserCollectionException {
        Optional<UserDTO> optionalUser = userRepo.findUserById( id );

        if (!optionalUser.isPresent()) {
            throw new UserCollectionException( UserCollectionException.NotFoundException( id ) );
        }else {
            return optionalUser.get();
        }

    }

    @Override
    public void updateUser(String id, UserDTO user) throws UserCollectionException {
        Optional<UserDTO> userWithId = userRepo.findUserById(id);
        Optional<UserDTO> userWithSameEmail = userRepo.findByEmail( user.getEmail() );

        if (userWithId.isPresent()) {
            if (userWithSameEmail.isPresent() && !userWithSameEmail.get().getId().equals( id ) ) {
                throw new UserCollectionException( UserCollectionException.UserAlreadyExist() );
            }
            UserDTO userToUpdate = userWithId.get();
            userToUpdate.setFirstName(user.getFirstName());
            userToUpdate.setLastName(user.getLastName());
            userToUpdate.setGender(user.getGender());
            userToUpdate.setAvatarImg(user.getAvatarImg());
            userToUpdate.setEmail(user.getEmail());
            userToUpdate.setUpdatedAt( new Date( System.currentTimeMillis() ) );
            userRepo.save( userToUpdate );
        }else{
            throw new UserCollectionException( UserCollectionException.NotFoundException( id ) );
        }
    }

    @Override
    public void deleteUser(String id) throws UserCollectionException, ProjectCollectionException {
        Optional<UserDTO> optionalUser = userRepo.findUserById(id);
        if (!optionalUser.isPresent()) {
            throw new UserCollectionException( UserCollectionException.NotFoundException( id ) );
        }else {
            UserDTO userToDelete = optionalUser.get();
            userToDelete.setAvailable(false);
            userRepo.save(userToDelete);
        }

    }

    @Override
    public Optional<UserDTO> findByEmail(String email) throws UserCollectionException {
        Optional<UserDTO> optionalUser = userRepo.findByEmail(email);
        if (!optionalUser.isPresent()) {
            throw new UserCollectionException( UserCollectionException.NotFoundException( email ) );
        }
        return optionalUser;
    }


    @Override
    public void verifyUserEmail(String id) throws UserCollectionException {
        Optional<UserDTO> optionalUser = userRepo.findUserById(id);
        if (!optionalUser.isPresent()) {
            throw new UserCollectionException( UserCollectionException.NotFoundException( id ) );
        }else {
            UserDTO userToVerify = optionalUser.get();
            userToVerify.setVerifiedEmail(true);
            userRepo.save(userToVerify);
        }
    }

    @Override
    public void updateUserPassword(String id, String newPwd) throws UserCollectionException {
        Optional<UserDTO> optionalUser = userRepo.findUserById(id);
        if (!optionalUser.isPresent()) {
            throw new UserCollectionException( UserCollectionException.NotFoundException( id ) );
        }else {
            UserDTO userToUpdate = optionalUser.get();
            userToUpdate.setPassword(this.passwordEncoder.encode(newPwd));
            userToUpdate.setUpdatedAt( new Date( System.currentTimeMillis() ) );
            userRepo.save( userToUpdate );
        }
    }

    @Override
    public UserDTO isEmailVerified(String email) throws UserCollectionException {
        Optional<UserDTO> optionalUser = userRepo.findByEmail(email);
        if (!optionalUser.isPresent()) {
            throw new UserCollectionException( UserCollectionException.NotFoundException( email ) );
        }

        if (!optionalUser.get().isVerifiedEmail()){
            throw new UserCollectionException( UserCollectionException.EmailNotVerified(email) );
        }

        return optionalUser.get();
    }


}
