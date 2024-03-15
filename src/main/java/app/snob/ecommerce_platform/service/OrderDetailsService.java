package app.snob.ecommerce_platform.service;

import app.snob.ecommerce_platform.dto.OrderDetailsRequest;
import app.snob.ecommerce_platform.dto.OrderDetailsResponse;
import app.snob.ecommerce_platform.entity.OrderDetails;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * The OrderDetailsService interface provides methods to manage order details.
 */
public interface OrderDetailsService {

    /**
     * Creates order details asynchronously for the specified email and order details request.
     *
     * @param email              the email of the user creating the order
     * @param orderDetailsRequest the request containing order details information
     * @return a CompletableFuture representing the asynchronous operation result
     */
    CompletableFuture<OrderDetails> createOrderDetails(String email, OrderDetailsRequest orderDetailsRequest);

    /**
     * Retrieves all order details based on pagination and sorting parameters.
     *
     * @param page the page number
     * @param size the number of items per page
     * @param sort the sorting parameters in the format [fieldName,direction]
     * @return a list of order details responses
     */
    List<OrderDetailsResponse> getAllOrderDetails(int page, int size, String[] sort);

    /**
     * Retrieves order details by ID.
     *
     * @param id the ID of the order details
     * @return the order details response
     */
    OrderDetailsResponse getOrderDetailsById(Long id);

    /**
     * Retrieves orders for a specific user based on pagination and sorting parameters.
     *
     * @param email the email of the user
     * @param page  the page number
     * @param size  the number of items per page
     * @param sort  the sorting parameters in the format [fieldName,direction]
     * @return a list of order details responses belonging to the user
     */
    List<OrderDetailsResponse> getOrdersByUser(String email, int page, int size, String[] sort);
}
