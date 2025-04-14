package com.samuelangan.mycompagny.authentification.service;


import com.samuelangan.mycompagny.authentification.config.MailCustomProperties;
import com.samuelangan.mycompagny.authentification.domain.User;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;


import com.samuelangan.mycompagny.authentification.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;


/**
 * Service for sending emails.
 * <p>
 * We use the {@link Async} annotation to send emails asynchronously.
 */
@Service
public class MailService {

    private final Logger log = LoggerFactory.getLogger(MailService.class);

    private static final String USER = "user";


    private static final String BASE_URL = "baseUrl";

    private final MailCustomProperties mailCustomProperties;

    private final JavaMailSender javaMailSender;

    private final MessageSource messageSource;

    private final SpringTemplateEngine templateEngine;
    private final UserRepository userRepository;

    public MailService(
            MailCustomProperties mailCustomProperties,
            JavaMailSender javaMailSender,
            MessageSource messageSource,
            SpringTemplateEngine templateEngine,
            UserRepository userRepository) {
        this.mailCustomProperties = mailCustomProperties;
        this.javaMailSender = javaMailSender;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
        this.userRepository = userRepository;
    }

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug(
                "Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
                isMultipart,
                isHtml,
                to,
                subject,
                content
        );

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom(mailCustomProperties.getFrom());
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent email to User '{}'", to);
        } catch (MailException | MessagingException e) {
            log.warn("Email could not be sent to user '{}'", to, e);
        }
    }





    @Async
    public void sendEmailFromTemplate(User user, String templateName, String titleKey) {
        if (user.getEmail() == null) {
            log.debug("Email doesn't exist for user '{}'", user.getLogin());
            return;
        }
        Locale locale = Locale.getDefault();
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, mailCustomProperties.getBaseUrl());  // Utilisation de la baseUrl de MailCustomProperties
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }





    @Async
    public void sendEmailToAdminFromTemplate(User admin, User user, String templateName, String titleKey) {
        if (admin == null || admin.getEmail() == null) {
            log.debug("Email doesn't exist for user admin '{}'", admin.getLogin());
            return;
        }

        if (user == null || user.getEmail() == null) {
            log.debug("Email doesn't exist for user '{}'", user.getLogin());
            return;
        }
        Locale locale = Locale.getDefault();
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, mailCustomProperties.getBaseUrl());
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, locale);
        sendEmail(admin.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendCreationEmail(User user) {
        log.debug("Sending creation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/creationEmailToUser", "email.creation.title");
    }

    @Async
    public void sendActivationConfirmEmail(User user) {
        log.debug("Sending creation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/activationConfirmEmail", "email.activation.title");
    }

    @Async
    public void sendActivationRequestEmail(User admin, User user) {
        log.debug("Sending information email to admin user '{}' for user {}", admin.getEmail(), user.getEmail());
        sendEmailToAdminFromTemplate(admin, user, "mail/activationRequestEmail", "email.activation.title");
    }

    @Async
    public void sendPasswordResetMail(User user) {
        log.debug("Sending password reset email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "order/passwordResetEmail", "email.reset.title");
    }




}

