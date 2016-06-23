package net.yazsoft.frame.controller;


import net.yazsoft.frame.controller.scopes.ViewScoped;
import net.yazsoft.frame.utils.Constants;
import net.yazsoft.frame.utils.Util;
import org.apache.log4j.Logger;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import javax.inject.Inject;
import javax.inject.Named;

@Named
@ViewScoped
public class Email {
    private static final Logger log = Logger.getLogger(Email.class);
    private SimpleMailMessage templateMessage;

    @Inject
    MailSender mailSender;

    public void sendMail(String to, String subject, String body)
    {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("mezopotamyakoleji@hotmail.com");  // dont send without from address
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            log.info("MAIL MESSAGE : " + message);
            mailSender.send(message);
//            Util.setFacesMessage("EMAIL SENT");
        } catch (Exception e) {
            Util.setFacesMessageError(e.getMessage());
            e.printStackTrace();
        }
    }



    public void sendTestMail() {
        sendMail("cumacakmak@gmail.com","java net.yazsoft.frame.mail", "TESTTTT");
    }
}
