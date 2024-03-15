package app.snob.ecommerce_platform.controller;

import app.snob.ecommerce_platform.dto.OrderDetailsRequest;
import app.snob.ecommerce_platform.dto.OrderDetailsResponse;
import app.snob.ecommerce_platform.dto.ProductRequest;
import app.snob.ecommerce_platform.dto.ProductResponse;
import app.snob.ecommerce_platform.service.OrderDetailsService;
import app.snob.ecommerce_platform.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderDetailsService orderDetailsService;

    @PostMapping
    public ResponseEntity<String> createOrderDetails(
            Principal principal,
            @RequestBody OrderDetailsRequest orderDetailsRequest
    ) {
        String email = principal.getName();
        orderDetailsService.createOrderDetails(email, orderDetailsRequest);
        return ResponseEntity.ok("Order created successfully");
    }
    @GetMapping
    public List<OrderDetailsResponse> getOrderDetails(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort
    ) {
        return orderDetailsService.getAllOrderDetails(page, size, sort);
    }

    @GetMapping("/my-orders")
    public List<OrderDetailsResponse> getLoggedInUserOrders(
            Principal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort
    ) {
        String email = principal.getName();
        return orderDetailsService.getOrdersByUser(email, page, size, sort);
    }

    @GetMapping("/{id}")
    public OrderDetailsResponse getOrderDetails(@PathVariable Long id) {
        return orderDetailsService.getOrderDetailsById(id);
    }
}
