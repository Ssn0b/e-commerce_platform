package app.snob.ecommerce_platform.dto;

import app.snob.ecommerce_platform.entity.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private UUID id;
    private String name;
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role;
}
