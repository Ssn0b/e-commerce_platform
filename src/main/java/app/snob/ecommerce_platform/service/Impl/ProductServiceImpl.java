package app.snob.ecommerce_platform.service.Impl;

import app.snob.ecommerce_platform.dto.*;
import app.snob.ecommerce_platform.entity.Product;
import app.snob.ecommerce_platform.entity.ProductCategory;
import app.snob.ecommerce_platform.entity.ProductInventory;
import app.snob.ecommerce_platform.repository.ProductCategoryRepository;
import app.snob.ecommerce_platform.repository.ProductInventoryRepository;
import app.snob.ecommerce_platform.repository.ProductRepository;
import app.snob.ecommerce_platform.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductInventoryRepository productInventoryRepository;
    @CacheEvict(value = "products", allEntries=true)
    @Override
    public Product addProduct(ProductRequest productRequest) {
        log.info("Adding product: {}", productRequest);
        if (productRequest.getCategory() == null || !productCategoryRepository.existsByName(productRequest.getCategory())){
            log.error("EntityNotFoundException thrown at addProduct method" );
            throw new EntityNotFoundException("Category invalid");
        }

        ProductCategory productCategory =
                productCategoryRepository.findByName(productRequest.getCategory());

        ProductInventory productInventory = productInventoryRepository.save(
                ProductInventory.builder()
                .quantity(productRequest.getInventoryQuantity())
                .build()
        );

        Product product = Product.builder()
                .name(productRequest.getName())
                .SKU(productRequest.getSKU())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .category(productCategory)
                .inventory(productInventory)
                .build();

        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long id, ProductUpdateRequest productUpdateRequest) throws BadRequestException {
        log.info("Updating product with ID {}: {}", id, productUpdateRequest);
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty()) {
            log.error("EntityNotFoundException thrown at updateProduct method" );
            throw new EntityNotFoundException("Product with id "+ id +" does not exist");
        }

        Product product = optionalProduct.get();

        if (productUpdateRequest != null) {
            if (productUpdateRequest.getName() != null && !productUpdateRequest.getName().isBlank()) {
                product.setName(productUpdateRequest.getName());
            }
            if (productUpdateRequest.getPrice() != null) {
                product.setPrice(productUpdateRequest.getPrice());
            }
            if (productUpdateRequest.getSKU() != null && !productUpdateRequest.getSKU().isBlank()) {
                product.setSKU(productUpdateRequest.getSKU());
            }
            if (productUpdateRequest.getDescription() != null && !productUpdateRequest.getDescription().isBlank()) {
                product.setDescription(productUpdateRequest.getDescription());
            }
            if (productUpdateRequest.getCategory() != null && !productUpdateRequest.getCategory().isBlank()) {
                ProductCategory productCategory = product.getCategory();
                productCategory.setName(productUpdateRequest.getName());
                productCategoryRepository.save(productCategory);
                product.setCategory(productCategory);
            }
            if (productUpdateRequest.getInventoryQuantity() != null) {
                ProductInventory productInventory = product.getInventory();
                productInventory.setQuantity(productUpdateRequest.getInventoryQuantity());
                productInventoryRepository.save(productInventory);
                product.setInventory(productInventory);
            }
            return productRepository.save(product);
        }
        else {
            log.error("BadRequestException thrown at updateProduct method" );
            throw new BadRequestException("Invalid ProductUpdateRequest");
        }
    }
    @Cacheable(value = "products", key = "{#name}+'.'+{#category}+'.'+{#minPrice}+'.'+{#maxPrice}+'.'+{#page}+'.'+{#size}+'.'+{#sort}")
    @Override
    public List<ProductResponse> getAllProducts(String name, String category, Double minPrice,
                                                Double maxPrice, int page, int size, String[] sort) {
        log.info("Fetching all products with parameters: name={}, category={}, minPrice={}, maxPrice={}, page={}, size={}, sort={}",
                name, category, minPrice, maxPrice, page, size, sort);
        Sort.Direction direction = sort[1].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));
        Specification<Product> specification = buildSpecification(name, category, minPrice, maxPrice);

        List<Product> products = productRepository.findAll(specification, pageable).getContent();

        return products.stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponse getProductById(Long id) {
        log.info("Fetching product by ID: {}", id);
        return productRepository.findById(id)
                .map(this::mapToProductResponse)
                .orElseThrow(() -> new EntityNotFoundException("Product with ID " + id + " not found"));
    }

    @CacheEvict(value = "products", allEntries=true)
    @Override
    public void deleteProductById(Long id) {
        log.info("Deleting product by ID: {}", id);
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            ProductInventory inventory = product.getInventory();

            product.setInventory(null);
            productRepository.save(product);

            productInventoryRepository.delete(inventory);
            productRepository.deleteById(id);
        } else {
            log.error("EntityNotFoundException thrown at deleteProductById method" );
            throw new EntityNotFoundException("Product with ID " + id + " not found");
        }
    }

    private Specification<Product> buildSpecification(String name, String category, Double minPrice, Double maxPrice) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (Objects.nonNull(name)) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name + "%"));
            }

            if (Objects.nonNull(category)) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("category").get("name")), "%" + category + "%"));
            }

            if (Objects.nonNull(minPrice)) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice));
            }

            if (Objects.nonNull(maxPrice)) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private ProductResponse mapToProductResponse(Product product){
        return ProductResponse.builder()
                .id(product.getId())
                .price(product.getPrice())
                .name(product.getName())
                .SKU(product.getSKU())
                .category(mapToProductCategoryResponse(product.getCategory()))
                .inventory(product.getInventory())
                .build();
    }
    private ProductCategoryResponse mapToProductCategoryResponse(ProductCategory productCategory){
        return ProductCategoryResponse.builder()
                .id(productCategory.getId())
                .name(productCategory.getName())
                .build();
    }
}
