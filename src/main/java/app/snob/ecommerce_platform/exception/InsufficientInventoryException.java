package app.snob.ecommerce_platform.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InsufficientInventoryException extends RuntimeException {
    private String message;

    public InsufficientInventoryException(String message) {
        this.message = message;
    }

}
