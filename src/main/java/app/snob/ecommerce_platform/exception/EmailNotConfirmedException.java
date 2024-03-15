package app.snob.ecommerce_platform.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EmailNotConfirmedException extends RuntimeException {
    private String message;

    public EmailNotConfirmedException(String message) {
        this.message = message;
    }
}
