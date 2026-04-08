package franchise_api.application.port.in.command;

public record UpdateProductStockCommand(String franchiseId, String branchId, String productId, int stock) {
}