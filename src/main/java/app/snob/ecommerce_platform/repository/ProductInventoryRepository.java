package app.snob.ecommerce_platform.repository;

import app.snob.ecommerce_platform.entity.ProductInventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductInventoryRepository extends JpaRepository<ProductInventory, Long> {

}
