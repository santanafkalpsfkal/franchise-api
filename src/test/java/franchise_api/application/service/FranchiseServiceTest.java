package franchise_api.application.service;

import franchise_api.application.port.out.FranchiseRepository;
import franchise_api.domain.exception.NotFoundException;
import franchise_api.domain.model.Branch;
import franchise_api.domain.model.Franchise;
import franchise_api.domain.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FranchiseServiceTest {

	@Mock
	private FranchiseRepository franchiseRepository;

	private FranchiseService franchiseService;

	@BeforeEach
	void setUp() {
		franchiseService = new FranchiseService(franchiseRepository);
	}

	@Test
	void shouldCreateFranchise() {
		when(franchiseRepository.save(any(Franchise.class)))
				.thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

		StepVerifier.create(franchiseService.createFranchise("Acme"))
				.assertNext(franchise -> {
					org.junit.jupiter.api.Assertions.assertEquals("Acme", franchise.name());
					org.junit.jupiter.api.Assertions.assertTrue(franchise.branches().isEmpty());
				})
				.verifyComplete();
	}

	@Test
	void shouldAddProductToBranch() {
		Franchise franchise = new Franchise(
				"fr-1",
				"Acme",
				List.of(new Branch("br-1", "Centro", List.of()))
		);

		when(franchiseRepository.findById("fr-1")).thenReturn(Mono.just(franchise));
		when(franchiseRepository.save(any(Franchise.class)))
				.thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

		StepVerifier.create(franchiseService.addProduct("fr-1", "br-1", "Coca-Cola", 10))
				.assertNext(updated -> {
					Product product = updated.branches().get(0).products().get(0);
					org.junit.jupiter.api.Assertions.assertEquals("Coca-Cola", product.name());
					org.junit.jupiter.api.Assertions.assertEquals(10, product.stock());
				})
				.verifyComplete();
	}

	@Test
	void shouldReturnTopStockProductPerBranch() {
		Franchise franchise = new Franchise(
				"fr-1",
				"Acme",
				List.of(
						new Branch(
								"br-1",
								"Centro",
								List.of(
										new Product("p-1", "A", 3),
										new Product("p-2", "B", 11)
								)
						),
						new Branch(
								"br-2",
								"Norte",
								List.of(
										new Product("p-3", "C", 7),
										new Product("p-4", "D", 4)
								)
						)
				)
		);

		when(franchiseRepository.findById("fr-1")).thenReturn(Mono.just(franchise));

		StepVerifier.create(franchiseService.getTopStockProductsByBranch("fr-1"))
				.expectNextMatches(product -> product.branchId().equals("br-1") && product.productId().equals("p-2") && product.stock() == 11)
				.expectNextMatches(product -> product.branchId().equals("br-2") && product.productId().equals("p-3") && product.stock() == 7)
				.verifyComplete();
	}

	@Test
	void shouldFailWhenFranchiseDoesNotExist() {
		when(franchiseRepository.findById("missing")).thenReturn(Mono.empty());

		StepVerifier.create(franchiseService.addBranch("missing", "Centro"))
				.expectErrorSatisfies(error -> org.junit.jupiter.api.Assertions.assertInstanceOf(NotFoundException.class, error))
				.verify();
	}
}