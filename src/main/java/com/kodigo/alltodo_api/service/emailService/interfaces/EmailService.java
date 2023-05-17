package com.kodigo.alltodo_api.service.emailService.interfaces;

import com.kodigo.alltodo_api.model.mail.MailRequest;
import com.kodigo.alltodo_api.model.mail.MailResponse;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.Map;

public interface EmailService {


    public MailResponse sendEmail(MailRequest request, Map<String, Object> model, String pathTemplate) throws IOException;
}
