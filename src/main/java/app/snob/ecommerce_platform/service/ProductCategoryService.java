package app.snob.ecommerce_platform.service;

import app.snob.ecommerce_platform.dto.ProductCategoryRequest;
import app.snob.ecommerce_platform.dto.ProductCategoryResponse;
import app.snob.ecommerce_platform.entity.ProductCategory;

import java.util.List;

/**
 * The ProductCategoryService interface provides methods to interact with product category-related operations.
 */
public interface ProductCategoryService {

    /**
     * Adds a new product category based on the provided product category request.
     *
     * @param productCategoryRequest the request containing the details of the product category to be added
     * @return the added product category
     */
    ProductCategory addProductCategory(ProductCategoryRequest productCategoryRequest);

    /**
     * Retrieves a list of all product categories.
     *
     * @return a list of product category responses
     */
    List<ProductCategoryResponse> getAllProductCategories();

    /**
     * Retrieves a product category by its unique identifier.
     *
     * @param id the unique identifier of the product category
     * @return the product category response
     */
    ProductCategoryResponse getProductCategoryById(Long id);
}
