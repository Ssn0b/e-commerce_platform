package app.snob.ecommerce_platform.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InvalidEmailException extends RuntimeException {
    private String message;

    public InvalidEmailException(String message) {
        this.message = message;
    }
}
