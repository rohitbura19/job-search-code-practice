package practice.e;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

class NoteServiceTest {
    NoteRepository repo = mock(NoteRepository.class);
    NoteService service = new NoteService(repo, new NotesProps(2, 10));

    Note note(String owner, String title) {
        Note n = new Note(owner, title, "b");
        org.springframework.test.util.ReflectionTestUtils.setField(n, "id", 1L);     // what the database would assign
        return n;
    }

    @Test void createChecksQuotaThenSavesAndFlushes() {
        when(repo.countByOwner("ann")).thenReturn(1L);
        when(repo.saveAndFlush(any(Note.class))).thenAnswer(i -> {
            Note n = i.getArgument(0);
            org.springframework.test.util.ReflectionTestUtils.setField(n, "id", 1L);
            return n;
        });
        NoteView v = service.create(new NoteRequest("ann", "t", "b"));
        assertEquals("t", v.title());
        verify(repo).saveAndFlush(any(Note.class));
    }

    @Test void createRejectsAtTheQuota() {
        when(repo.countByOwner("ann")).thenReturn(2L);
        assertThrows(QuotaExceededException.class, () -> service.create(new NoteRequest("ann", "t", "b")));
        verify(repo, never()).saveAndFlush(any());
    }

    @Test void getAndDeleteReportMissingNotes() {
        when(repo.findById(7L)).thenReturn(Optional.empty());
        assertThrows(NoteNotFoundException.class, () -> service.get(7));
        assertThrows(NoteNotFoundException.class, () -> service.delete(7));
        assertThrows(NoteNotFoundException.class, () -> service.update(7, new NoteUpdate("t", "b", 0L)));
        verify(repo, never()).delete(any());
    }

    @Test void deleteLoadsThenDeletes() {
        Note n = note("ann", "t");
        when(repo.findById(1L)).thenReturn(Optional.of(n));
        service.delete(1);
        verify(repo).delete(n);
    }

    @Test void updateComparesTheVersionToken() {
        Note n = note("ann", "old");
        when(repo.findById(1L)).thenReturn(Optional.of(n));
        when(repo.saveAndFlush(n)).thenReturn(n);
        assertThrows(StaleVersionException.class, () -> service.update(1, new NoteUpdate("new", "b", 5L)));   // entity version is 0
        verify(repo, never()).saveAndFlush(any());
        assertEquals("new", service.update(1, new NoteUpdate("new", "b2", 0L)).title());
        assertEquals("b2", n.getBody());
    }

    @Test void restoreFailsWhenNothingChanged() {
        when(repo.restore(3L)).thenReturn(0);
        assertThrows(NoteNotFoundException.class, () -> service.restore(3));
        when(repo.restore(4L)).thenReturn(1);
        when(repo.findById(4L)).thenReturn(Optional.of(note("ann", "t")));
        assertEquals("t", service.restore(4).title());
    }

    @Test void listCapsSizeSortsByIdAndPicksTheRightQuery() {
        when(repo.findByOwner(eq("ann"), any(Pageable.class))).thenReturn(new PageImpl<>(List.of(note("ann", "t"))));
        service.list("ann", null, 0, 500);
        service.list("ann", "  ", 0, 500);
        ArgumentCaptor<Pageable> cap = ArgumentCaptor.forClass(Pageable.class);
        verify(repo, org.mockito.Mockito.times(2)).findByOwner(eq("ann"), cap.capture());
        assertEquals(10, cap.getValue().getPageSize());
        assertEquals("id: ASC", cap.getValue().getSort().toString());
        verify(repo, never()).search(any(), any(), any());

        when(repo.search(eq("ann"), eq("milk"), any(Pageable.class))).thenReturn(new PageImpl<>(List.of()));
        service.list("ann", " milk ", 2, 5);
        verify(repo).search(eq("ann"), eq("milk"), any(Pageable.class));
    }

    @Test void writesAreTransactionalAndReadsAreReadOnly() throws Exception {
        for (String m : new String[] {"create", "delete", "restore"}) {
            Transactional t = java.util.Arrays.stream(NoteService.class.getDeclaredMethods()).filter(x -> x.getName().equals(m)).findFirst().orElseThrow().getAnnotation(Transactional.class);
            assertEquals(false, t == null || t.readOnly(), m + " must be a read-write transaction");
        }
        Transactional get = NoteService.class.getDeclaredMethod("get", long.class).getAnnotation(Transactional.class);
        assertEquals(true, get != null && get.readOnly());
    }
}
