package franchise_api.infrastructure.persistence.mongo.mapper;

import franchise_api.domain.model.Branch;
import franchise_api.domain.model.Franchise;
import franchise_api.domain.model.Product;
import franchise_api.infrastructure.persistence.mongo.document.BranchDocument;
import franchise_api.infrastructure.persistence.mongo.document.FranchiseDocument;
import franchise_api.infrastructure.persistence.mongo.document.ProductDocument;

import java.util.List;

public final class FranchiseDocumentMapper {

	private FranchiseDocumentMapper() {
	}

	public static Franchise toDomain(FranchiseDocument document) {
		return new Franchise(
				document.getId(),
				document.getName(),
				document.getBranches() == null
						? List.of()
						: document.getBranches().stream().map(branchDocument -> toBranchDomain(branchDocument)).toList()
		);
	}

	public static FranchiseDocument toDocument(Franchise franchise) {
		return new FranchiseDocument(
				franchise.id(),
				franchise.name(),
				franchise.branches().stream().map(branch -> toBranchDocument(branch)).toList()
		);
	}

	private static Branch toBranchDomain(BranchDocument document) {
		return new Branch(
				document.getId(),
				document.getName(),
				document.getProducts() == null
						? List.of()
						: document.getProducts().stream().map(productDocument -> toProductDomain(productDocument)).toList()
		);
	}

	private static BranchDocument toBranchDocument(Branch branch) {
		return new BranchDocument(
				branch.id(),
				branch.name(),
				branch.products().stream().map(product -> toProductDocument(product)).toList()
		);
	}

	private static Product toProductDomain(ProductDocument document) {
		return new Product(document.getId(), document.getName(), document.getStock());
	}

	private static ProductDocument toProductDocument(Product product) {
		return new ProductDocument(product.id(), product.name(), product.stock());
	}
}