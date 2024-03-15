package app.snob.ecommerce_platform.entity.audit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Getter
@MappedSuperclass
public class DateAudit {
    @JsonIgnore
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAtInUtc;
    @JsonIgnore
    @UpdateTimestamp
    @Column(nullable = false)
    private Instant modifiedAtInUtc = Instant.now();
}
