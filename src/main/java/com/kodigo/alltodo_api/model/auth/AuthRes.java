package com.kodigo.alltodo_api.model.auth;

import com.kodigo.alltodo_api.model.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthRes {

    private final String jwt;
    private final UserDTO user;

}
