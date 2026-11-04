package practice.e;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class StatusAndValidationTest {
    static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    @ParameterizedTest
    @CsvSource({
            "OPEN,IN_PROGRESS,true", "OPEN,CLOSED,true", "OPEN,RESOLVED,false", "OPEN,OPEN,false",
            "IN_PROGRESS,OPEN,true", "IN_PROGRESS,RESOLVED,true", "IN_PROGRESS,CLOSED,false", "IN_PROGRESS,IN_PROGRESS,false",
            "RESOLVED,CLOSED,true", "RESOLVED,OPEN,true", "RESOLVED,IN_PROGRESS,false", "RESOLVED,RESOLVED,false",
            "CLOSED,OPEN,false", "CLOSED,IN_PROGRESS,false", "CLOSED,RESOLVED,false", "CLOSED,CLOSED,false"})
    void transitions(Status from, Status to, boolean allowed) {
        assertThat(from.canMoveTo(to)).isEqualTo(allowed);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Fix DB", "HELP!", "", "123456789", "Server IS DOWN", "ab", "NO-OP"})
    @NullSource
    void validTitlesForTheCapsRule(String title) {
        assertThat(new NotAllCapsValidator().isValid(title, null)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"SERVER IS DOWN", "ABCDEF", "ÄÖÜÄÖÜ", "A-B-C-D-E-F", "URGENT 123"})
    void invalidTitlesForTheCapsRule(String title) {
        assertThat(new NotAllCapsValidator().isValid(title, null)).isFalse();
    }

    static Set<String> violations(CreateTicketRequest r) {
        return VALIDATOR.validate(r).stream().map(v -> v.getPropertyPath().toString()).collect(Collectors.toSet());
    }

    @Test void validRequest() {
        assertThat(violations(new CreateTicketRequest("Printer jams", Priority.LOW, null))).isEmpty();
        assertThat(violations(new CreateTicketRequest("Printer jams", Priority.HIGH, "ann@x.com"))).isEmpty();
        assertThat(violations(new CreateTicketRequest("x".repeat(100), Priority.HIGH, null))).isEmpty();
    }

    @Test void invalidRequests() {
        assertThat(violations(new CreateTicketRequest(null, Priority.LOW, null))).containsExactly("title");
        assertThat(violations(new CreateTicketRequest("   ", Priority.LOW, null))).containsExactly("title");
        assertThat(violations(new CreateTicketRequest("x".repeat(101), Priority.LOW, null))).containsExactly("title");
        assertThat(violations(new CreateTicketRequest("SERVER IS DOWN", Priority.LOW, null))).containsExactly("title");
        assertThat(violations(new CreateTicketRequest("ok title", null, null))).containsExactly("priority");
        assertThat(violations(new CreateTicketRequest("ok title", Priority.LOW, "not-an-email"))).containsExactly("assignee");
        assertThat(violations(new CreateTicketRequest("ok title", Priority.LOW, "a".repeat(80) + "@x.com"))).containsExactly("assignee");
        assertThat(violations(new CreateTicketRequest(null, null, "bad"))).containsExactlyInAnyOrder("title", "priority", "assignee");
    }
}
