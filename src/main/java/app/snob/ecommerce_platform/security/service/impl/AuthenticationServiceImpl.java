package app.snob.ecommerce_platform.security.service.impl;

import app.snob.ecommerce_platform.entity.Token;
import app.snob.ecommerce_platform.entity.User;
import app.snob.ecommerce_platform.entity.enums.Role;
import app.snob.ecommerce_platform.exception.EmailNotConfirmedException;
import app.snob.ecommerce_platform.exception.RegistrationException;
import app.snob.ecommerce_platform.repository.TokenRepository;
import app.snob.ecommerce_platform.repository.UserRepository;
import app.snob.ecommerce_platform.security.dto.AuthenticationRequest;
import app.snob.ecommerce_platform.security.dto.AuthenticationResponse;
import app.snob.ecommerce_platform.security.dto.RegisterRequest;
import app.snob.ecommerce_platform.security.service.AuthenticationService;
import app.snob.ecommerce_platform.security.service.EmailConfirmationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTool jwtTool;
    private final PasswordEncoder passwordEncoder;
    private final EmailConfirmationService emailConfirmationService;


    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())){
            throw new RegistrationException("User with the same email or username already exists.");
        }
        User user = User.builder()
                            .name(request.getName())
                            .email(request.getEmail())
                            .role(Role.USER)
                            .password(passwordEncoder.encode(request.getPassword()))
                            .build();
        UUID confirmationToken = emailConfirmationService.generateConfirmationToken(user);
        var savedUser = userRepository.save(user);
        var jwtToken = jwtTool.generateToken(user);
        var refreshToken = jwtTool.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);

        emailConfirmationService.sendConfirmationEmail(user.getEmail(), String.valueOf(confirmationToken));


        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }


    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail());

        if (!user.isConfirmedByEmail()) {
            throw new EmailNotConfirmedException("Email not confirmed for this user.");
        }

        var jwtToken = jwtTool.generateToken(user);
        var refreshToken = jwtTool.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtTool.extractUsername(refreshToken);
        try {
            if (userEmail != null) {
                var user = this.userRepository.findByEmail(userEmail);
                if (jwtTool.isTokenValid(refreshToken, user)) {
                    var accessToken = jwtTool.generateToken(user);
                    revokeAllUserTokens(user);
                    saveUserToken(user, accessToken);
                    var authResponse = AuthenticationResponse.builder()
                            .accessToken(accessToken)
                            .refreshToken(refreshToken)
                            .build();
                    new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
                }
            }
        }catch (java.io.IOException exception){
            exception.printStackTrace();
        }

    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}
