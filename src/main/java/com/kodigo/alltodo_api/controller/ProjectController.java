package com.kodigo.alltodo_api.controller;

import com.kodigo.alltodo_api.exception.ProjectCollectionException;
import com.kodigo.alltodo_api.exception.TodoCollectionException;
import com.kodigo.alltodo_api.model.ProjectDTO;
import com.kodigo.alltodo_api.model.TodoDTO;
import com.kodigo.alltodo_api.service.interfaces.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.List;


@RestController
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("/projects")
    public ResponseEntity<?> getAll(){
        List<ProjectDTO> projects = projectService.getAllProjects();
        return new ResponseEntity<>( projects, projects.size() > 0 ? HttpStatus.OK : HttpStatus.NOT_FOUND );
    }

    @PostMapping("/projects")
    public ResponseEntity<?> create(@RequestBody ProjectDTO project) {
        try {
            projectService.createProject(project);
            return new ResponseEntity<ProjectDTO>(project, HttpStatus.OK);

        } catch (ConstraintViolationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (ProjectCollectionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/projects/{id}")
    public ResponseEntity<?> getById( @PathVariable("id") String id ){
        try {
            return new ResponseEntity<>(projectService.getProjectById(id), HttpStatus.OK);
        } catch (ProjectCollectionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/projects/{id}")
    public ResponseEntity<?> updateById( @PathVariable("id") String id, @RequestBody ProjectDTO project ){
        try {
            projectService.updateProject( id, project );
            return new ResponseEntity<>("Update Todo with id "+id, HttpStatus.OK);
        } catch (ConstraintViolationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (ProjectCollectionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/projects/{id}")
    public ResponseEntity<?> deleteById( @PathVariable("id") String id ){
        try {
            projectService.deleteProject(id);
            return new ResponseEntity<>("Successfully deleted with id "+id, HttpStatus.OK );
        }catch ( ProjectCollectionException e ){
            return new ResponseEntity<>( e.getMessage(), HttpStatus.NOT_FOUND );
        }
    }

}
