package practice.d20261026.g;

public class UploadException extends RuntimeException {
    public enum Code { INVALID_SPEC, NOT_FOUND, EXPIRED, BAD_INDEX, BAD_LENGTH, CONFLICT, INCOMPLETE, ALREADY_COMPLETED }

    private final Code code;

    public UploadException(Code code, String message) {
        super(message);
        this.code = code;
    }

    public Code code() { return code; }
}
