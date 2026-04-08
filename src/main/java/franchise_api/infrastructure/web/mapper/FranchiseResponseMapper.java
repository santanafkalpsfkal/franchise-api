package franchise_api.infrastructure.web.mapper;

import franchise_api.domain.model.Branch;
import franchise_api.domain.model.Franchise;
import franchise_api.domain.model.Product;
import franchise_api.domain.model.TopStockProduct;
import franchise_api.infrastructure.web.dto.response.BranchResponse;
import franchise_api.infrastructure.web.dto.response.FranchiseResponse;
import franchise_api.infrastructure.web.dto.response.ProductResponse;
import franchise_api.infrastructure.web.dto.response.TopStockProductResponse;

public final class FranchiseResponseMapper {

	private FranchiseResponseMapper() {
	}

	public static FranchiseResponse toResponse(Franchise franchise) {
		return new FranchiseResponse(
				franchise.id(),
				franchise.name(),
				franchise.branches().stream().map(branch -> toBranchResponse(branch)).toList()
		);
	}

	public static TopStockProductResponse toResponse(TopStockProduct product) {
		return new TopStockProductResponse(
				product.branchId(),
				product.branchName(),
				product.productId(),
				product.productName(),
				product.stock()
		);
	}

	private static BranchResponse toBranchResponse(Branch branch) {
		return new BranchResponse(
				branch.id(),
				branch.name(),
				branch.products().stream().map(product -> toProductResponse(product)).toList()
		);
	}

	private static ProductResponse toProductResponse(Product product) {
		return new ProductResponse(product.id(), product.name(), product.stock());
	}
}