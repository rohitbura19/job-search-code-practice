package practice.e;

import java.util.List;

/** Given. A "slice" response: no total count, only whether another page exists (cheaper than a count query). */
public record TicketPage(List<TicketView> content, boolean hasNext) {}
