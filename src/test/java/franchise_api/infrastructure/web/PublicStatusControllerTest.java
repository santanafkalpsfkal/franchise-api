package franchise_api.infrastructure.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(controllers = PublicStatusController.class)
class PublicStatusControllerTest {

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void shouldExposeRootStatus() {
		webTestClient.get()
				.uri("/")
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.application").isEqualTo("franchise-api")
				.jsonPath("$.status").isEqualTo("UP")
				.jsonPath("$.healthEndpoint").isEqualTo("/actuator/health")
				.jsonPath("$.franchisesEndpoint").isEqualTo("/api/franchises");
	}

	@Test
	void shouldExposeHealthAlias() {
		webTestClient.get()
				.uri("/health")
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.application").isEqualTo("franchise-api")
				.jsonPath("$.status").isEqualTo("UP");
	}
}