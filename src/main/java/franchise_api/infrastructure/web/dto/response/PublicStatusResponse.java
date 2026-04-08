package franchise_api.infrastructure.web.dto.response;

public record PublicStatusResponse(
		String application,
		String status,
		String healthEndpoint,
		String franchisesEndpoint) {
}