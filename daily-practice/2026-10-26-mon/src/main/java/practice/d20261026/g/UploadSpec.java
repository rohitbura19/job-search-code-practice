package practice.d20261026.g;

/** What the client announces up front. Chunks are chunkSize bytes except the last, which carries the remainder. */
public record UploadSpec(String fileName, long totalBytes, int chunkSize) {
    public static final int MAX_CHUNK = 5 * 1024 * 1024;

    public int chunkCount() { return (int) ((totalBytes + chunkSize - 1) / chunkSize); }

    /** Expected length of chunk `index` (0-based); only valid for 0 <= index < chunkCount(). */
    public int lengthOf(int index) {
        return index < chunkCount() - 1 ? chunkSize : (int) (totalBytes - (long) (chunkCount() - 1) * chunkSize);
    }
}
