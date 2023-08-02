package org.thermoweb.generator.name;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.random.RandomGenerator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NameGenerator {
    private static final RandomGenerator generator = RandomGenerator.getDefault();

    public static void main(String[] args) {
        String firstnameFile = Optional.ofNullable(FrequencyLoader.class.getClassLoader().getResource("firstnames.csv"))
                .map(URL::getFile)
                .orElseThrow();
        List<FirstnameFileLine> firstnames = FrequencyLoader.loadFirstnames(firstnameFile);

        Map<String, RandomCollection<String>> transitionsMap = FrequencyLoader.getTransitionMap(firstnames.stream()
                .filter(f -> f.gender().equals(Gender.MALE) && f.language().equals(Language.FRENCH))
                .map(FirstnameFileLine::firstname));

        for (int i = 0; i < 50; i++) {
            String randomFirstname = generateRandomFirstname(transitionsMap);
            log.atInfo().log("generated : {}", randomFirstname);
        }
    }

    private static String generateRandomFirstname(Map<String, RandomCollection<String>> transitionsMap) {
        String firstLetter = getRandomItem(transitionsMap.keySet().stream().filter(s -> s.length() == 1).toList());
        StringBuilder firstname = new StringBuilder(firstLetter);
        RandomCollection<String> getNextProbableLetters;
        while ((getNextProbableLetters = getGetNextProbableLetters(transitionsMap, firstname)) != null) {
            firstname.append(getNextProbableLetters.next());
        }

        return firstname.toString();
    }

    private static RandomCollection<String> getGetNextProbableLetters(Map<String, RandomCollection<String>> transitionsMap, StringBuilder firstname) {
        return transitionsMap.get(firstname.length() < 3 ? firstname.toString() : firstname.substring(firstname.length() - 3));
    }

    public static <T> T getRandomItem(List<T> list) {
        return list.get(generator.nextInt(list.size()));
    }
}
