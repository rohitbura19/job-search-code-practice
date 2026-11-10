package practice.e;

public enum Plan {
    FREE(0), BASIC(500), PRO(1500);

    private final int priceCents;
    Plan(int priceCents) { this.priceCents = priceCents; }
    public int priceCents() { return priceCents; }
}
