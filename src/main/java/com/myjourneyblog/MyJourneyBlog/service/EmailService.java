package com.myjourneyblog.MyJourneyBlog.service;

import com.myjourneyblog.MyJourneyBlog.config.EmailProperties;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * Send HTML email asynchronously
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final EmailProperties emailProperties;

    /**
     * Send HTML email asynchronously
     */
    @Async
    public void sendEmail(String to, String subject, String htmlContent) {
        if (!emailProperties.isEnabled()) {
            log.info("Email sending is disabled. Would send to: {} with subject: {}", to, subject);
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(emailProperties.getFrom(), emailProperties.getFromName());
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true = HTML

            mailSender.send(message);
            log.info("Email sent successfully to: {}", to);

        } catch (MessagingException e) {
            log.error("Failed to send email to: {}", to, e);
            // Don't throw exception - email failures shouldn't break app flow
        } catch (Exception e) {
            log.error("Unexpected error sending email to: {}", to, e);
        }
    }

    /**
     * Send welcome email to new user
     */
    public void sendWelcomeEmail(String to, String username, String fullName) {
        String subject = "Welcome to My Journey Blog! 🎉";
        String htmlContent = buildWelcomeEmail(username, fullName);
        sendEmail(to, subject, htmlContent);
    }

    /**
     * Send password reset email
     */
    public void sendPasswordResetEmail(String to, String username, String resetToken) {
        String subject = "Reset Your Password";
        String resetLink = "http://localhost:8080/reset-password?token=" + resetToken;
        String htmlContent = buildPasswordResetEmail(username, resetLink);
        sendEmail(to, subject, htmlContent);
    }

    /**
     * Send post published notification
     */
    public void sendPostPublishedEmail(String to, String username, String postTitle, Long postId) {
        String subject = "Your post has been published!";
        String postLink = "http://localhost:8080/learning/post/" + postId;
        String htmlContent = buildPostPublishedEmail(username, postTitle, postLink);
        sendEmail(to, subject, htmlContent);
    }

    /**
     * Send new follower notification (future feature)
     */
    public void sendNewFollowerEmail(String to, String followerUsername) {
        String subject = followerUsername + " started following you!";
        String htmlContent = buildNewFollowerEmail(followerUsername);
        sendEmail(to, subject, htmlContent);
    }

    // ===== EMAIL TEMPLATE BUILDERS =====

    /**
     * Build welcome email HTML
     */
    private String buildWelcomeEmail(String username, String fullName) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "    <meta charset='UTF-8'>" +
                "</head>" +
                "<body style='font-family: Arial, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: 0 auto; padding: 20px;'>" +
                "    <div style='background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); padding: 30px; text-align: center; border-radius: 10px 10px 0 0;'>" +
                "        <h1 style='color: white; margin: 0; font-size: 28px;'>Welcome to My Journey Blog! 🎉</h1>" +
                "    </div>" +
                "    <div style='background-color: #f9f9f9; padding: 30px; border-radius: 0 0 10px 10px;'>" +
                "        <p style='font-size: 18px; margin-bottom: 20px;'>Hi " + fullName + "!</p>" +
                "        <p style='margin-bottom: 15px;'>Welcome aboard! Your account <strong>@" + username + "</strong> has been created successfully.</p>" +
                "        <p style='margin-bottom: 20px;'>You can now start documenting your learning journey and sharing your progress with the community.</p>" +
                "        <div style='background-color: white; padding: 20px; border-radius: 8px; margin: 20px 0;'>" +
                "            <h2 style='color: #667eea; font-size: 20px; margin-top: 0;'>Quick Start Guide:</h2>" +
                "            <ul style='padding-left: 20px;'>" +
                "                <li style='margin-bottom: 10px;'>📝 Create your first learning post</li>" +
                "                <li style='margin-bottom: 10px;'>🚀 Share project updates</li>" +
                "                <li style='margin-bottom: 10px;'>📷 Upload a profile picture</li>" +
                "                <li style='margin-bottom: 10px;'>🔍 Explore other's journeys</li>" +
                "            </ul>" +
                "        </div>" +
                "        <div style='text-align: center; margin-top: 30px;'>" +
                "            <a href='http://localhost:8080/dashboard' style='background-color: #667eea; color: white; padding: 12px 30px; text-decoration: none; border-radius: 6px; display: inline-block; font-weight: bold;'>Go to Dashboard</a>" +
                "        </div>" +
                "        <p style='margin-top: 30px; font-size: 14px; color: #666;'>Happy learning!<br>The Developer Journey Team</p>" +
                "    </div>" +
                "    <div style='text-align: center; padding: 20px; font-size: 12px; color: #999;'>" +
                "        <p>You received this email because you registered at My Journey Blog.</p>" +
                "        <p>© 2025 My Journey Blog. All rights reserved.</p>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }

    /**
     * Build password reset email HTML
     */
    private String buildPasswordResetEmail(String username, String resetLink) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "    <meta charset='UTF-8'>" +
                "</head>" +
                "<body style='font-family: Arial, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: 0 auto; padding: 20px;'>" +
                "    <div style='background-color: #ff6b6b; padding: 30px; text-align: center; border-radius: 10px 10px 0 0;'>" +
                "        <h1 style='color: white; margin: 0; font-size: 28px;'>Password Reset Request 🔒</h1>" +
                "    </div>" +
                "    <div style='background-color: #f9f9f9; padding: 30px; border-radius: 0 0 10px 10px;'>" +
                "        <p style='font-size: 18px; margin-bottom: 20px;'>Hi @" + username + ",</p>" +
                "        <p style='margin-bottom: 15px;'>We received a request to reset your password.</p>" +
                "        <p style='margin-bottom: 20px;'>Click the button below to reset your password:</p>" +
                "        <div style='text-align: center; margin: 30px 0;'>" +
                "            <a href='" + resetLink + "' style='background-color: #ff6b6b; color: white; padding: 12px 30px; text-decoration: none; border-radius: 6px; display: inline-block; font-weight: bold;'>Reset Password</a>" +
                "        </div>" +
                "        <div style='background-color: #fff3cd; border-left: 4px solid #ffc107; padding: 15px; margin: 20px 0;'>" +
                "            <p style='margin: 0; color: #856404;'><strong>⚠️ Security Notice:</strong></p>" +
                "            <p style='margin: 5px 0 0; color: #856404;'>This link will expire in 1 hour. If you didn't request this, please ignore this email.</p>" +
                "        </div>" +
                "        <p style='margin-top: 30px; font-size: 14px; color: #666;'>Best regards,<br>The Developer Journey Team</p>" +
                "    </div>" +
                "    <div style='text-align: center; padding: 20px; font-size: 12px; color: #999;'>" +
                "        <p>If the button doesn't work, copy this link:</p>" +
                "        <p style='color: #667eea; word-break: break-all;'>" + resetLink + "</p>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }

    /**
     * Build post published email HTML
     */
    private String buildPostPublishedEmail(String username, String postTitle, String postLink) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "    <meta charset='UTF-8'>" +
                "</head>" +
                "<body style='font-family: Arial, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: 0 auto; padding: 20px;'>" +
                "    <div style='background-color: #51cf66; padding: 30px; text-align: center; border-radius: 10px 10px 0 0;'>" +
                "        <h1 style='color: white; margin: 0; font-size: 28px;'>Post Published! 🎉</h1>" +
                "    </div>" +
                "    <div style='background-color: #f9f9f9; padding: 30px; border-radius: 0 0 10px 10px;'>" +
                "        <p style='font-size: 18px; margin-bottom: 20px;'>Hi @" + username + ",</p>" +
                "        <p style='margin-bottom: 15px;'>Great news! Your post has been published successfully.</p>" +
                "        <div style='background-color: white; padding: 20px; border-radius: 8px; margin: 20px 0; border-left: 4px solid #51cf66;'>" +
                "            <h2 style='margin-top: 0; font-size: 20px; color: #333;'>" + postTitle + "</h2>" +
                "            <p style='color: #666; margin-bottom: 0;'>Your learning journey continues!</p>" +
                "        </div>" +
                "        <div style='text-align: center; margin: 30px 0;'>" +
                "            <a href='" + postLink + "' style='background-color: #51cf66; color: white; padding: 12px 30px; text-decoration: none; border-radius: 6px; display: inline-block; font-weight: bold;'>View Post</a>" +
                "        </div>" +
                "        <p style='margin-top: 30px; font-size: 14px; color: #666;'>Keep up the great work!<br>The Developer Journey Team</p>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }

    /**
     * Build new follower email HTML
     */
    private String buildNewFollowerEmail(String followerUsername) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "    <meta charset='UTF-8'>" +
                "</head>" +
                "<body style='font-family: Arial, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: 0 auto; padding: 20px;'>" +
                "    <div style='background-color: #4dabf7; padding: 30px; text-align: center; border-radius: 10px 10px 0 0;'>" +
                "        <h1 style='color: white; margin: 0; font-size: 28px;'>New Follower! 👥</h1>" +
                "    </div>" +
                "    <div style='background-color: #f9f9f9; padding: 30px; border-radius: 0 0 10px 10px;'>" +
                "        <p style='font-size: 18px; margin-bottom: 20px;'>Great news!</p>" +
                "        <p style='margin-bottom: 15px;'><strong>@" + followerUsername + "</strong> started following your journey.</p>" +
                "        <div style='text-align: center; margin: 30px 0;'>" +
                "            <a href='http://localhost:8080/dashboard' style='background-color: #4dabf7; color: white; padding: 12px 30px; text-decoration: none; border-radius: 6px; display: inline-block; font-weight: bold;'>View Dashboard</a>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }
}
