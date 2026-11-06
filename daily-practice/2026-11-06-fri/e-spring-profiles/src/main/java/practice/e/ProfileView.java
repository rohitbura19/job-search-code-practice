package practice.e;

public record ProfileView(Long id, String email, String displayName, String city, String country, long version) {
    public static ProfileView from(Profile p) {
        return new ProfileView(p.getId(), p.getEmail(), p.getDisplayName(), p.getAddress().city(), p.getAddress().country(), p.getVersion());
    }
}
