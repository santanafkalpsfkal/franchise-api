package franchise_api.application.port.in.command;

public record RenameFranchiseCommand(String franchiseId, String newName) {
}