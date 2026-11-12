package s14_campus;

import java.util.List;

/** Given. */
public record Student(String id, String name, String city, List<Course> courses) {}
