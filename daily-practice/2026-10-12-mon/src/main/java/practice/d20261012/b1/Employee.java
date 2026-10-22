package practice.d20261012.b1;

import java.util.List;

/** GIVEN - salary in rupees per year. */
public record Employee(String name, String dept, double salary, List<String> skills) {}
