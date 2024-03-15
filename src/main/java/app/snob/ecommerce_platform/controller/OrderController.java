package app.snob.ecommerce_platform.controller;

import app.snob.ecommerce_platform.dto.OrderDetailsRequest;
import app.snob.ecommerce_platform.dto.OrderDetailsResponse;
import app.snob.ecommerce_platform.dto.ProductRequest;
import app.snob.ecommerce_platform.dto.ProductResponse;
import app.snob.ecommerce_platform.service.OrderDetailsService;
import app.snob.ecommerce_platform.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderDetailsService orderDetailsService;

    @PostMapping
    @Operation(summary = "Create Order Details")
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "200", description  = "Order created successfully"),
            @ApiResponse(responseCode  = "400", description  = "Bad request")
    })
    public ResponseEntity<String> createOrderDetails(
            Principal principal,
            @Valid @RequestBody OrderDetailsRequest orderDetailsRequest) throws ExecutionException, InterruptedException {
        String email = principal.getName();
        orderDetailsService.createOrderDetails(email, orderDetailsRequest).get();
        return ResponseEntity.ok("Order created successfully");
    }

    @GetMapping
    @Operation(summary = "Get All Order Details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of order details retrieved successfully")
    })
    public List<OrderDetailsResponse> getOrderDetails(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort) {
        return orderDetailsService.getAllOrderDetails(page, size, sort);
    }

    @GetMapping("/my-orders")
    @Operation(summary = "Get Logged-In User Orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of user orders retrieved successfully")
    })
    public List<OrderDetailsResponse> getLoggedInUserOrders(
            Principal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort) {
        String email = principal.getName();
        return orderDetailsService.getOrdersByUser(email, page, size, sort);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Order Details by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order details retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public OrderDetailsResponse getOrderDetails(@PathVariable Long id) {
        return orderDetailsService.getOrderDetailsById(id);
    }
}
