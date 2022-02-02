package com.kodigo.alltodo_api.service;

import com.kodigo.alltodo_api.exception.UserCollectionException;
import com.kodigo.alltodo_api.model.UserDTO;
import com.kodigo.alltodo_api.repository.UserRepository;
import com.kodigo.alltodo_api.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void createUser(UserDTO user) throws ConstraintViolationException, UserCollectionException {
       Optional<UserDTO> userOptional = userRepo.findByEmail( user.getEmail() );
       if (userOptional.isPresent()){
           throw  new UserCollectionException( UserCollectionException.UserAlreadyExist() );
       }else{
           user.setPassword(this.passwordEncoder.encode(user.getPassword()));
           user.setCreatedAt( new Date( System.currentTimeMillis() ));
           userRepo.save( user );
       }
    }


    @Override
    public UserDTO getUserById(String id) throws UserCollectionException {
        Optional<UserDTO> optionalUser = userRepo.findById( id );
        if (!optionalUser.isPresent()) {
            throw new UserCollectionException( UserCollectionException.NotFoundException( id ) );
        }else {
            return optionalUser.get();
        }

    }

    @Override
    public void updateUser(String id, UserDTO user) throws UserCollectionException {
        Optional<UserDTO> userWithId = userRepo.findById(id);
        Optional<UserDTO> userWithSameEmail = userRepo.findByEmail( user.getEmail() );
        if (userWithId.isPresent()) {
            if (userWithSameEmail.isPresent() && !userWithSameEmail.get().getId().equals( id ) ) {
                throw new UserCollectionException( UserCollectionException.UserAlreadyExist() );
            }
            UserDTO userToUpdate = userWithId.get();
            userToUpdate.setFirstName(user.getFirstName());
            userToUpdate.setLastName(user.getLastName());
            userToUpdate.setGender(user.getGender());
            userToUpdate.setEmail(user.getEmail());
            userToUpdate.setUpdateAt( new Date( System.currentTimeMillis() ) );
            userRepo.save( userToUpdate );
        }else{
            throw new UserCollectionException( UserCollectionException.NotFoundException( id ) );
        }
    }

    @Override
    public void deleteUser(String id) throws UserCollectionException {
        Optional<UserDTO> optionalUser = userRepo.findById(id);
        if (!optionalUser.isPresent()) {
            throw new UserCollectionException( UserCollectionException.NotFoundException( id ) );
        }else {
            UserDTO userToDelete = optionalUser.get();
            userToDelete.setAvailable(false);
            userRepo.save(userToDelete);
        }

    }
}
