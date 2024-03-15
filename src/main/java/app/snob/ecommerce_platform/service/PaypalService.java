package app.snob.ecommerce_platform.service;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;

import java.util.Map;

/**
 * The PaypalService interface provides methods to interact with PayPal-related operations.
 */
public interface PaypalService {

    /**
     * Creates a PayPal payment for the specified email and order ID.
     *
     * @param email   the email of the user making the payment
     * @param orderId the ID of the order for which the payment is being created
     * @return a map containing information about the created payment
     */
    Map<String, Object> createPayment(String email, Long orderId);

    /**
     * Completes a PayPal payment based on the information provided in the HTTP request.
     *
     * @param req the HTTP request containing payment information from PayPal
     * @return a response entity indicating the status of the payment completion process
     */
    ResponseEntity<String> completePayment(HttpServletRequest req);

    /**
     * Cancels a PayPal payment based on the information provided in the HTTP request.
     *
     * @param req the HTTP request containing payment information from PayPal
     */
    void cancelPayment(HttpServletRequest req);
}
