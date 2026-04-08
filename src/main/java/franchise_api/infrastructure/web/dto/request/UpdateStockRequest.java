package franchise_api.infrastructure.web.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateStockRequest(
		@NotNull(message = "Product stock is required")
		@Min(value = 0, message = "Product stock cannot be negative")
		Integer stock) {
}