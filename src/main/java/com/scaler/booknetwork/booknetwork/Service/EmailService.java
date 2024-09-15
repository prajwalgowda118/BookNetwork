package com.scaler.booknetwork.booknetwork.Service;

import com.scaler.booknetwork.booknetwork.Models.EmailTemplateName;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static java.awt.SystemColor.text;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED;

@Service
@RequiredArgsConstructor

public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    public void sendEmail(String to,
                          String username,
                          EmailTemplateName emailTemplateName,
                          String confirmationUrl,
                          String activationCode,
                          String Subject) throws MessagingException {
        String templateName;
        if(emailTemplateName == null){
            templateName="confirm-email";
        }else{
            templateName=emailTemplateName.getValue();
        }

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper helper=new MimeMessageHelper(
                mimeMessage,
                MULTIPART_MODE_MIXED,
                UTF_8.name()
        );

        Map<String,Object> model = new HashMap<>();
        model.put("username", username);
        model.put("templateName", templateName);
        model.put("confirmationUrl", confirmationUrl);
        model.put("activation_code", activationCode);

        Context context = new Context();
        context.setVariables(model);
        helper.setFrom("prajwal7683@gmail.com");
        helper.setTo(to);
        helper.setSubject(Subject);
        //helper.setText(text,true);

        String template = templateEngine.process(templateName, context);

        helper.setText(template,true);

        mailSender.send(mimeMessage);

        /*SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(Subject);
        message.setText(text);
        mailSender.send(message);*/

    }

}
