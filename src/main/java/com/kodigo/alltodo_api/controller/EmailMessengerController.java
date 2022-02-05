package com.kodigo.alltodo_api.controller;

import com.kodigo.alltodo_api.helpers.DateFormatter;
import com.kodigo.alltodo_api.model.dto.TodoDTO;
import com.kodigo.alltodo_api.model.mail.MailRequest;
import com.kodigo.alltodo_api.model.mail.MailResponse;
import com.kodigo.alltodo_api.service.emailService.interfaces.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class EmailMessengerController {

    @Autowired
    private EmailService emailService;

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

}
