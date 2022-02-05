package com.kodigo.alltodo_api.model.auth;

import com.kodigo.alltodo_api.model.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {

    private final String jwt;
    private final UserDTO user;

}
