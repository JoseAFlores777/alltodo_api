package com.kodigo.alltodo_api.service.DB.implementations.mongoDB.DBservice;

import com.kodigo.alltodo_api.model.auth.CustomUserDetails;
import com.kodigo.alltodo_api.model.dto.UserDTO;
import com.kodigo.alltodo_api.service.DB.implementations.mongoDB.repository.UserRepository_MongoDB;
import com.kodigo.alltodo_api.service.DB.interfaces.DBservices.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserAuthServiceImpl implements UserAuthService {

    @Autowired
    private UserRepository_MongoDB userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<UserDTO> optionalUser = userRepo.findByEmail( email );
        if (!optionalUser.isPresent()) {
            throw new UsernameNotFoundException("The User doesn't exist");
        }

        return new CustomUserDetails(optionalUser.get());
        //return new User(optionalUser.get().getEmail(),optionalUser.get().getPassword(),new ArrayList<>());
    }
}
