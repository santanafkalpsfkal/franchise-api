package franchise_api.infrastructure.web;

import franchise_api.application.port.in.command.CreateFranchiseCommand;
import franchise_api.application.port.in.command.RenameProductCommand;
import franchise_api.application.port.in.FranchiseUseCase;
import franchise_api.domain.model.Branch;
import franchise_api.domain.model.Franchise;
import franchise_api.domain.model.Product;
import franchise_api.domain.model.TopStockProduct;
import franchise_api.infrastructure.web.handler.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
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
	void shouldReturnAllFranchises() {
		when(franchiseUseCase.getAllFranchises()).thenReturn(Flux.just(
				new Franchise("fr-1", "Acme", List.of()),
				new Franchise("fr-2", "Burger House", List.of())
		));

		webTestClient.get()
				.uri("/api/franchises")
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$[0].id").isEqualTo("fr-1")
				.jsonPath("$[0].name").isEqualTo("Acme")
				.jsonPath("$[1].id").isEqualTo("fr-2")
				.jsonPath("$[1].name").isEqualTo("Burger House");
	}

	@Test
	void shouldCreateFranchise() {
		Franchise franchise = new Franchise("fr-1", "Acme", List.of());
		when(franchiseUseCase.createFranchise(any(CreateFranchiseCommand.class))).thenReturn(Mono.just(franchise));

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
	void shouldRenameProduct() {
		Franchise franchise = new Franchise(
				"fr-1",
				"Acme",
				List.of(new Branch("br-1", "Centro", List.of(new Product("p-1", "Pepsi", 10))))
		);
		when(franchiseUseCase.renameProduct(any(RenameProductCommand.class))).thenReturn(Mono.just(franchise));

		webTestClient.patch()
				.uri("/api/franchises/fr-1/branches/br-1/products/p-1/name")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue("""
						{"name":"Pepsi"}
						""")
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.branches[0].products[0].name").isEqualTo("Pepsi");
	}

	@Test
	void shouldDeleteProduct() {
		when(franchiseUseCase.deleteProduct("fr-1", "br-1", "p-1")).thenReturn(Mono.empty());

		webTestClient.delete()
				.uri("/api/franchises/fr-1/branches/br-1/products/p-1")
				.exchange()
				.expectStatus().isNoContent();
	}

	@Test
	void shouldReturnInternalErrorWhenUnexpectedFailureOccurs() {
		when(franchiseUseCase.getAllFranchises()).thenReturn(Flux.error(new IllegalStateException("boom")));

		webTestClient.get()
				.uri("/api/franchises")
				.exchange()
				.expectStatus().isEqualTo(500)
				.expectBody()
				.jsonPath("$.code").isEqualTo("INTERNAL_ERROR")
				.jsonPath("$.message").isEqualTo("Unexpected server error");
	}

	@Test
	void shouldReturnServiceUnavailableWhenDatabaseFails() {
		when(franchiseUseCase.getAllFranchises())
				.thenReturn(Flux.error(new DataAccessResourceFailureException("mongo unavailable")));

		webTestClient.get()
				.uri("/api/franchises")
				.exchange()
				.expectStatus().isEqualTo(503)
				.expectBody()
				.jsonPath("$.code").isEqualTo("DATABASE_UNAVAILABLE")
				.jsonPath("$.message").isEqualTo("Database temporarily unavailable");
	}
}