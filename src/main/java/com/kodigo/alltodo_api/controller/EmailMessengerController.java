package com.kodigo.alltodo_api.controller;

import com.kodigo.alltodo_api.exception.ProjectCollectionException;
import com.kodigo.alltodo_api.exception.UserCollectionException;
import com.kodigo.alltodo_api.helpers.DateFormatter;
import com.kodigo.alltodo_api.model.auth.CustomUserDetails;
import com.kodigo.alltodo_api.model.dto.TodoDTO;
import com.kodigo.alltodo_api.model.dto.UserDTO;
import com.kodigo.alltodo_api.model.mail.MailRequest;
import com.kodigo.alltodo_api.model.mail.MailResponse;
import com.kodigo.alltodo_api.service.DB.interfaces.DBservices.UserService;
import com.kodigo.alltodo_api.service.emailService.interfaces.EmailService;
import com.kodigo.alltodo_api.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class EmailMessengerController {

    @Autowired
    private EmailService emailService;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;

    private DateFormatter dateFormatter = new DateFormatter();

    @Value("${MAIN_PATH}")
    private String MAIN_PATH;

    public MailResponse newTodoEmail(MailRequest request){
        String pathTemplate = "newTodoEmail_Template.ftl";
        TodoDTO todo = (TodoDTO) request.getEntity();
        Map<String,Object> model = new HashMap<>();
        model.put("userName", todo.getCreatedBy().getFirstName() );
        model.put("todoTitle", todo.getTitle());
        model.put("todoDate", dateFormatter.Formatter( todo.getExpirationDate(),"MMM d, E" )  );
        String projectName = (todo.getProject() != null) ? todo.getProject().getName() : "None";
        model.put("projectName", projectName);
        model.put("MAIN_PATH",MAIN_PATH);
        return emailService.sendEmail(request, model, pathTemplate);
    }

    @GetMapping("/auth/verify-email/{id}")
    public ResponseEntity<?> verifyEmail (@PathVariable("id") String id){
        try {
            Map<String,Object> model = new HashMap<>();
            String pathTemplate = "verifyEmail_Template.ftl";
            MailRequest request = new MailRequest();

            UserDTO userDTO = userService.getUserById(id);

            CustomUserDetails customUserDetails = new CustomUserDetails(userDTO);

            String jwt = jwtUtil.generateToken(customUserDetails);

            request.setSubject("Verification Email");
            request.setTo(userDTO.getEmail());

            model.put("userName", userDTO.getFirstName() );
            model.put("userEmail", userDTO.getEmail());
            model.put("checkerLink",MAIN_PATH+"auth/email-verified/"+jwt);


            return new ResponseEntity<>(emailService.sendEmail(request, model, pathTemplate), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getStackTrace(), HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/auth/email-verified/{token}")
    public ResponseEntity<?> emailVerified( @PathVariable("token") String token ){
        try {

            String jwt = token.substring(7);
            String userId = jwtUtil.extractUserId(jwt);

            UserDTO userDTO = userService.getUserById(userId);
            CustomUserDetails customUserDetails = new CustomUserDetails(userDTO);

            if (!jwtUtil.validateToken(jwt,customUserDetails)) {
                throw new BadCredentialsException("invalid jwt");
            }

            userService.verifyUserEmail(userDTO.getId());

            return new ResponseEntity<>("User "+userDTO.getId()+" verified",HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
