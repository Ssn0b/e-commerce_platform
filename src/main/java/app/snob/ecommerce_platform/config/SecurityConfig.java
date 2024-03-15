package app.snob.ecommerce_platform.config;

import app.snob.ecommerce_platform.exception.CustomAccessDeniedHandler;
import app.snob.ecommerce_platform.exception.JwtAuthenticationEntryPoint;
import app.snob.ecommerce_platform.security.filter.JwtAuthenticationFilter;
import jakarta.persistence.AttributeOverride;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static app.snob.ecommerce_platform.entity.enums.Role.ADMIN;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationEntryPoint unauthorizedHandler;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/v1/auth/**",
                                "/v3/**",
                                "/api/v1/payment/**")
                        .permitAll()
                        .requestMatchers(GET,
                                "/api/v1/product",
                                "/api/v1/product/{id}",
                                "/api/v1/product/category",
                                "/api/v1/product/category/{id}")
                        .permitAll()
                        .requestMatchers(GET,
                                "/api/v1/order",
                                "/api/v1/order/{id}",
                                "/api/v1/user",
                                "/api/v1/user/{id}",
                                "/api/v1/user/email/{email}")
                        .hasAnyRole(ADMIN.name())
                        .requestMatchers(DELETE,
                                "/api/v1/product/{id}")
                        .hasAnyRole(ADMIN.name())
                        .requestMatchers(POST,
                                "/api/v1/product",
                                "/api/v1/product/category")
                        .hasAnyRole(ADMIN.name())
                        .requestMatchers(POST,
                                "/api/v1/order")
                        .authenticated()
                        .anyRequest().permitAll()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(unauthorizedHandler)
                        .accessDeniedHandler(accessDeniedHandler)
                );
        return http.build();
    }
}
