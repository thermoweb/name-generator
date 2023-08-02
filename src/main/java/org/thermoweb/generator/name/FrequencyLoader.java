package org.thermoweb.generator.name;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FrequencyLoader {
    private static final String COMMA_DELIMITER = ";";
    private static final int SLICE_WINDOWS = 3;

    private FrequencyLoader() {

    }

    static List<FirstnameData> loadFirstnames(String firstnameFile) {
        List<FirstnameData> firstnames = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(firstnameFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                addFirstname(firstnames, line);
            }
        } catch (IOException e) {
            log.atWarn().log("oups : {}", e.getMessage());
        }
        return firstnames;
    }

    private static void addFirstname(List<FirstnameData> firstnames, String line) {
        try {
            firstnames.add(new FirstnameData(line.split(COMMA_DELIMITER)));
        } catch (IllegalArgumentException e) {
            log.atWarn().log("oups : {}", e.getMessage());
        }
    }

    public static Map<String, RandomCollection<String>> getTransitionMap(Stream<String> firstnames) {
        Map<String, Map<String, Integer>> transitionMap = new HashMap<>();
        firstnames.forEach(firstname -> {
            if (firstname.length() <= 2) {
                log.atDebug().log("handle short firstname slicing");
            } else {
                for (int i = 1; i < firstname.length(); i++) {
                    String precedence = firstname.substring(i > SLICE_WINDOWS ? i - SLICE_WINDOWS : 0, i);
                    String nextLetter = String.valueOf(firstname.charAt(i));
                    transitionMap.merge(precedence, Map.of(nextLetter, 1),
                            (old, recent) -> Stream.of(old, recent)
                                    .map(Map::entrySet)
                                    .flatMap(Collection::stream)
                                    .collect(Collectors.toMap(
                                            Map.Entry::getKey,
                                            Map.Entry::getValue,
                                            Integer::sum
                                    ))
                    );
                }
            }
        });

        Map<String, RandomCollection<String>> transitions = new HashMap<>();
        for (Map.Entry<String, Map<String, Integer>> entry : transitionMap.entrySet()) {
            RandomCollection<String> randomCollection = getStringRandomCollection(entry.getValue());
            transitions.put(entry.getKey(), randomCollection);
        }
        return transitions;
    }

    private static RandomCollection<String> getStringRandomCollection(Map<String, Integer> transitions) {
        double total = transitions.values().stream().mapToInt(Integer::intValue).sum();
        RandomCollection<String> randomCollection = new RandomCollection<>();
        for (Map.Entry<String, Integer> entry : transitions.entrySet()) {
            randomCollection.add(entry.getValue() / total, entry.getKey());
        }
        return randomCollection;
    }
}
