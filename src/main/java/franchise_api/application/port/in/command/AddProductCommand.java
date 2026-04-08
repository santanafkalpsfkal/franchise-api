package franchise_api.application.port.in.command;

public record AddProductCommand(String franchiseId, String branchId, String productName, int stock) {
}