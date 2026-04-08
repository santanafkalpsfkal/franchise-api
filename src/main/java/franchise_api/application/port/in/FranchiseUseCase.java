package franchise_api.application.port.in;

import franchise_api.domain.model.Franchise;
import franchise_api.domain.model.TopStockProduct;
import franchise_api.application.port.in.command.AddBranchCommand;
import franchise_api.application.port.in.command.AddProductCommand;
import franchise_api.application.port.in.command.CreateFranchiseCommand;
import franchise_api.application.port.in.command.RenameBranchCommand;
import franchise_api.application.port.in.command.RenameFranchiseCommand;
import franchise_api.application.port.in.command.RenameProductCommand;
import franchise_api.application.port.in.command.UpdateProductStockCommand;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FranchiseUseCase {

	Flux<Franchise> getAllFranchises();

	Mono<Franchise> createFranchise(CreateFranchiseCommand command);

	Mono<Franchise> addBranch(AddBranchCommand command);

	Mono<Franchise> addProduct(AddProductCommand command);

	Mono<Void> deleteProduct(String franchiseId, String branchId, String productId);

	Mono<Franchise> updateProductStock(UpdateProductStockCommand command);

	Flux<TopStockProduct> getTopStockProductsByBranch(String franchiseId);

	Mono<Franchise> renameFranchise(RenameFranchiseCommand command);

	Mono<Franchise> renameBranch(RenameBranchCommand command);

	Mono<Franchise> renameProduct(RenameProductCommand command);
}