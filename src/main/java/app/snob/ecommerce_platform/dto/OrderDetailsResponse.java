package app.snob.ecommerce_platform.dto;

import app.snob.ecommerce_platform.entity.OrderItems;
import app.snob.ecommerce_platform.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailsResponse {
    private Long id;
    private Set<OrderItemsResponse> orderItems;
    private UserResponse user;
    private Double total;
    private String status;
}
