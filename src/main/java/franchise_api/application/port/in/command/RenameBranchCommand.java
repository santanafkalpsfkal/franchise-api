package franchise_api.application.port.in.command;

public record RenameBranchCommand(String franchiseId, String branchId, String newName) {
}