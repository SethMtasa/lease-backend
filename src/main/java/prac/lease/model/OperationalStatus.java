package prac.lease.model;

public enum OperationalStatus {
    OPERATIONAL("op"),
    UNDER_DEVELOPMENT("ud");

    private final String statusAbbreviation;

    OperationalStatus(String statusAbbreviation) {
        this.statusAbbreviation = statusAbbreviation;
    }

    public String getStatusAbbreviation() {
        return statusAbbreviation;
    }
}