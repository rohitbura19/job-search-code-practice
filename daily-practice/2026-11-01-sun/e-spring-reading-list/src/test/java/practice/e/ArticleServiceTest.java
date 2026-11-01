package practice.e;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

class ArticleServiceTest {
    ArticleRepository repo = mock(ArticleRepository.class);
    ArticleService service;

    @BeforeEach void setUp() {
        service = new ArticleService(repo, new ReadingProps(3, 10));
        when(repo.save(any())).thenAnswer(i -> { Article a = i.getArgument(0); ReflectionTestUtils.setField(a, "id", 5L); return a; });
    }

    @Test void addNormalisesTagsAndSaves() {
        ArticleView v = service.add(new ArticleRequest("https://x.example", "T", Set.of(" Java", "JAVA", "Spring ")));
        ArgumentCaptor<Article> cap = ArgumentCaptor.forClass(Article.class);
        verify(repo).save(cap.capture());
        assertEquals(Set.of("java", "spring"), cap.getValue().getTags());
        assertEquals(5, v.id());
        assertEquals(Set.of("java", "spring"), v.tags());
    }

    @Test void addAcceptsNullTags() {
        assertTrue(service.add(new ArticleRequest("https://x.example", "T", null)).tags().isEmpty());
    }

    @Test void tagLimitIsCheckedAfterNormalisation() {
        assertDoesNotThrow(() -> service.add(new ArticleRequest("https://x.example", "T", Set.of("a", "A", "b", "c"))));   // 3 distinct
        assertThrows(TooManyTagsException.class, () -> service.add(new ArticleRequest("https://y.example", "T", Set.of("a", "b", "c", "d"))));
    }

    @Test void duplicateUrlIsRejectedWithoutSaving() {
        when(repo.existsByUrl("https://x.example")).thenReturn(true);
        assertThrows(DuplicateUrlException.class, () -> service.add(new ArticleRequest("https://x.example", "T", Set.of())));
        verify(repo, never()).save(any());
    }

    @Test void getMissingThrows() {
        when(repo.findById(9L)).thenReturn(Optional.empty());
        assertThrows(ArticleNotFoundException.class, () -> service.get(9));
    }

    @Test void byTagNormalisesTagAndCapsPageSize() {
        when(repo.findByTag(any(), any())).thenReturn(Page.empty());
        service.byTag("  JaVa ", 2, 1000);
        ArgumentCaptor<Pageable> cap = ArgumentCaptor.forClass(Pageable.class);
        verify(repo).findByTag(org.mockito.ArgumentMatchers.eq("java"), cap.capture());
        assertEquals(10, cap.getValue().getPageSize());
        assertEquals(2, cap.getValue().getPageNumber());
        assertTrue(cap.getValue().getSort().getOrderFor("id").isAscending());
    }

    @Test void byTagMapsPage() {
        Article a = new Article("https://x.example", "T", Set.of("java"));
        ReflectionTestUtils.setField(a, "id", 1L);
        when(repo.findByTag(any(), any())).thenReturn(new PageImpl<>(java.util.List.of(a), org.springframework.data.domain.PageRequest.of(0, 10), 1));
        var r = service.byTag("java", 0, 10);
        assertEquals(1, r.total());
        assertEquals("T", r.items().get(0).title());
    }
}
