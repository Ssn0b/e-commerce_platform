package app.snob.ecommerce_platform.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {
    ACTIVE("Active"),
    CANCELED("Canceled"),
    PROCESSED("Processed");
    private final String status;
}
