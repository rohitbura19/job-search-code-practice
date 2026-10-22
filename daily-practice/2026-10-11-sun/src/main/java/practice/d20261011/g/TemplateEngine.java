package practice.d20261011.g;

import java.util.Map;

/** GIVEN - do not edit. */
public interface TemplateEngine {
    RenderedMessage render(String type, Channel c, Map<String, String> params);
}
