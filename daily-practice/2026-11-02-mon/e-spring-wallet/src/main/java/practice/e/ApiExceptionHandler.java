package practice.e;

import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

// TODO: make this controller advice (extending ResponseEntityExceptionHandler keeps validation errors as 400 ProblemDetail) and map, as ProblemDetail:
//  AccountNotFound -> 404; InsufficientFunds, DuplicateOwner, IdempotencyConflict, DataIntegrityViolationException -> 409; InvalidTransfer -> 422.
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {}
