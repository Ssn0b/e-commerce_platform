package app.snob.ecommerce_platform.security.service;

import app.snob.ecommerce_platform.entity.User;

import java.util.UUID;

public interface EmailConfirmationService {
    void sendConfirmationEmail(String userEmail, String confirmationToken);
    boolean confirmEmail(String confirmationToken);
    UUID generateConfirmationToken(User user);
}
