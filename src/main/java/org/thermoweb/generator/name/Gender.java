package org.thermoweb.generator.name;

import java.util.Arrays;
import java.util.List;

public enum Gender {
    NONE("m,f", "f,m"),
    MALE("m"),
    FEMALE("f");

    private final List<String> code;

    Gender(String ... code) {
        this.code = Arrays.stream(code).toList();
    }

    public static Gender fromLetter(String letter) {
        for (Gender gender : values()) {
            if (gender.code.contains(letter)) {
                return gender;
            }
        }

        throw new IllegalArgumentException(String.format("the code %s does not exists", letter));
    }
}
