package franchise_api.infrastructure.web.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AddBranchRequest(
		@NotBlank(message = "Branch name is required")
		String name) {
}