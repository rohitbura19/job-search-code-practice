package practice.d20261029.h2;

public record Expense(String id, String employee, long amountPaise, String category) {
    public Expense {
        if (amountPaise <= 0) throw new IllegalArgumentException("amount must be positive");
    }
}
