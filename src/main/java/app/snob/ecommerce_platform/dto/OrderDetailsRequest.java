package app.snob.ecommerce_platform.dto;

import app.snob.ecommerce_platform.entity.OrderItems;
import app.snob.ecommerce_platform.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailsRequest {
    @NotNull
    private List<OrderItemsRequest> orderItems;
}
