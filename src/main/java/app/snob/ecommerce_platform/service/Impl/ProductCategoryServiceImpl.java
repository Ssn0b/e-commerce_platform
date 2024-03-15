package app.snob.ecommerce_platform.service.Impl;

import app.snob.ecommerce_platform.dto.ProductCategoryRequest;
import app.snob.ecommerce_platform.dto.ProductCategoryResponse;
import app.snob.ecommerce_platform.entity.ProductCategory;
import app.snob.ecommerce_platform.repository.ProductCategoryRepository;
import app.snob.ecommerce_platform.service.ProductCategoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductCategoryServiceImpl implements ProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;

    @Override
    public ProductCategory addProductCategory(ProductCategoryRequest productCategoryRequest) {
        log.info("Adding product category: {}", productCategoryRequest);
        ProductCategory productCategory = ProductCategory.builder()
                .name(productCategoryRequest.getName())
                .build();
        return productCategoryRepository.save(productCategory);
    }

    @Override
    public List<ProductCategoryResponse> getAllProductCategories() {
        log.info("Fetching all product categories");
        return productCategoryRepository.findAll()
                .stream()
                .map(this::mapToProductCategoryResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProductCategoryResponse getProductCategoryById(Long id) {
        log.info("Fetching product category by ID: {}", id);
        return productCategoryRepository.findById(id)
                .map(this::mapToProductCategoryResponse)
                .orElseThrow(() -> new EntityNotFoundException("Product category with ID " + id + " not found"));
    }
    private ProductCategoryResponse mapToProductCategoryResponse(ProductCategory productCategory){
        return ProductCategoryResponse.builder()
                .id(productCategory.getId())
                .name(productCategory.getName())
                .build();
    }
}
