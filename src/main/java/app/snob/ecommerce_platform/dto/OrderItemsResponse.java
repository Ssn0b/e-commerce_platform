package app.snob.ecommerce_platform.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemsResponse {
    private Long id;
    private Integer quantity;
    private ProductResponse product;
}
