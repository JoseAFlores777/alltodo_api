package com.kodigo.alltodo_api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "todos")
public class TodoDTO {

    @Id
    private String id;

    @NotNull( message = "The title cannot be null")
    @NotBlank( message = "The Todo must have at least one character in the title")
    private String title;

    @NotNull( message = "The description cannot be null")
    @NotBlank( message = "The Todo must have at least one character in the description")
    private String description;

    @NotNull( message = "The status of completed cannot be null")
    private Boolean completed;


    @DocumentReference
    private UserDTO asignatedTo;

    @DocumentReference
    private  ProjectDTO project;

    @DocumentReference
    private UserDTO createdBy;

    @DocumentReference
    private UserDTO updatedBy;

    private Date createdAt;

    private Date updatedAt;

    @NotNull( message = "The status of User cannot be null")
    private boolean isAvailable = true;
}
