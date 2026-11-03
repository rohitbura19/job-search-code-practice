package practice.e;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(BookingExceptions.NotFound.class)
    ProblemDetail notFound(BookingExceptions.NotFound e) { return problem(HttpStatus.NOT_FOUND, "Booking not found", e); }

    @ExceptionHandler(BookingExceptions.Overlap.class)
    ProblemDetail overlap(BookingExceptions.Overlap e) { return problem(HttpStatus.CONFLICT, "Room already booked", e); }

    @ExceptionHandler({BookingExceptions.TooLong.class, BookingExceptions.InPast.class})
    ProblemDetail unprocessable(RuntimeException e) { return problem(HttpStatus.UNPROCESSABLE_ENTITY, "Booking rejected", e); }

    private static ProblemDetail problem(HttpStatus status, String title, Exception e) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, e.getMessage());
        pd.setTitle(title);
        return pd;
    }
}
