package practice.e;

/**
 * TODO(E2): make this an entity: IDENTITY id; name not null; email not null, unique, length 200.
 * No reference back to courses (the relationship is unidirectional from Course).
 */
public class Student {
    private Long id;
    private String name;
    private String email;

    protected Student() {}
    public Student(String name, String email) { this.name = name; this.email = email; }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
}
