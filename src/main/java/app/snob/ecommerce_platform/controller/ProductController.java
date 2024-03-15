package app.snob.ecommerce_platform.controller;

import app.snob.ecommerce_platform.dto.*;
import app.snob.ecommerce_platform.service.ProductCategoryService;
import app.snob.ecommerce_platform.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final ProductCategoryService productCategoryService;

    @PostMapping
    @Operation(summary = "Add Product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product added successfully")
    })
    public ResponseEntity<String> addProduct(@Valid @RequestBody ProductRequest productRequest) {
        productService.addProduct(productRequest);
        return ResponseEntity.ok("Product added successfully");
    }
    @GetMapping
    @Operation(summary = "Get Products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of products retrieved successfully")
    })
    public List<ProductResponse> getProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort
    ) {
        return productService.getAllProducts(name, category, minPrice, maxPrice, page, size, sort);
    }
    @GetMapping("/{id}")
    @Operation(summary = "Get Product by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ProductResponse getProduct(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<Void> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductUpdateRequest productUpdateRequest) {
        try {
            productService.updateProduct(id, productUpdateRequest);
            return ResponseEntity.ok().build();
        }  catch (BadRequestException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product deleted successfully")
    })
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProductById(id);
    }

    @PostMapping("/category")
    @Operation(summary = "Add Product Category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product category added successfully")
    })
    public ResponseEntity<String> addProductCategory(@Valid @RequestBody ProductCategoryRequest productCategoryRequest) {
        productCategoryService.addProductCategory(productCategoryRequest);
        return ResponseEntity.ok("Product category added successfully");
    }

    @GetMapping("/category")
    @Operation(summary = "Get Product Categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of product categories retrieved successfully")
    })
    public List<ProductCategoryResponse> getProductCategories() {
        return productCategoryService.getAllProductCategories();
    }

    @GetMapping("/category/{id}")
    @Operation(summary = "Get Product Category by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product category retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Product category not found")
    })
    public ProductCategoryResponse getProductCategory(@PathVariable Long id) {
        return productCategoryService.getProductCategoryById(id);
    }
}

