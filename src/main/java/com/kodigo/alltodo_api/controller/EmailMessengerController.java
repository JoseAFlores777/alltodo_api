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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Controller
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

    @GetMapping("/auth/email-verification/{email}")
    public ResponseEntity<?> verifyEmail (@PathVariable("email") String email){
        try {
            Map<String,Object> model = new HashMap<>();
            String pathTemplate = "verifyEmail_Template.ftl";
            MailRequest request = new MailRequest();

            UserDTO userDTO = userService.findByEmail(email).get();

            CustomUserDetails customUserDetails = new CustomUserDetails(userDTO);

            String jwt = jwtUtil.generateToken(customUserDetails);

            request.setSubject("Verification Email");
            request.setTo(userDTO.getEmail());

            model.put("userName", userDTO.getFirstName() );
            model.put("userEmail", userDTO.getEmail());
            model.put("checkerLink",MAIN_PATH+"auth/verify-email/"+jwt);


            return new ResponseEntity<>(emailService.sendEmail(request, model, pathTemplate), HttpStatus.OK);
        } catch (UserCollectionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/auth/verify-email/{token}")
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

    @GetMapping("/auth/forgot-password/{id}")
    public ResponseEntity<?> forgotPassword (@PathVariable("id") String id){
        try {
            Map<String,Object> model = new HashMap<>();
            String pathTemplate = "forgotPassword_Template.ftl";
            MailRequest request = new MailRequest();

            UserDTO userDTO = userService.getUserById(id);

            CustomUserDetails customUserDetails = new CustomUserDetails(userDTO);

            String jwt = jwtUtil.generateToken(customUserDetails);

            request.setSubject("Forgot your Password?");
            request.setTo(userDTO.getEmail());

            model.put("userName", userDTO.getFirstName() );
            model.put("checkerLink",MAIN_PATH+"auth/reset-password/"+jwt);


            return new ResponseEntity<>(emailService.sendEmail(request, model, pathTemplate), HttpStatus.OK);
        } catch (UserCollectionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/auth/reset-password/{token}")
    public String resetPasswordForm(@PathVariable("token") String token, Model model){
        try {

            String jwt = token.substring(7);
            String userId = jwtUtil.extractUserId(jwt);

            UserDTO userDTO = userService.getUserById(userId);
            CustomUserDetails customUserDetails = new CustomUserDetails(userDTO);

            if (!jwtUtil.validateToken(jwt,customUserDetails)) {
                throw new BadCredentialsException("invalid jwt");
            }
            
            model.addAttribute("id",userDTO.getId());


        }catch (Exception e){
                e.getStackTrace();
        }
            return "resetPassword.html";
        // TODO: 7/2/22  
    }


    @GetMapping("/auth/update-password/{id}")
    public ResponseEntity<?> resetPassword(@PathVariable("id") String id, @RequestBody String newPwd){
        try {

            userService.updateUserPassword(id, newPwd);

            return new ResponseEntity<>("User "+id+" changed the password",HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


}
