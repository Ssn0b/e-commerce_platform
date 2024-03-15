package app.snob.ecommerce_platform.security.service.impl;

import app.snob.ecommerce_platform.entity.User;
import app.snob.ecommerce_platform.repository.UserRepository;
import app.snob.ecommerce_platform.security.service.EmailConfirmationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailConfirmationServiceImpl implements EmailConfirmationService {
    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;

    @Async
    @Override
    public void sendConfirmationEmail(String userEmail, String confirmationToken) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("levc4kvlad@gmail.com");
        message.setTo(userEmail);
        message.setSubject("Confirm your registry at E-commerce platform!");
        message.setText("Thank you for registering with E-commerce platform!\n\n" +
                "To confirm your registration, please click on the following link:\n" +
                "http://localhost:8080/api/v1/auth/confirm?token=" + confirmationToken + "\n\n" +
                "If you didn't request this registration, please ignore this email.");
        javaMailSender.send(message);
    }

    @Override
    public boolean confirmEmail(String confirmationToken) {
        Optional<User> userOptional = userRepository.findByConfirmationToken(UUID.fromString(confirmationToken));
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setConfirmedByEmail(true);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public UUID generateConfirmationToken(User user) {
        UUID token = UUID.randomUUID();
        user.setConfirmationToken(token);
        return token;
    }
}
