package practice.e;

/** Interface projection: aliases in the JPQL select must match these getter names. */
public interface EnrollmentCount {
    Long getCourseId();
    String getTitle();
    int getCapacity();
    int getEnrolled();
}
