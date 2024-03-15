package app.snob.ecommerce_platform.entity;

import app.snob.ecommerce_platform.entity.audit.DateAudit;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "product", indexes = @Index(name = "id_index",columnList = "id"))
public class Product extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String SKU;
    private String description;
    private Double price;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private ProductCategory category;
    @OneToOne
    @JoinColumn(name = "inventory_id")
    private ProductInventory inventory;
}
