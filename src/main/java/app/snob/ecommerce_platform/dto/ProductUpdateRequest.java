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
public class ProductUpdateRequest {
    private String name;
    private String SKU;
    private String category;
    private String description;
    @Min(value = 0, message = "Price must be positive")
    private Double price;
    @Min(value = 0, message = "Price must be positive")
    private Integer inventoryQuantity;
}
