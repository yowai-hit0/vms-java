package com.rw.rra.vms.email;

import com.rw.rra.vms.auth.OtpType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

@Service
@AllArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final ITemplateEngine templateEngine;

    @Async
    public void sendAccountVerificationEmail(String to, String name, String otp) {
        sendOtpEmail(to, name, otp, OtpType.VERIFY_ACCOUNT);
    }

    @Async
    public void sendResetPasswordOtp(String to, String name, String otp) {
        sendOtpEmail(to, name, otp, OtpType.FORGOT_PASSWORD);
    }

    @Async
    public void sendVerificationSuccessEmail(String to, String name) {
        sendSuccessEmail(to, name, "verify_success", "Account Verified Successfully");
    }

    @Async
    public void sendResetPasswordSuccessEmail(String to, String name) {
        sendSuccessEmail(to, name, "reset_success", "Password Reset Successfully");
    }


    @Async
    public void sendUserRegisteredEmail(String to, String name, String plateNumber) {
        Context ctx = new Context();
        ctx.setVariable("name", name);
        ctx.setVariable("plateNumber", plateNumber);
        ctx.setVariable("companyName", "Rwanda Revenue Authority");
        sendEmail("owner_registered", ctx, to, "Welcome to RRA Vehicle Management");
    }

    @Async
    public void sendPlateCreatedEmail(String to, String name, String plateNumber) {
        Context ctx = new Context();
        ctx.setVariable("name", name);
        ctx.setVariable("plateNumber", plateNumber);
        ctx.setVariable("companyName", "Rwanda Revenue Authority");
        sendEmail("plate_created", ctx, to, "Your Plate Number Has Been Created");
    }

    @Async
    public void sendPlateAssignedEmail(String to, String name, String plateNumber, String vehicleDetails) {
        Context ctx = new Context();
        ctx.setVariable("name", name);
        ctx.setVariable("plateNumber", plateNumber);
        ctx.setVariable("vehicleDetails", vehicleDetails);
        ctx.setVariable("companyName", "Rwanda Revenue Authority");
        sendEmail("plate_assigned", ctx, to, "Your Plate Has Been Assigned to a Vehicle");
    }

    @Async
    public void sendOwnershipTransferEmailToSender(String to, String name, String plateNumber,
                                                   Double amount, String newOwnerFirst, String newOwnerLast) {
        Context ctx = new Context();
        ctx.setVariable("name", name);
        ctx.setVariable("plateNumber", plateNumber);
        ctx.setVariable("amount", amount);
        ctx.setVariable("newOwner", newOwnerFirst + " " + newOwnerLast);
        ctx.setVariable("companyName", "Rwanda Revenue Authority");
        sendEmail("ownership_transferred_sender", ctx, to, "Vehicle Ownership Transferred");
    }

    @Async
    public void sendOwnershipTransferEmailToReceiver(String to, String name, String plateNumber,
                                                     Double amount, String prevOwnerFirst, String prevOwnerLast) {
        Context ctx = new Context();
        ctx.setVariable("name", name);
        ctx.setVariable("plateNumber", plateNumber);
        ctx.setVariable("amount", amount);
        ctx.setVariable("previousOwner", prevOwnerFirst + " " + prevOwnerLast);
        ctx.setVariable("companyName", "Rwanda Revenue Authority");
        sendEmail("ownership_transferred_receiver", ctx, to, "You Received a Vehicle Ownership");
    }

    @Async
    public void sendPostInspectionNotification(String to, String name, String plateNumber) {
        Context ctx = new Context();
        ctx.setVariable("name", name);
        ctx.setVariable("plateNumber", plateNumber);
        ctx.setVariable("companyName", "Rwanda Revenue Authority");
        sendEmail("post_inspection_notification", ctx, to, "Vehicle Inspection Update");
    }

    // Generic OTP email
    private void sendOtpEmail(String to, String name, String otp, OtpType type) {
        try {
            Context ctx = new Context();
            ctx.setVariable("name", name);
            ctx.setVariable("otp", otp);
            ctx.setVariable("companyName", "Rwanda Revenue Authority");
            ctx.setVariable("expirationTime", "10 minutes");

            String template = switch (type) {
                case VERIFY_ACCOUNT -> "verify_account";
                case FORGOT_PASSWORD -> "forgot_password";
            };

            MimeMessage msg = mailSender.createMimeMessage();
            var helper = new MimeMessageHelper(msg, MimeMessageHelper.MULTIPART_MODE_MIXED);
            String html = templateEngine.process(template, ctx);
            helper.setText(html, true);
            helper.setTo(to);
            helper.setSubject(type == OtpType.VERIFY_ACCOUNT
                    ? "Verify your account - One Time Password (OTP)"
                    : "Reset your password - One Time Password (OTP)");
            helper.setFrom("ybarasingiz@zohomail.com");
            mailSender.send(msg);
        } catch (MessagingException e) {
            log.error("Unable to send OTP email", e);
        }
    }

    // Generic success email
    private void sendSuccessEmail(String to, String name, String template, String subject) {
        try {
            Context ctx = new Context();
            ctx.setVariable("name", name);
            ctx.setVariable("companyName", "Rwanda Revenue Authority");

            MimeMessage msg = mailSender.createMimeMessage();
            var helper = new MimeMessageHelper(msg, MimeMessageHelper.MULTIPART_MODE_MIXED);
            String html = templateEngine.process(template, ctx);
            helper.setText(html, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom("ybarasingiz@zohomail.com");
            mailSender.send(msg);
        } catch (MessagingException e) {
            log.error("Unable to send success email", e);
        }
    }

    // Very generic for one-off templates
    private void sendEmail(String template, Context ctx, String to, String subject) {
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            var helper = new MimeMessageHelper(msg, MimeMessageHelper.MULTIPART_MODE_MIXED);
            String html = templateEngine.process(template, ctx);
            helper.setText(html, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom("ybarasingiz@zohomail.com");
            mailSender.send(msg);
        } catch (MessagingException e) {
            log.error("Failed to send email [{}] to {}", subject, to, e);
        }
    }
}
