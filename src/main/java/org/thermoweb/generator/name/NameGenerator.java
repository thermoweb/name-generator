package org.thermoweb.generator.name;

import java.util.List;
import java.util.Map;
import java.util.random.RandomGenerator;

public class NameGenerator {
    private static final RandomGenerator generator = RandomGenerator.getDefault();

    private NameGenerator() {

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
}
