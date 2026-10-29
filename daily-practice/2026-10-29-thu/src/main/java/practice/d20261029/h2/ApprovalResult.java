package practice.d20261029.h2;

public record ApprovalResult(boolean approved, String by, String reason) {
    public static ApprovalResult approved(String by) { return new ApprovalResult(true, by, ""); }
    public static ApprovalResult rejected(String by, String reason) { return new ApprovalResult(false, by, reason); }
}
