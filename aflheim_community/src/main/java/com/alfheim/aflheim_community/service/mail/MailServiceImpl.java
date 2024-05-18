package com.alfheim.aflheim_community.service.mail;

import com.alfheim.aflheim_community.exception.server.InternalServerErrorException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassRelativeResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.SpringTemplateLoader;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String mailFrom;
    private Template confirmationMailTemplate;
    private Configuration configuration;
    private Map<String, String> emailTemplates = new HashMap<>();

    public MailServiceImpl() {}

    @Override
    public void configureMailSettings(String emailType) {
        this.configuration = new Configuration(Configuration.VERSION_2_3_30);
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateLoader(
                new SpringTemplateLoader(new ClassRelativeResourceLoader(this.getClass()), "/")
        );
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        try {
            this.emailTemplates.put("confirmation", "templates/confirm_mail.ftlh");
            this.emailTemplates.put("reset_password", "templates/reset_password_mail.ftlh");

            if (emailType.equals("confirmation")) {
                System.out.println("CONFIRMATION STEP CONDITIONAL. TEMPLATE PATH : " + String.valueOf(emailTemplates.get("confirmation")));
                this.confirmationMailTemplate = configuration.getTemplate(String.valueOf(emailTemplates.get("confirmation")));
            } else {
                System.out.println("SETTING TEMPLATE PATH TO RESET PASSWORD");
                this.confirmationMailTemplate = configuration.getTemplate(String.valueOf(emailTemplates.get("reset_password")));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            log.error("Error setting mail configs up (MailServiceImpl.configureMailSettings)");
            throw new IllegalArgumentException(e);
        }
    }
    @Override
    public void sendEmail(String email, String emailType, Map<String, String> model) {

        System.out.println("MAIL RESET PASSWORD CODE GOT [SENDING] -> "+model.get("pass_reset_code")+"\n\n");

        String mailText = getEmailText(emailType, model);
        MimeMessagePreparator messagePreparator = getEmail(email, emailType,mailText);
        javaMailSender.send(messagePreparator);
    }

    private MimeMessagePreparator getEmail(String email, String emailType, String mailText) {

        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(mailFrom);
            messageHelper.setTo(email);

            if (emailType.equals("confirmation")) {
                messageHelper.setSubject("Confirm Your Email");
            } else if (emailType.equals("passwordReset")) {
                messageHelper.setSubject("Reset Your Password");
            }

            messageHelper.setText(mailText, true);
        };

        return  messagePreparator;
    }

    private String getEmailText(String emailType, Map<String, String> model) {

        Map<String, String> attr = new HashMap<>();

        if (emailType.equals("confirmation")) {
            System.out.println("EMAIL TYPE, CONFIRMATION");
            attr.put("confirm_code", model.get("confirm_code"));
        } else if (emailType.equals("passwordReset")) {
            System.out.println("EMAIL TYPE, PASSWORD RESET");
            attr.put("reset_code", model.get("pass_reset_code"));
        }

        StringWriter writer = new StringWriter();
        try {
            confirmationMailTemplate.process(attr, writer);
        } catch (TemplateException | IOException e) {
            log.error("template &| IO error (MailServiceImpl.getEmailText)");
            throw new IllegalStateException(e);
        }

        return writer.toString();
    }
}
