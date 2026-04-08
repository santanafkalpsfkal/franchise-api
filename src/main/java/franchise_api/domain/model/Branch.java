package franchise_api.domain.model;

import franchise_api.domain.exception.DomainException;
import franchise_api.domain.exception.NotFoundException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public record Branch(String id, String name, List<Product> products) {

	public Branch {
		if (name == null || name.isBlank()) {
			throw new DomainException("Branch name is required");
		}
		products = products == null ? List.of() : List.copyOf(products);
	}

	public Branch addProduct(String productName, int stock) {
		List<Product> updatedProducts = new ArrayList<>(products);
		updatedProducts.add(new Product(UUID.randomUUID().toString(), productName, stock));
		return new Branch(id, name, updatedProducts);
	}

	public Branch removeProduct(String productId) {
		boolean removed = products.stream().anyMatch(product -> product.id().equals(productId));
		if (!removed) {
			throw new NotFoundException("Product not found: " + productId);
		}

		List<Product> updatedProducts = products.stream()
				.filter(product -> !product.id().equals(productId))
				.toList();
		return new Branch(id, name, updatedProducts);
	}

	public Branch updateProductStock(String productId, int stock) {
		return mapProduct(productId, product -> product.updateStock(stock));
	}

	public Branch renameProduct(String productId, String newName) {
		return mapProduct(productId, product -> product.rename(newName));
	}

	public Branch rename(String newName) {
		return new Branch(id, newName, products);
	}

	public TopStockProduct topStockProduct() {
		Product product = products.stream()
				.max(Comparator.comparingInt(Product::stock))
				.orElseThrow(() -> new NotFoundException("Branch has no products: " + id));
		return new TopStockProduct(id, name, product.id(), product.name(), product.stock());
	}

	private Branch mapProduct(String productId, java.util.function.Function<Product, Product> mapper) {
		boolean found = products.stream().anyMatch(product -> product.id().equals(productId));
		if (!found) {
			throw new NotFoundException("Product not found: " + productId);
		}

		List<Product> updatedProducts = products.stream()
				.map(product -> product.id().equals(productId) ? mapper.apply(product) : product)
				.toList();
		return new Branch(id, name, updatedProducts);
	}
}