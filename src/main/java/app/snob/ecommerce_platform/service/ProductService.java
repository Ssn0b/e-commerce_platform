package app.snob.ecommerce_platform.service;

import app.snob.ecommerce_platform.dto.ProductRequest;
import app.snob.ecommerce_platform.dto.ProductResponse;
import app.snob.ecommerce_platform.dto.ProductUpdateRequest;
import app.snob.ecommerce_platform.entity.Product;
import org.apache.coyote.BadRequestException;

import java.util.List;

/**
 * The ProductService interface provides methods to interact with product-related operations.
 */
public interface ProductService {

    /**
     * Adds a new product based on the provided product request.
     *
     * @param productRequest the request containing the details of the product to be added
     * @return the added product
     */
    Product addProduct(ProductRequest productRequest);

    /**
     * Retrieves a list of products based on the provided parameters.
     *
     * @param name     the name of the product (optional)
     * @param category the category of the product (optional)
     * @param minPrice the minimum price of the product (optional)
     * @param maxPrice the maximum price of the product (optional)
     * @param page     the page number for pagination
     * @param size     the page size for pagination
     * @param sort     the sorting criteria
     * @return a list of product responses
     */
    List<ProductResponse> getAllProducts(String name, String category, Double minPrice, Double maxPrice, int page, int size, String[] sort);

    /**
     * Updates an existing product identified by its unique identifier.
     *
     * @param id                   the unique identifier of the product to be updated
     * @param productUpdateRequest the request containing the updated details of the product
     * @return the updated product
     * @throws BadRequestException if the request is invalid
     */
    Product updateProduct(Long id, ProductUpdateRequest productUpdateRequest) throws BadRequestException;

    /**
     * Retrieves a product by its unique identifier.
     *
     * @param id the unique identifier of the product
     * @return the product response
     */
    ProductResponse getProductById(Long id);

    /**
     * Deletes a product by its unique identifier.
     *
     * @param id the unique identifier of the product to be deleted
     */
    void deleteProductById(Long id);
}
