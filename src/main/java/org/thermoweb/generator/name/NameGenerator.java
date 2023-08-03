package org.thermoweb.generator.name;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.random.RandomGenerator;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NameGenerator {

    @Getter
    private static final List<FirstnameData> firstnames = loadFirstnamesFile();

    @Getter
    private static final List<String> names = loadNamesFile();
    private static final RandomGenerator generator = RandomGenerator.getDefault();

    private static final int MAX_LENGTH = 30;

    private NameGenerator() {

    }

    public static String getRandomFirstname(Language language) {
        return getRandomFirstname(language, getRandomItem(Arrays.stream(Gender.values()).toList()));
    }

    public static String getRandomFirstname(Language language, Gender gender) {
        Map<String, RandomCollection<String>> transitionsMap = FrequencyLoader.getTransitionsMap(Type.FIRSTNAME, gender, language);

        return generateRandomFirstname(transitionsMap, MAX_LENGTH);
    }

    public static String generateRandomFirstname(Map<String, RandomCollection<String>> transitionsMap, int length) {
        String firstLetter = getRandomItem(transitionsMap.keySet().stream().filter(s -> s.length() == 1).toList());
        StringBuilder firstname = new StringBuilder(firstLetter);
        RandomCollection<String> getNextProbableLetters;
        while ((getNextProbableLetters = getGetNextProbableLetters(transitionsMap, firstname)) != null && firstname.length() <= length) {
            firstname.append(getNextProbableLetters.next());
        }

        return firstname.toString();
    }

    private static RandomCollection<String> getGetNextProbableLetters(Map<String, RandomCollection<String>> transitionsMap, StringBuilder firstname) {
        return transitionsMap.get(firstname.length() < 3 ? firstname.toString() : firstname.substring(firstname.length() - 3));
    }

    private static <T> T getRandomItem(List<T> list) {
        return list.get(generator.nextInt(list.size()));
    }

    private static List<FirstnameData> loadFirstnamesFile() {
        log.atInfo().log("loading firstnames file...");
        return FrequencyLoader.loadFirstnames(Thread.currentThread().getContextClassLoader().getResourceAsStream("firstnames.csv"));
    }

    private static List<String> loadNamesFile() {
        log.atInfo().log("loading names file...");
        return FrequencyLoader.loadNames(Thread.currentThread().getContextClassLoader().getResourceAsStream("names.csv"));
    }
}
