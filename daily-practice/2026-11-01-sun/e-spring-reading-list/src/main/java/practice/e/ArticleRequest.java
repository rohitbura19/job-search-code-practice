package practice.e;

import java.util.Set;

/**
 * TODO: add Bean Validation annotations.
 *   url: required, 1..300 chars, must start with http:// or https:// (regex);
 *   title: not blank, max 120;
 *   tags: optional (null allowed), at most 20 elements here (the configurable per-article limit is checked in the service),
 *         every element not blank and max 20 chars.
 */
public record ArticleRequest(String url, String title, Set<String> tags) {}
