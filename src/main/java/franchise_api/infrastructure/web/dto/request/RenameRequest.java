package franchise_api.infrastructure.web.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RenameRequest(
		@NotBlank(message = "Name is required")
		String name) {
}