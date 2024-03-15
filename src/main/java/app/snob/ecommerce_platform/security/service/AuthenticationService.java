package app.snob.ecommerce_platform.security.service;

import app.snob.ecommerce_platform.security.dto.AuthenticationRequest;
import app.snob.ecommerce_platform.security.dto.AuthenticationResponse;
import app.snob.ecommerce_platform.security.dto.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
    void refreshToken(HttpServletRequest request, HttpServletResponse response);
}
