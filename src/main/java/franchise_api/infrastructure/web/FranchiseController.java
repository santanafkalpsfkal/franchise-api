package franchise_api.infrastructure.web;

import franchise_api.application.port.in.FranchiseUseCase;
import franchise_api.infrastructure.web.dto.request.AddBranchRequest;
import franchise_api.infrastructure.web.dto.request.AddProductRequest;
import franchise_api.infrastructure.web.dto.request.CreateFranchiseRequest;
import franchise_api.infrastructure.web.dto.request.RenameRequest;
import franchise_api.infrastructure.web.dto.request.UpdateStockRequest;
import franchise_api.infrastructure.web.dto.response.FranchiseResponse;
import franchise_api.infrastructure.web.dto.response.TopStockProductResponse;
import franchise_api.infrastructure.web.mapper.FranchiseResponseMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/franchises")
public class FranchiseController {

	private final FranchiseUseCase franchiseUseCase;

	public FranchiseController(FranchiseUseCase franchiseUseCase) {
		this.franchiseUseCase = franchiseUseCase;
	}

	@PostMapping
	public Mono<ResponseEntity<FranchiseResponse>> createFranchise(@Valid @RequestBody Mono<CreateFranchiseRequest> request) {
		return request
				.flatMap(body -> franchiseUseCase.createFranchise(body.name()))
				.map(FranchiseResponseMapper::toResponse)
				.map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
	}

	@PostMapping("/{franchiseId}/branches")
	public Mono<ResponseEntity<FranchiseResponse>> addBranch(
			@PathVariable String franchiseId,
			@Valid @RequestBody Mono<AddBranchRequest> request) {
		return request
				.flatMap(body -> franchiseUseCase.addBranch(franchiseId, body.name()))
				.map(FranchiseResponseMapper::toResponse)
				.map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
	}

	@PostMapping("/{franchiseId}/branches/{branchId}/products")
	public Mono<ResponseEntity<FranchiseResponse>> addProduct(
			@PathVariable String franchiseId,
			@PathVariable String branchId,
			@Valid @RequestBody Mono<AddProductRequest> request) {
		return request
				.flatMap(body -> franchiseUseCase.addProduct(franchiseId, branchId, body.name(), body.stock()))
				.map(FranchiseResponseMapper::toResponse)
				.map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
	}

	@DeleteMapping("/{franchiseId}/branches/{branchId}/products/{productId}")
	public Mono<ResponseEntity<Void>> deleteProduct(
			@PathVariable String franchiseId,
			@PathVariable String branchId,
			@PathVariable String productId) {
		return franchiseUseCase.deleteProduct(franchiseId, branchId, productId)
				.thenReturn(ResponseEntity.noContent().build());
	}

	@PatchMapping("/{franchiseId}/branches/{branchId}/products/{productId}/stock")
	public Mono<FranchiseResponse> updateProductStock(
			@PathVariable String franchiseId,
			@PathVariable String branchId,
			@PathVariable String productId,
			@Valid @RequestBody Mono<UpdateStockRequest> request) {
		return request
				.flatMap(body -> franchiseUseCase.updateProductStock(franchiseId, branchId, productId, body.stock()))
				.map(FranchiseResponseMapper::toResponse);
	}

	@GetMapping("/{franchiseId}/top-stock-products")
	public Flux<TopStockProductResponse> getTopStockProductsByBranch(@PathVariable String franchiseId) {
		return franchiseUseCase.getTopStockProductsByBranch(franchiseId)
				.map(FranchiseResponseMapper::toResponse);
	}

	@PatchMapping("/{franchiseId}/name")
	public Mono<FranchiseResponse> renameFranchise(
			@PathVariable String franchiseId,
			@Valid @RequestBody Mono<RenameRequest> request) {
		return request
				.flatMap(body -> franchiseUseCase.renameFranchise(franchiseId, body.name()))
				.map(FranchiseResponseMapper::toResponse);
	}

	@PatchMapping("/{franchiseId}/branches/{branchId}/name")
	public Mono<FranchiseResponse> renameBranch(
			@PathVariable String franchiseId,
			@PathVariable String branchId,
			@Valid @RequestBody Mono<RenameRequest> request) {
		return request
				.flatMap(body -> franchiseUseCase.renameBranch(franchiseId, branchId, body.name()))
				.map(FranchiseResponseMapper::toResponse);
	}

	@PatchMapping("/{franchiseId}/branches/{branchId}/products/{productId}/name")
	public Mono<FranchiseResponse> renameProduct(
			@PathVariable String franchiseId,
			@PathVariable String branchId,
			@PathVariable String productId,
			@Valid @RequestBody Mono<RenameRequest> request) {
		return request
				.flatMap(body -> franchiseUseCase.renameProduct(franchiseId, branchId, productId, body.name()))
				.map(FranchiseResponseMapper::toResponse);
	}
}