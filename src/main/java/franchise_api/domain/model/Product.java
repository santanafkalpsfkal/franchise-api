package franchise_api.domain.model;

import franchise_api.domain.exception.DomainException;

public record Product(String id, String name, int stock) {

	public Product {
		if (name == null || name.isBlank()) {
			throw new DomainException("Product name is required");
		}
		if (stock < 0) {
			throw new DomainException("Product stock cannot be negative");
		}
	}

	public Product rename(String newName) {
		return new Product(id, newName, stock);
	}

	public Product updateStock(int newStock) {
		return new Product(id, name, newStock);
	}
}