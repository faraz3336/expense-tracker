package com.fullStack.expenseTracker.services;

import com.fullStack.expenseTracker.models.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.HtmlUtils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
public class BrevoEmailService {
    private static final String BREVO_EMAIL_API_URL = "https://api.brevo.com/v3/smtp/email";

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${brevo.api-key}")
    private String apiKey;

    @Value("${brevo.sender-email}")
    private String senderEmail;

    @Value("${app.verification-link-base-url:https://expense-tracker-backend-v2.onrender.com/mywallet/auth/signup/verify}")
    private String verificationLinkBaseUrl;

    public void sendVerificationEmail(String toEmail, String verificationCode) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", apiKey);

        Map<String, Object> body = Map.of(
                "sender", Map.of("email", senderEmail, "name", "Expense Tracker"),
                "to", List.of(Map.of("email", toEmail)),
                "subject", "Expense Tracker Verification",
                "htmlContent", buildVerificationHtml(verificationCode)
        );

        ResponseEntity<String> response = restTemplate.postForEntity(
                BREVO_EMAIL_API_URL,
                new HttpEntity<>(body, headers),
                String.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new IllegalStateException("Brevo email API returned " + response.getStatusCode());
        }
    }

    public void sendVerificationEmail(User user) {
        sendVerificationEmail(user.getEmail(), user.getVerificationCode());
    }

    private String buildVerificationHtml(String verificationCode) {
        String safeCode = HtmlUtils.htmlEscape(verificationCode);
        String verifyLink = verificationLinkBaseUrl + "?code=" + URLEncoder.encode(verificationCode, StandardCharsets.UTF_8);

        return """
                <p>Your Expense Tracker verification code is:</p>
                <p><strong>%s</strong></p>
                <p>You can also verify using this link:</p>
                <p><a href="%s">Verify your account</a></p>
                <p>This code expires in 15 minutes.</p>
                """.formatted(safeCode, HtmlUtils.htmlEscape(verifyLink));
    }
}
