package com.kodigo.alltodo_api.model.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthSignupRequest {

    private String firstName;
    private String lastName;
    private String gender;
    private String email;
    private String password;

}
