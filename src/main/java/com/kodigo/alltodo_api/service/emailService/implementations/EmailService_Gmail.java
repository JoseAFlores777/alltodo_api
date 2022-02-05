package com.kodigo.alltodo_api.service.emailService.implementations;

import com.kodigo.alltodo_api.model.mail.MailRequest;
import com.kodigo.alltodo_api.model.mail.MailResponse;
import com.kodigo.alltodo_api.service.emailService.interfaces.EmailService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;


@Service
public class EmailService_Gmail implements EmailService {

    @Autowired
    private JavaMailSender sender;

    @Autowired
    private Configuration config;

    @Value("${spring.mail.username}")
    private String from;



    public MailResponse sendEmail(MailRequest request, Map<String, Object> model, String pathTemplate) {
        MailResponse response = new MailResponse();
        MimeMessage message = sender.createMimeMessage();
        try {
            // set mediaType
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            // add attachment
            //helper.addAttachment("src/main/resources/img/Color-Brand.png", new ClassPathResource("src/main/resources/img/Color-Brand.png"));

            Template t = config.getTemplate( pathTemplate );
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);

            helper.setTo(request.getTo());
            helper.setText(html, true);
            helper.setSubject(request.getSubject());
            helper.setFrom(from,"alltodo");
            sender.send(message);

            response.setMessage("mail send to : " + request.getTo());
            response.setStatus(Boolean.TRUE);

        } catch (MessagingException | IOException | TemplateException e) {
            e.printStackTrace();
            response.setMessage("Mail Sending failure : "+e.getMessage());
            response.setStatus(Boolean.FALSE);
        }

        return response;
    }

}
