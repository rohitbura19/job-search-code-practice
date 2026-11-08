package practice.e;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * TODO(E1b): make this an entity.
 *  - id: IDENTITY generated; title: not null, unique, length 100; capacity: not null.
 *  - schedule: @Embedded.
 *  - students: @ManyToMany owned by this side with a join table "enrollment" (columns course_id, student_id), lazy (default).
 *    Use a Set so a student cannot be listed twice.
 */
public class Course {
    private Long id;
    private String title;
    private int capacity;
    private Schedule schedule;
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
