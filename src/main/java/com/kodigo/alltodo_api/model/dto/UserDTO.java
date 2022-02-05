package com.kodigo.alltodo_api.model.dto;

import com.kodigo.alltodo_api.model.enums.Gender;
import com.kodigo.alltodo_api.validators.TypeGenderConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
public class UserDTO {

    @Id
    private String id;

    @NotNull( message = "The firstName cannot be null")
    @NotBlank( message = "The firstName must have at least one character")
    private String firstName;

    @NotNull( message = "The lastName cannot be null")
    @NotBlank( message = "The lastName must have at least one character")
    private String lastName;

    @TypeGenderConstraint(message = "The Gender must be Female or Male")
    @NotNull( message = "The gender cannot be null")
    private String gender;

    @NotNull( message = "The email cannot be null")
    @Email(message = "invalid email format")
    private String email;

    private boolean verifiedEmail = false;

    @NotNull( message = "The password cannot be null")
    @NotBlank( message = "The password must have at least one character")
    private String password;

    private String avatarImg;

    private String GoogleAvatarImg;

    private boolean googleSignIn;

    private Date createdAt;

    private Date updatedAt;

    @NotNull( message = "The status of User cannot be null")
    private boolean isAvailable = true;
}
