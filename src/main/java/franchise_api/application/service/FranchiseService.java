package franchise_api.application.service;

import franchise_api.application.port.in.FranchiseUseCase;
import franchise_api.application.port.out.FranchiseRepository;
import franchise_api.domain.exception.NotFoundException;
import franchise_api.domain.model.Franchise;
import franchise_api.domain.model.TopStockProduct;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class FranchiseService implements FranchiseUseCase {

	private final FranchiseRepository franchiseRepository;

	public FranchiseService(FranchiseRepository franchiseRepository) {
		this.franchiseRepository = franchiseRepository;
	}

	@Override
	public Mono<Franchise> createFranchise(String name) {
		return franchiseRepository.save(new Franchise(null, name, List.of()));
	}

	@Override
	public Mono<Franchise> addBranch(String franchiseId, String branchName) {
		return getFranchise(franchiseId)
				.map(franchise -> franchise.addBranch(branchName))
				.flatMap(franchiseRepository::save);
	}

	@Override
	public Mono<Franchise> addProduct(String franchiseId, String branchId, String productName, int stock) {
		return getFranchise(franchiseId)
				.map(franchise -> franchise.addProduct(branchId, productName, stock))
				.flatMap(franchiseRepository::save);
	}

	@Override
	public Mono<Void> deleteProduct(String franchiseId, String branchId, String productId) {
		return getFranchise(franchiseId)
				.map(franchise -> franchise.removeProduct(branchId, productId))
				.flatMap(franchiseRepository::save)
				.then();
	}

	@Override
	public Mono<Franchise> updateProductStock(String franchiseId, String branchId, String productId, int stock) {
		return getFranchise(franchiseId)
				.map(franchise -> franchise.updateProductStock(branchId, productId, stock))
				.flatMap(franchiseRepository::save);
	}

	@Override
	public Flux<TopStockProduct> getTopStockProductsByBranch(String franchiseId) {
		return getFranchise(franchiseId)
				.flatMapMany(franchise -> Flux.fromIterable(franchise.topStockProductsByBranch()));
	}

	@Override
	public Mono<Franchise> renameFranchise(String franchiseId, String newName) {
		return getFranchise(franchiseId)
				.map(franchise -> franchise.rename(newName))
				.flatMap(franchiseRepository::save);
	}

	@Override
	public Mono<Franchise> renameBranch(String franchiseId, String branchId, String newName) {
		return getFranchise(franchiseId)
				.map(franchise -> franchise.renameBranch(branchId, newName))
				.flatMap(franchiseRepository::save);
	}

	@Override
	public Mono<Franchise> renameProduct(String franchiseId, String branchId, String productId, String newName) {
		return getFranchise(franchiseId)
				.map(franchise -> franchise.renameProduct(branchId, productId, newName))
				.flatMap(franchiseRepository::save);
	}

	private Mono<Franchise> getFranchise(String franchiseId) {
		return franchiseRepository.findById(franchiseId)
				.switchIfEmpty(Mono.error(new NotFoundException("Franchise not found: " + franchiseId)));
	}
}