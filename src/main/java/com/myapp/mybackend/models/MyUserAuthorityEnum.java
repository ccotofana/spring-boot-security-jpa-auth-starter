package com.myapp.mybackend.models;

import jakarta.persistence.AttributeConverter;

import java.util.HashSet;
import java.util.Set;

public enum MyUserAuthorityEnum {
    // Sample roles (authorities that have "ROLE_" prefix)
    ROLE_ADMIN(0),
    ROLE_USER(1),
    // Sample authority
    CAN_VIEW_COMMENTS(2);

    private static final String ROLE_PREFIX = "ROLE_";
    private int value;

    static {
        validateEnumValueUniqueness();
    }

    /**
     * Values are used for efficient database persistence. Duplicates are not allowed.
     */
    private static void validateEnumValueUniqueness() {
        Set<Integer> valueSet = new HashSet<>();
        for (MyUserAuthorityEnum e : values()) {
            if (valueSet.contains(e.getValue())) {
                throw new IllegalStateException("Enum values must be unique");
            }
            valueSet.add(e.getValue());
        }
    }

    MyUserAuthorityEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    /**
     * Role prefix is not used when specifying role names in Spring Security rules
     * @return Authority name, stripped of the "ROLE_" prefix (if present)
     */
    public String getRoleName() {
        return this.name().replaceFirst(ROLE_PREFIX, "");
    }

    /**
     * Converter class to use custom enum value instead of name or ordinal when persisting.
     */
    @jakarta.persistence.Converter
    public static class Converter implements AttributeConverter<MyUserAuthorityEnum, Integer> {
        @Override
        public Integer convertToDatabaseColumn(MyUserAuthorityEnum authEnum) {
            return authEnum.getValue();
        }

        @Override
        public MyUserAuthorityEnum convertToEntityAttribute(Integer value) {
            for (MyUserAuthorityEnum authEnum : MyUserAuthorityEnum.values()) {
                if (authEnum.getValue() == value) {
                    return authEnum;
                }
            }
            throw new IllegalArgumentException("Invalid MyUserAuthorityEnum value: " + value);
        }
    }
}
