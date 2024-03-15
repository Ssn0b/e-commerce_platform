package app.snob.ecommerce_platform.dto;

import app.snob.ecommerce_platform.entity.ProductCategory;
import app.snob.ecommerce_platform.entity.ProductInventory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private String SKU;
    private String description;
    private Double price;
    private ProductCategoryResponse category;
    private ProductInventory inventory;
}
