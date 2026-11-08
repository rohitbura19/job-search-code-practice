package practice.e;

import jakarta.persistence.*;

@Entity
public class Student {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true, length = 200)
    private String email;

    protected Student() {}
    public Student(String name, String email) { this.name = name; this.email = email; }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
}
