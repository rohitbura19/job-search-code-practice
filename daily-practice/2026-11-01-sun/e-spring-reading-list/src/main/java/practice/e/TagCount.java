package practice.e;

/** Interface projection for the tag statistics query (aliases must be tag and total). */
public interface TagCount {
    String getTag();
    long getTotal();
}
