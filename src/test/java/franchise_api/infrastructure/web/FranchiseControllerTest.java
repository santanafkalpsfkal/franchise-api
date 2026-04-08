package franchise_api.infrastructure.web;

import franchise_api.application.port.in.FranchiseUseCase;
import franchise_api.domain.model.Branch;
import franchise_api.domain.model.Franchise;
import franchise_api.domain.model.Product;
import franchise_api.domain.model.TopStockProduct;
import franchise_api.infrastructure.web.handler.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = FranchiseController.class)
@Import(GlobalExceptionHandler.class)
class FranchiseControllerTest {

	@Autowired
	private WebTestClient webTestClient;

	@MockitoBean
	private FranchiseUseCase franchiseUseCase;

	@Test
	void shouldCreateFranchise() {
		Franchise franchise = new Franchise("fr-1", "Acme", List.of());
		when(franchiseUseCase.createFranchise("Acme")).thenReturn(Mono.just(franchise));

		webTestClient.post()
				.uri("/api/franchises")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue("""
						{"name":"Acme"}
						""")
				.exchange()
				.expectStatus().isCreated()
				.expectBody()
				.jsonPath("$.id").isEqualTo("fr-1")
				.jsonPath("$.name").isEqualTo("Acme");
	}

	@Test
	void shouldReturnValidationErrorForNegativeStock() {
		webTestClient.patch()
				.uri("/api/franchises/fr-1/branches/br-1/products/p-1/stock")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue("""
						{"stock":-1}
						""")
				.exchange()
				.expectStatus().isBadRequest()
				.expectBody()
				.jsonPath("$.code").isEqualTo("VALIDATION_ERROR");
	}

	@Test
	void shouldReturnTopStockProducts() {
		when(franchiseUseCase.getTopStockProductsByBranch(anyString()))
				.thenReturn(Flux.just(new TopStockProduct("br-1", "Centro", "p-2", "Coca-Cola", 11)));

		webTestClient.get()
				.uri("/api/franchises/fr-1/top-stock-products")
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$[0].branchId").isEqualTo("br-1")
				.jsonPath("$[0].productName").isEqualTo("Coca-Cola")
				.jsonPath("$[0].stock").isEqualTo(11);
	}

	@Test
	void shouldDeleteProduct() {
		when(franchiseUseCase.deleteProduct("fr-1", "br-1", "p-1")).thenReturn(Mono.empty());

		webTestClient.delete()
				.uri("/api/franchises/fr-1/branches/br-1/products/p-1")
				.exchange()
				.expectStatus().isNoContent();
	}
}