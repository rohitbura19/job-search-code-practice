package practice.e;

import jakarta.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
public class Course {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String title;

    @Column(nullable = false)
    private int capacity;

    @Embedded
    private Schedule schedule;

    /** Owning side of the many-to-many: the join table "enrollment" has one row per (course, student). */
    @ManyToMany
    @JoinTable(name = "enrollment",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id"))
    private Set<Student> students = new LinkedHashSet<>();

    protected Course() {}
    public Course(String title, int capacity, Schedule schedule) { this.title = title; this.capacity = capacity; this.schedule = schedule; }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public int getCapacity() { return capacity; }
    public Schedule getSchedule() { return schedule; }
    public Set<Student> getStudents() { return students; }
    public void setTitle(String title) { this.title = title; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public boolean isFull() { return students.size() >= capacity; }
    public boolean enroll(Student s) { return students.add(s); }
    public boolean unenroll(Student s) { return students.remove(s); }
}
