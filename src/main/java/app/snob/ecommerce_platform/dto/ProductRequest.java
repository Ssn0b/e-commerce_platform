package app.snob.ecommerce_platform.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    @NotBlank(message = "Name is mandatory")
    private String name;
    @NotBlank(message = "SKU is mandatory")
    private String SKU;
    @NotBlank(message = "Category is mandatory")
    private String category;
    @NotBlank(message = "Description is mandatory")
    private String description;
    @NotNull
    @Min(value=1, message = "Price must be positive")
    private Double price;
    @NotNull
    @Min(value=1, message = "Quantity must be positive")
    private Integer inventoryQuantity;
}
