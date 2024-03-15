package app.snob.ecommerce_platform.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@ToString
public class RegisterRequest {
    @Pattern(
            regexp = "^(?!.*\\.\\.)(?!.*\\.$)(?!.*\\-\\-)"
                    + "(?=[ЄІЇҐЁА-ЯA-Z])"
                    + "[ЄІЇҐЁєіїґёА-Яа-яA-Za-z0-9\\s-'’.\\\"]"
                    + "{1,30}"
                    + "(?<![ЭэЁёъЪЫы])$",
            message = "Incorrect name")
    private String name;
    @NotBlank
    @Email(
            regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$",
            message = "Incorrect email")
    private String email;
    @NotBlank
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,30}", message = "Invalid password")
    private String password;
}
