package app.snob.ecommerce_platform.entity;

import app.snob.ecommerce_platform.entity.audit.DateAudit;
import app.snob.ecommerce_platform.entity.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "order_details")
public class OrderDetails extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(mappedBy = "orderDetails", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<OrderItems> orderDetailsList;
    private Double total;
    @Enumerated(EnumType.STRING)
    private Status status;
}
