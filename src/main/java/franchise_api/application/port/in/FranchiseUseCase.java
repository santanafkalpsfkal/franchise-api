package franchise_api.application.port.in;

import franchise_api.domain.model.Franchise;
import franchise_api.domain.model.TopStockProduct;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FranchiseUseCase {

	Mono<Franchise> createFranchise(String name);

	Mono<Franchise> addBranch(String franchiseId, String branchName);

	Mono<Franchise> addProduct(String franchiseId, String branchId, String productName, int stock);

	Mono<Void> deleteProduct(String franchiseId, String branchId, String productId);

	Mono<Franchise> updateProductStock(String franchiseId, String branchId, String productId, int stock);

	Flux<TopStockProduct> getTopStockProductsByBranch(String franchiseId);

	Mono<Franchise> renameFranchise(String franchiseId, String newName);

	Mono<Franchise> renameBranch(String franchiseId, String branchId, String newName);

	Mono<Franchise> renameProduct(String franchiseId, String branchId, String productId, String newName);
}