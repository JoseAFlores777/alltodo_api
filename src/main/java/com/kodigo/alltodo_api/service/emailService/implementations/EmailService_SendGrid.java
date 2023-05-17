package com.kodigo.alltodo_api.service.emailService.implementations;

import com.kodigo.alltodo_api.model.mail.MailRequest;
import com.kodigo.alltodo_api.model.mail.MailResponse;
import com.kodigo.alltodo_api.service.emailService.interfaces.EmailService;
import com.sendgrid.*;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
@Primary
public class EmailService_SendGrid implements EmailService {

    @Autowired
    private Configuration config;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    public MailResponse sendEmail(MailRequest request, Map<String, Object> model, String pathTemplate) {
        MailResponse response = new MailResponse();
        Email fromEmail = new Email(from, "alltodo");
        Email toEmail = new Email(request.getTo());
        String subject = request.getSubject();
        Content content;

        try {
            Template template = config.getTemplate(pathTemplate);
            String htmlContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            content = new Content("text/html", htmlContent);

            Mail mail = new Mail(fromEmail, subject, toEmail, content);

            SendGrid sg = new SendGrid(sendGridApiKey);
            Request sendGridRequest = new Request();

            sendGridRequest.setMethod(Method.POST);
            sendGridRequest.setEndpoint("mail/send");
            sendGridRequest.setBody(mail.build());

            Response sendGridResponse = sg.api(sendGridRequest);

            if (sendGridResponse.getStatusCode() == 202) {
                response.setMessage("Mail sent to: " + request.getTo());
                response.setStatus(true);
            } else {
                response.setMessage("Failed to send mail. Status code: " + sendGridResponse.getStatusCode());
                response.setStatus(false);
            }
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
            response.setMessage("Mail sending failure: " + e.getMessage());
            response.setStatus(false);
        }

        return response;
    }

}
