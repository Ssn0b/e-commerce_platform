package app.snob.ecommerce_platform.service.Impl;

import app.snob.ecommerce_platform.dto.*;
import app.snob.ecommerce_platform.entity.*;
import app.snob.ecommerce_platform.entity.enums.Status;
import app.snob.ecommerce_platform.exception.InsufficientInventoryException;
import app.snob.ecommerce_platform.repository.OrderDetailsRepository;
import app.snob.ecommerce_platform.repository.OrderItemsRepository;
import app.snob.ecommerce_platform.repository.ProductRepository;
import app.snob.ecommerce_platform.repository.UserRepository;
import app.snob.ecommerce_platform.service.OrderDetailsService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderDetailsServiceImpl implements OrderDetailsService {
    private final OrderDetailsRepository orderDetailsRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @CacheEvict(value = "orders", allEntries=true)
    @Override
    public CompletableFuture<OrderDetails> createOrderDetails(String email, OrderDetailsRequest orderDetailsRequest) {
        log.info("Creating order details for user with email: {}", email);
        Set<OrderItems> orderItems = orderDetailsRequest.getOrderItems().stream()
                .map(this::mapToOrderItems)
                .collect(Collectors.toSet());

        User user = userRepository.findByEmail(email);

        for (OrderItems orderItem : orderItems) {
                Product product = orderItem.getProduct();
                ProductInventory inventory = product.getInventory();
                int requestedQuantity = orderItem.getQuantity();
                if (requestedQuantity < 0 || inventory == null || inventory.getQuantity() < requestedQuantity) {
                    throw new InsufficientInventoryException("Insufficient inventory for product: " + product.getName());
                }
            }

        Double total = orderItems.stream()
                .mapToDouble(orderItem -> orderItem.getProduct().getPrice() * orderItem.getQuantity())
                .sum();

        OrderDetails orderDetails = orderDetailsRepository.save(OrderDetails.builder()
                .user(user)
                .status(Status.ACTIVE)
                .total(total)
                .build());
        orderItems.forEach(orderItem -> orderItem.setOrderDetails(orderDetails));
        orderItemsRepository.saveAll(orderItems);
        orderDetails.setOrderDetailsList(new ArrayList<>(orderItems));

        return CompletableFuture.completedFuture(orderDetailsRepository.save(orderDetails));
    }

    @Cacheable(value = "orders", key = "{#page}+'.'+{#size}+'.'+{#sort}")
    @Override
    public List<OrderDetailsResponse> getAllOrderDetails(int page, int size, String[] sort) {
        log.info("Getting all order details");
        Sort.Direction direction = sort[1].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));

        return orderDetailsRepository.findAll(pageable).getContent()
                .stream()
                .map(this::mapToOrderDetailsResponse)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDetailsResponse getOrderDetailsById(Long id) {
        log.info("Getting order details by ID: {}", id);
        return orderDetailsRepository.findById(id)
                .map(this::mapToOrderDetailsResponse)
                .orElseThrow(() -> new EntityNotFoundException("Order with ID " + id + " not found"));
    }
    @Override
    public List<OrderDetailsResponse> getOrdersByUser(String email, int page, int size, String[] sort){
        log.info("Getting orders for user with email: {}", email);
        Sort.Direction direction = sort[1].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));

        List<OrderDetails> orderDetails = orderDetailsRepository.findAll(pageable).getContent();

        if (!email.isBlank() && !email.isEmpty()) {
            orderDetails = orderDetails.stream()
                    .filter(order -> order.getUser().getEmail().contains(email))
                    .toList();
            return orderDetails.stream()
                    .map(this::mapToOrderDetailsResponse)
                    .collect(Collectors.toList());
        }else {
            return Collections.emptyList();
        }
    }


    private OrderDetailsResponse mapToOrderDetailsResponse(OrderDetails orderDetails){
        return OrderDetailsResponse.builder()
                .id(orderDetails.getId())
                .user(mapToUserResponse(orderDetails.getUser()))
                .status(orderDetails.getStatus().getStatus())
                .orderItems(orderDetails.getOrderDetailsList().stream()
                        .map(this::mapToOrderItemsResponse)
                        .collect(Collectors.toSet()))
                .total(orderDetails.getTotal())
                .build();
    }
    private UserResponse mapToUserResponse(User user){
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .role(user.getRole())
                .email(user.getEmail())
                .build();
    }
    private OrderItems mapToOrderItems(OrderItemsRequest orderItemsRequest){
        Optional<Product> productOptional  = productRepository.findById(orderItemsRequest.getProductId());
        Product product = productOptional.orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + orderItemsRequest.getProductId()));
        return OrderItems.builder()
                .quantity(orderItemsRequest.getQuantity())
                .product(product)
                .build();
    }

    private OrderItemsResponse mapToOrderItemsResponse(OrderItems orderItems){
        return OrderItemsResponse.builder()
                .id(orderItems.getId())
                .quantity(orderItems.getQuantity())
                .product(mapToProductResponse(orderItems.getProduct()))
                .build();
    }

    private ProductResponse mapToProductResponse(Product product){
        return ProductResponse.builder()
                .id(product.getId())
                .price(product.getPrice())
                .name(product.getName())
                .SKU(product.getSKU())
                .category(mapToProductCategoryResponse(product.getCategory()))
                .inventory(product.getInventory())
                .build();
    }
    private ProductCategoryResponse mapToProductCategoryResponse(ProductCategory productCategory){
        return ProductCategoryResponse.builder()
                .id(productCategory.getId())
                .name(productCategory.getName())
                .build();
    }
}
