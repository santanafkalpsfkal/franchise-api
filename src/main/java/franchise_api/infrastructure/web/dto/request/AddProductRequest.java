package franchise_api.infrastructure.web.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddProductRequest(
		@NotBlank(message = "Product name is required")
		String name,
		@NotNull(message = "Product stock is required")
		@Min(value = 0, message = "Product stock cannot be negative")
		Integer stock) {
}