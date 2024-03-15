package app.snob.ecommerce_platform.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegistrationException extends RuntimeException {
    private String message;

    public RegistrationException(String message) {
        this.message = message;
    }

}
