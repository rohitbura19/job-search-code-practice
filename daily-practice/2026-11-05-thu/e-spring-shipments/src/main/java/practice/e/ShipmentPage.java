package practice.e;

import java.util.List;

/** Given. Own page type: Spring Data's PageImpl JSON shape is not a stable contract. */
public record ShipmentPage(List<ShipmentView> content, long total, int page, int size) {}
