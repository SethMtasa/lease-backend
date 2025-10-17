package prac.lease.model;

public enum LeaseStatus {
    PENDING_APPROVAL("pa"),
    APPROVED("ap"),
    REJECTED("rj"),
    ACTIVE("ac"),
    EXPIRED("ex"),
    AUTO_RENEWED("ar");

    private final String statusAbbreviation;

    LeaseStatus(String statusAbbreviation) {
        this.statusAbbreviation = statusAbbreviation;
    }

    public String getStatusAbbreviation() {
        return statusAbbreviation;
    }
}