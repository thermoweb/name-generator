package org.thermoweb.generator.name;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum Language {
    AFRICAN,
    ARABIC,
    ARMENIAN,
    BIBLICAL,
    CHINESE,
    CROATIAN,
    CZECH,
    DANISH,
    ENGLISH,
    ENGLISH_BIBLICAL("english, biblical"),
    ENGLISH_JEWISH("english, jewish"),
    ENGLISH_MODERN("english (modern)"),
    FINNISH,
    FRENCH,
    GERMAN,
    GREEK_ANCIENT("ancient greek"),
    GREEK_ANCIENT_LATINIZED("ancient greek (latinized)"),
    GREEK_MYTHOLOGY("greek mythology"),
    GREEK_MYTHOLOGY_LATINIZED("greek mythology (latinized)"),
    HISTORY,
    HUNGARIAN,
    INDIAN,
    IRANIAN,
    IRISH,
    ITALIAN,
    JEWISH,
    LITERATURE,
    NONE,
    POLISH,
    PORTUGUESE,
    RUSSIAN,
    SPANISH,
    SWEDISH,
    TURKISH;

    private final String code;

    Language(String code) {
        this.code = code;
    }

    Language() {
        this.code = this.name().toLowerCase();
    }

    public static Language fromCode(String code) {
        for (Language language : values()) {
            if (language.code.equals(code)) {
                return language;
            }
        }
        log.atDebug().log("the code {} does not exist", code);
        return NONE;
    }
}
