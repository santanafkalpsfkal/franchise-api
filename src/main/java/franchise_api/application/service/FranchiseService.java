package franchise_api.application.service;

import franchise_api.application.port.in.command.AddBranchCommand;
import franchise_api.application.port.in.command.AddProductCommand;
import franchise_api.application.port.in.command.CreateFranchiseCommand;
import franchise_api.application.port.in.command.RenameBranchCommand;
import franchise_api.application.port.in.command.RenameFranchiseCommand;
import franchise_api.application.port.in.command.RenameProductCommand;
import franchise_api.application.port.in.command.UpdateProductStockCommand;
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
	public Flux<Franchise> getAllFranchises() {
		return franchiseRepository.findAll();
	}

	@Override
	public Mono<Franchise> createFranchise(CreateFranchiseCommand command) {
		return franchiseRepository.save(new Franchise(null, command.name(), List.of()));
	}

	@Override
	public Mono<Franchise> addBranch(AddBranchCommand command) {
		return getFranchise(command.franchiseId())
				.map(franchise -> franchise.addBranch(command.branchName()))
				.flatMap(franchiseRepository::save);
	}

	@Override
	public Mono<Franchise> addProduct(AddProductCommand command) {
		return getFranchise(command.franchiseId())
				.map(franchise -> franchise.addProduct(command.branchId(), command.productName(), command.stock()))
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
	public Mono<Franchise> updateProductStock(UpdateProductStockCommand command) {
		return getFranchise(command.franchiseId())
				.map(franchise -> franchise.updateProductStock(command.branchId(), command.productId(), command.stock()))
				.flatMap(franchiseRepository::save);
	}

	@Override
	public Flux<TopStockProduct> getTopStockProductsByBranch(String franchiseId) {
		return getFranchise(franchiseId)
				.flatMapMany(franchise -> Flux.fromIterable(franchise.topStockProductsByBranch()));
	}

	@Override
	public Mono<Franchise> renameFranchise(RenameFranchiseCommand command) {
		return getFranchise(command.franchiseId())
				.map(franchise -> franchise.rename(command.newName()))
				.flatMap(franchiseRepository::save);
	}

	@Override
	public Mono<Franchise> renameBranch(RenameBranchCommand command) {
		return getFranchise(command.franchiseId())
				.map(franchise -> franchise.renameBranch(command.branchId(), command.newName()))
				.flatMap(franchiseRepository::save);
	}

	@Override
	public Mono<Franchise> renameProduct(RenameProductCommand command) {
		return getFranchise(command.franchiseId())
				.map(franchise -> franchise.renameProduct(command.branchId(), command.productId(), command.newName()))
				.flatMap(franchiseRepository::save);
	}

	private Mono<Franchise> getFranchise(String franchiseId) {
		return franchiseRepository.findById(franchiseId)
				.switchIfEmpty(Mono.error(new NotFoundException("Franchise not found: " + franchiseId)));
	}
}