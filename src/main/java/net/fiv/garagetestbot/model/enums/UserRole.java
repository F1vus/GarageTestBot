package net.fiv.garagetestbot.model.enums;

import lombok.Getter;

@Getter
public enum UserRole {
    MECHANIC("механік"),
    ELECTRICIAN("електрик"),
    MANAGER("менеджер");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public static UserRole fromString(String value) throws IllegalArgumentException {
        for (UserRole role : UserRole.values()) {
            if (role.name().equalsIgnoreCase(value) || role.displayName.equalsIgnoreCase(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown role: " + value);
    }
}
