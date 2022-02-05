package com.kodigo.alltodo_api.service;

import com.kodigo.alltodo_api.exception.UserCollectionException;
import com.kodigo.alltodo_api.model.CustomUserDetails;
import com.kodigo.alltodo_api.model.UserDTO;
import com.kodigo.alltodo_api.repository.UserRepository;
import com.kodigo.alltodo_api.service.interfaces.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserAuthServiceImpl implements UserAuthService {

    @Autowired
    private UserRepository userRepo;

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
