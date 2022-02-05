package com.kodigo.alltodo_api.model.mail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailRequest {

    private String to;
    private String subject;
    private Object entity;
}
