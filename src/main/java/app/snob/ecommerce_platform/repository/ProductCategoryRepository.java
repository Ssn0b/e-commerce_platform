package app.snob.ecommerce_platform.repository;

import app.snob.ecommerce_platform.entity.ProductCategory;
import app.snob.ecommerce_platform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
    ProductCategory findByName(String name);
    boolean existsByName(String name);
}
