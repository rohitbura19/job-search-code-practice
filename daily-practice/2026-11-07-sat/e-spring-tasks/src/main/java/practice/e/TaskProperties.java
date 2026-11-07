package practice.e;

import jakarta.validation.constraints.Min;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/** app.tasks.max-comments-per-task (relaxed binding: also APP_TASKS_MAXCOMMENTSPERTASK). */
@Validated
@ConfigurationProperties(prefix = "app.tasks")
public record TaskProperties(@Min(1) int maxCommentsPerTask) {}
