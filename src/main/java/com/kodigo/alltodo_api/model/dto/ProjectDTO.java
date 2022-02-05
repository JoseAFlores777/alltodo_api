package com.kodigo.alltodo_api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "projects")
public class ProjectDTO {

    @Id
    private String id;

    @NotNull( message = "The name cannot be null")
    @NotBlank( message = "The Project must have at least one character in the name")
    private String name;

    @NotNull( message = "The description cannot be null")
    @NotBlank( message = "The Todo must have at least one character in the description")
    private String description;

    @DocumentReference
    private UserDTO createdBy;

    @DocumentReference
    private UserDTO updatedBy;

    private Date createdAt;

    private Date updatedAt;

    @NotNull( message = "The status of User cannot be null")
    private boolean isAvailable = true;

}
