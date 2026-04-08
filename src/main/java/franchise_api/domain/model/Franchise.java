package franchise_api.domain.model;

import franchise_api.domain.exception.DomainException;
import franchise_api.domain.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public record Franchise(String id, String name, List<Branch> branches) {

	public Franchise {
		if (name == null || name.isBlank()) {
			throw new DomainException("Franchise name is required");
		}
		branches = branches == null ? List.of() : List.copyOf(branches);
	}

	public Franchise addBranch(String branchName) {
		List<Branch> updatedBranches = new ArrayList<>(branches);
		updatedBranches.add(new Branch(UUID.randomUUID().toString(), branchName, List.of()));
		return new Franchise(id, name, updatedBranches);
	}

	public Franchise addProduct(String branchId, String productName, int stock) {
		return mapBranch(branchId, branch -> branch.addProduct(productName, stock));
	}

	public Franchise removeProduct(String branchId, String productId) {
		return mapBranch(branchId, branch -> branch.removeProduct(productId));
	}

	public Franchise updateProductStock(String branchId, String productId, int stock) {
		return mapBranch(branchId, branch -> branch.updateProductStock(productId, stock));
	}

	public Franchise rename(String newName) {
		return new Franchise(id, newName, branches);
	}

	public Franchise renameBranch(String branchId, String newName) {
		return mapBranch(branchId, branch -> branch.rename(newName));
	}

	public Franchise renameProduct(String branchId, String productId, String newName) {
		return mapBranch(branchId, branch -> branch.renameProduct(productId, newName));
	}

	public List<TopStockProduct> topStockProductsByBranch() {
		return branches.stream()
				.filter(branch -> !branch.products().isEmpty())
				.map(Branch::topStockProduct)
				.toList();
	}

	private Franchise mapBranch(String branchId, Function<Branch, Branch> mapper) {
		boolean found = branches.stream().anyMatch(branch -> branch.id().equals(branchId));
		if (!found) {
			throw new NotFoundException("Branch not found: " + branchId);
		}

		List<Branch> updatedBranches = branches.stream()
				.map(branch -> branch.id().equals(branchId) ? mapper.apply(branch) : branch)
				.toList();
		return new Franchise(id, name, updatedBranches);
	}
}