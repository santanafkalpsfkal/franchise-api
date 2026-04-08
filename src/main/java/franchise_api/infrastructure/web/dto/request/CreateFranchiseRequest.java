package franchise_api.infrastructure.web.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateFranchiseRequest(
		@NotBlank(message = "Franchise name is required")
		String name) {
}