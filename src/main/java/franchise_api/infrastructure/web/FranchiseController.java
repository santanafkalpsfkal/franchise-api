package franchise_api.infrastructure.web;

import franchise_api.application.port.in.command.AddBranchCommand;
import franchise_api.application.port.in.command.AddProductCommand;
import franchise_api.application.port.in.command.CreateFranchiseCommand;
import franchise_api.application.port.in.command.RenameBranchCommand;
import franchise_api.application.port.in.command.RenameFranchiseCommand;
import franchise_api.application.port.in.command.RenameProductCommand;
import franchise_api.application.port.in.command.UpdateProductStockCommand;
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

	@GetMapping
	public Flux<FranchiseResponse> getAllFranchises() {
		return franchiseUseCase.getAllFranchises()
				.map(FranchiseResponseMapper::toResponse);
	}

	@PostMapping
	public Mono<ResponseEntity<FranchiseResponse>> createFranchise(@Valid @RequestBody Mono<CreateFranchiseRequest> request) {
		return request
				.map(body -> new CreateFranchiseCommand(body.name()))
				.flatMap(franchiseUseCase::createFranchise)
				.map(FranchiseResponseMapper::toResponse)
				.map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
	}

	@PostMapping("/{franchiseId}/branches")
	public Mono<ResponseEntity<FranchiseResponse>> addBranch(
			@PathVariable String franchiseId,
			@Valid @RequestBody Mono<AddBranchRequest> request) {
		return request
				.map(body -> new AddBranchCommand(franchiseId, body.name()))
				.flatMap(franchiseUseCase::addBranch)
				.map(FranchiseResponseMapper::toResponse)
				.map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
	}

	@PostMapping("/{franchiseId}/branches/{branchId}/products")
	public Mono<ResponseEntity<FranchiseResponse>> addProduct(
			@PathVariable String franchiseId,
			@PathVariable String branchId,
			@Valid @RequestBody Mono<AddProductRequest> request) {
		return request
				.map(body -> new AddProductCommand(franchiseId, branchId, body.name(), body.stock()))
				.flatMap(franchiseUseCase::addProduct)
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
				.map(body -> new UpdateProductStockCommand(franchiseId, branchId, productId, body.stock()))
				.flatMap(franchiseUseCase::updateProductStock)
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
				.map(body -> new RenameFranchiseCommand(franchiseId, body.name()))
				.flatMap(franchiseUseCase::renameFranchise)
				.map(FranchiseResponseMapper::toResponse);
	}

	@PatchMapping("/{franchiseId}/branches/{branchId}/name")
	public Mono<FranchiseResponse> renameBranch(
			@PathVariable String franchiseId,
			@PathVariable String branchId,
			@Valid @RequestBody Mono<RenameRequest> request) {
		return request
				.map(body -> new RenameBranchCommand(franchiseId, branchId, body.name()))
				.flatMap(franchiseUseCase::renameBranch)
				.map(FranchiseResponseMapper::toResponse);
	}

	@PatchMapping("/{franchiseId}/branches/{branchId}/products/{productId}/name")
	public Mono<FranchiseResponse> renameProduct(
			@PathVariable String franchiseId,
			@PathVariable String branchId,
			@PathVariable String productId,
			@Valid @RequestBody Mono<RenameRequest> request) {
		return request
				.map(body -> new RenameProductCommand(franchiseId, branchId, productId, body.name()))
				.flatMap(franchiseUseCase::renameProduct)
				.map(FranchiseResponseMapper::toResponse);
	}
}