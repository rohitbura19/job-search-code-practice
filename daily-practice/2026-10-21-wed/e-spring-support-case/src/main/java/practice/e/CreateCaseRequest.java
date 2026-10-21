package practice.e;

/**
 * TODO: add validation. chargerSerial -> @ChargerSerial; severity required;
 * summary required, not blank, max 200; contactEmail required, not blank, a valid email, max 120.
 */
public record CreateCaseRequest(String chargerSerial, Severity severity, String summary, String contactEmail) {}
