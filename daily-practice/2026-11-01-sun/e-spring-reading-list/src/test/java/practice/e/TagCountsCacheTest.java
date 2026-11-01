package practice.e;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig
class TagCountsCacheTest {
    @Configuration @Import(CacheConfig.class)
    static class Cfg {
        @Bean ArticleRepository repo() { return mock(ArticleRepository.class); }
        @Bean ReadingProps props() { return new ReadingProps(5, 50); }
        @Bean ArticleService service(ArticleRepository r, ReadingProps p) { return new ArticleService(r, p); }
    }

    @Autowired ArticleService service;
    @Autowired ArticleRepository repo;
    @Autowired CacheManager cacheManager;

    private static TagCount tc(String tag, long n) {
        return new TagCount() { public String getTag() { return tag; } public long getTotal() { return n; } };
    }

    @BeforeEach void clean() {
        reset(repo);
        cacheManager.getCache("tagCounts").clear();
        when(repo.tagCounts()).thenReturn(List.of(tc("java", 2)));
    }

    @Test void secondCallIsServedFromTheCache() {
        assertEquals(List.of(new TagCountView("java", 2)), service.tagCounts());
        service.tagCounts();
        verify(repo, times(1)).tagCounts();
    }

    @Test void addingAnArticleEvictsTheCache() {
        when(repo.save(any())).thenAnswer(i -> { Article a = i.getArgument(0); org.springframework.test.util.ReflectionTestUtils.setField(a, "id", 1L); return a; });
        service.tagCounts();
        service.add(new ArticleRequest("https://x.example", "T", Set.of("java")));
        service.tagCounts();
        verify(repo, times(2)).tagCounts();
    }
}
