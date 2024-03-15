package app.snob.ecommerce_platform.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductInventoryRequest {
    @Min(value = 1, message = "Quantity must be positive")
    private Integer quantity;
}
