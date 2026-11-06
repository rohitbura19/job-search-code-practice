package practice.e;

import jakarta.persistence.Embeddable;

@Embeddable
public record Address(String city, String country) {}
