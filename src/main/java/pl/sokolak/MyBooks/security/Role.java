package pl.sokolak.MyBooks.security;

public enum Role {
    GUEST,
    ADMIN,
    DEV,
    NONE;

    public static Role fromString(String string) {
        Role role = switch (string) {
            case "ROLE_GUEST" -> GUEST;
            case "ROLE_ADMIN" -> ADMIN;
            case "ROLE_DEV" -> DEV;
            default -> NONE;
        };
        return role;
    }
}
