package practice.e;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.util.ReflectionTestUtils;

class ProfileServiceTest {
    ProfileRepository repo = mock(ProfileRepository.class);
    ProfileService service = new ProfileService(repo);

    @BeforeEach void echoSave() {
        when(repo.saveAndFlush(any(Profile.class))).thenAnswer(i -> i.getArgument(0));
    }

    private Profile stored(long version) {
        Profile p = new Profile("ann@x.com", "Ann", new Address("Pune", "IN"));
        ReflectionTestUtils.setField(p, "id", 7L);
        ReflectionTestUtils.setField(p, "version", version);
        when(repo.findById(7L)).thenReturn(Optional.of(p));
        return p;
    }

    @Test void createNormalisesEmailAndCountry() {
        ProfileView v = service.create(new CreateProfileRequest("  Ann@X.com ", "Ann", "Pune", "in"));
        assertEquals("ann@x.com", v.email());
        assertEquals("IN", v.country());
        verify(repo).existsByEmailIgnoreCase("ann@x.com");
    }

    @Test void createRejectsDuplicate() {
        when(repo.existsByEmailIgnoreCase("ann@x.com")).thenReturn(true);
        assertThrows(Exceptions.DuplicateEmailException.class, () -> service.create(new CreateProfileRequest("ann@x.com", "Ann", "Pune", "IN")));
        verify(repo, never()).saveAndFlush(any());
    }

    @Test void createMapsRaceToDuplicate() {
        when(repo.saveAndFlush(any(Profile.class))).thenThrow(new DataIntegrityViolationException("unique"));
        assertThrows(Exceptions.DuplicateEmailException.class, () -> service.create(new CreateProfileRequest("ann@x.com", "Ann", "Pune", "IN")));
    }

    @Test void getMissing() {
        when(repo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(Exceptions.ProfileNotFoundException.class, () -> service.get(1));
    }

    @Test void patchChangesOnlyGivenFields() {
        stored(3);
        ProfileView v = service.patch(7, 3, new PatchProfileRequest(null, "Delhi"));
        assertEquals("Ann", v.displayName());
        assertEquals("Delhi", v.city());
        assertEquals("IN", v.country());
    }

    @Test void patchWithWrongVersionIsRejectedAndNothingSaved() {
        stored(3);
        assertThrows(Exceptions.StaleVersionException.class, () -> service.patch(7, 2, new PatchProfileRequest("New", null)));
        verify(repo, never()).saveAndFlush(any());
    }

    @Test void patchMissing() {
        when(repo.findById(9L)).thenReturn(Optional.empty());
        assertThrows(Exceptions.ProfileNotFoundException.class, () -> service.patch(9, 0, new PatchProfileRequest("x", null)));
    }
}
