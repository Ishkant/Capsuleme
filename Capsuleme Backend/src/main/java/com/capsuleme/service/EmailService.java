package com.capsuleme.service;

import com.capsuleme.model.Capsule;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.base-url}")
    private String baseUrl;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendUnlockEmail(Capsule capsule) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(capsule.getUserEmail());
            helper.setSubject("Your CapsuleMe is ready to open — \"" + capsule.getTitle() + "\"");

            String unlockUrl = baseUrl + "/unlock.html?id=" + capsule.getId();
            String html = buildUnlockEmailHtml(capsule.getUserName(), capsule.getTitle(), unlockUrl);
            helper.setText(html, true);

            mailSender.send(message);
            System.out.println("Unlock email sent to: " + capsule.getUserEmail());

        } catch (MessagingException e) {
            System.err.println("Failed to send unlock email: " + e.getMessage());
        }
    }

    public void sendSealConfirmationEmail(Capsule capsule) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(capsule.getUserEmail());
            helper.setSubject("Your CapsuleMe has been sealed — \"" + capsule.getTitle() + "\"");

            String html = buildSealConfirmationHtml(
                    capsule.getUserName(),
                    capsule.getTitle(),
                    capsule.getUnlockDate().toString()
            );
            helper.setText(html, true);

            mailSender.send(message);
            System.out.println("Seal confirmation sent to: " + capsule.getUserEmail());

        } catch (MessagingException e) {
            System.err.println("Failed to send confirmation email: " + e.getMessage());
        }
    }

    private String buildUnlockEmailHtml(String name, String title, String unlockUrl) {
        return "<!DOCTYPE html><html><body style=\"font-family:Georgia,serif;background:#fdf8f0;padding:40px 20px;margin:0;\">"
                + "<div style=\"max-width:560px;margin:0 auto;background:#fff;border:1px solid #e0d8cc;border-radius:4px;overflow:hidden;\">"
                + "<div style=\"background:#c0392b;padding:24px 32px;\">"
                + "<h1 style=\"color:#fff;margin:0;font-size:22px;\">CapsuleMe</h1>"
                + "<p style=\"color:#f5b7b1;margin:4px 0 0;font-size:14px;\">Seal Your Present. Surprise Your Future.</p>"
                + "</div>"
                + "<div style=\"padding:32px;\">"
                + "<h2 style=\"font-size:20px;margin:0 0 12px;color:#1a1612;\">Hey " + name + "!</h2>"
                + "<p style=\"color:#5a5040;line-height:1.7;font-size:15px;\">Your time capsule <strong>\"" + title + "\"</strong> has unlocked today!</p>"
                + "<p style=\"color:#5a5040;line-height:1.7;font-size:15px;\">Answer a few quick questions about who you are <em>today</em>, then see your personal growth report.</p>"
                + "<div style=\"text-align:center;margin:28px 0;\">"
                + "<a href=\"" + unlockUrl + "\" style=\"background:#c0392b;color:#fff;padding:14px 32px;text-decoration:none;border-radius:2px;font-size:15px;\">Open My Capsule</a>"
                + "</div>"
                + "<p style=\"color:#a0907a;font-size:13px;\">Or copy this link: <a href=\"" + unlockUrl + "\" style=\"color:#c0392b;\">" + unlockUrl + "</a></p>"
                + "</div></div></body></html>";
    }

    private String buildSealConfirmationHtml(String name, String title, String unlockDate) {
        return "<!DOCTYPE html><html><body style=\"font-family:Georgia,serif;background:#fdf8f0;padding:40px 20px;margin:0;\">"
                + "<div style=\"max-width:560px;margin:0 auto;background:#fff;border:1px solid #e0d8cc;border-radius:4px;overflow:hidden;\">"
                + "<div style=\"background:#c0392b;padding:24px 32px;\">"
                + "<h1 style=\"color:#fff;margin:0;font-size:22px;\">CapsuleMe</h1>"
                + "<p style=\"color:#f5b7b1;margin:4px 0 0;font-size:14px;\">Seal Your Present. Surprise Your Future.</p>"
                + "</div>"
                + "<div style=\"padding:32px;\">"
                + "<h2 style=\"font-size:20px;margin:0 0 12px;color:#1a1612;\">Your capsule is sealed, " + name + "!</h2>"
                + "<p style=\"color:#5a5040;line-height:1.7;font-size:15px;\"><strong>\"" + title + "\"</strong> is now locked and waiting for you.</p>"
                + "<div style=\"background:#fdf8f0;border:1px solid #e0d8cc;border-radius:4px;padding:16px 20px;margin:20px 0;\">"
                + "<p style=\"margin:0;font-size:14px;color:#7a6f62;\">Unlock Date</p>"
                + "<p style=\"margin:4px 0 0;font-size:20px;font-weight:600;color:#1a1612;\">" + unlockDate + "</p>"
                + "</div>"
                + "<p style=\"color:#5a5040;line-height:1.7;font-size:15px;\">On that day you'll receive another email. Until then — keep going!</p>"
                + "</div></div></body></html>";
    }
}
