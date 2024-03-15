package app.snob.ecommerce_platform.entity;

import app.snob.ecommerce_platform.entity.audit.DateAudit;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "order")
public class OrderItems extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer quantity;

}
