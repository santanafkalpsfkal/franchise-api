package franchise_api.application.port.in.command;

public record AddBranchCommand(String franchiseId, String branchName) {
}