package pl.sokolak.MyBooks.security;

import lombok.Getter;

import java.util.Set;

public enum OperationType {
    READ(Role.ADMIN, Role.DEV, Role.GUEST),
    ADD(Role.ADMIN, Role.DEV),
    DELETE(Role.ADMIN, Role.DEV),
    EDIT(Role.ADMIN, Role.DEV);

    @Getter
    private final Set<Role> roles;
    OperationType(Role... roles) {
        this.roles = Set.of(roles);
    }
}
