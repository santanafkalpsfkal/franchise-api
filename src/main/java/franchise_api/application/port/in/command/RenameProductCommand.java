package franchise_api.application.port.in.command;

public record RenameProductCommand(String franchiseId, String branchId, String productId, String newName) {
}