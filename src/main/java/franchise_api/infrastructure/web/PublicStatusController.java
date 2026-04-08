package franchise_api.infrastructure.web;

import franchise_api.infrastructure.web.dto.response.PublicStatusResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class PublicStatusController {

	@GetMapping("/")
	public Mono<PublicStatusResponse> root() {
		return Mono.just(new PublicStatusResponse(
				"franchise-api",
				"UP",
				"/actuator/health",
				"/api/franchises"));
	}

	@GetMapping("/health")
	public Mono<PublicStatusResponse> health() {
		return Mono.just(new PublicStatusResponse(
				"franchise-api",
				"UP",
				"/actuator/health",
				"/api/franchises"));
	}
}