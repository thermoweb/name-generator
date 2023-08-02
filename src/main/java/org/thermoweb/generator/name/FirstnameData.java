package org.thermoweb.generator.name;

public record FirstnameData(String firstname, Gender gender, Language language, Double frequency) {

    public FirstnameData(String[] line) {
        this(line[0], Gender.fromLetter(line[1]), Language.fromCode(line[2]), Double.parseDouble(line[3]));
        if (line[0].contains("(") || line[0].contains(")")) {
            throw new IllegalArgumentException(String.format("firstname '%s' should not contains those kind of letters...", line[0]));
        }
    }
}
