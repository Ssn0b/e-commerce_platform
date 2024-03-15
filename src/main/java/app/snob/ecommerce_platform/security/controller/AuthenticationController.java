package app.snob.ecommerce_platform.security.controller;

import app.snob.ecommerce_platform.security.dto.AuthenticationRequest;
import app.snob.ecommerce_platform.security.dto.AuthenticationResponse;
import app.snob.ecommerce_platform.security.dto.RegisterRequest;
import app.snob.ecommerce_platform.security.service.AuthenticationService;
import app.snob.ecommerce_platform.security.service.EmailConfirmationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * The AuthenticationController handles user authentication and token management.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final EmailConfirmationService emailConfirmationService;

    /**
     * Register a new user.
     *
     * @param request The registration request containing user details.
     * @return ResponseEntity with an AuthenticationResponse upon successful registration.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    /**
     * Authenticate a user.
     *
     * @param request The authentication request containing user credentials.
     * @return ResponseEntity with an AuthenticationResponse upon successful authentication.
     */
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    /**
     * Refresh the authentication token.
     *
     * @param request  The HTTP request containing the current authentication token.
     * @param response The HTTP response where the new token will be sent.
     * @throws IOException If an IO error occurs during token refresh.
     */
    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        authenticationService.refreshToken(request, response);
    }
    /**
     * Confirm user's email using a confirmation token.
     *
     * @param confirmationToken The confirmation token received via email.
     * @return ResponseEntity with a message indicating the status of email confirmation.
     */
    @GetMapping("/confirm")
    public ResponseEntity<String> confirmEmail(@RequestParam("token") String confirmationToken) {
        boolean confirmed = emailConfirmationService.confirmEmail(confirmationToken);
        return confirmed
                ? ResponseEntity.ok("Email confirmed")
                : ResponseEntity.badRequest().body("Bad confirmation token");
    }
}
