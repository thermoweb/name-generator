package org.thermoweb.generator.name;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.random.RandomGenerator;

import org.thermoweb.generator.name.commands.GenerateCommand;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;

@Slf4j
@CommandLine.Command(name = "ng", subcommands = GenerateCommand.class)
public class NameGenerator implements Runnable {
    private static final RandomGenerator generator = RandomGenerator.getDefault();

    @Getter
    private static final List<FirstnameData> firstnames = loadFirstnamesFile();

    @Getter
    private static final List<String> names = loadNamesFile();

    public static void main(String[] args) {
        int exitCode = new CommandLine(new NameGenerator())
                .setCaseInsensitiveEnumValuesAllowed(true)
                .execute(args);
        System.exit(exitCode);
    }

    private static void exemple() {
        Map<String, RandomCollection<String>> transitionsMap = FrequencyLoader.getTransitionMap(firstnames.stream()
                .filter(f -> f.gender().equals(Gender.MALE) && f.language().equals(Language.FRENCH))
                .map(FirstnameData::firstname));

        for (int i = 0; i < 50; i++) {
            String randomFirstname = generateRandomFirstname(transitionsMap, 10);
            log.atInfo().log("generated : {}", randomFirstname);
        }
    }

    private static List<FirstnameData> loadFirstnamesFile() {
        log.atInfo().log("loading firstnames file...");
        String firstnameFile = Optional.ofNullable(NameGenerator.class.getClassLoader().getResource("firstnames.csv"))
                .map(URL::getFile)
                .orElseThrow();
        return FrequencyLoader.loadFirstnames(firstnameFile);
    }

    private static List<String> loadNamesFile() {
        log.atInfo().log("loading names file...");
        String namesFile = Optional.ofNullable(NameGenerator.class.getClassLoader().getResource("names.csv"))
                .map(URL::getFile)
                .orElseThrow();
        return FrequencyLoader.loadNames(namesFile);
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

    public static <T> T getRandomItem(List<T> list) {
        return list.get(generator.nextInt(list.size()));
    }

    @Override
    public void run() {
        log.atInfo().log("ng runned");
    }
}
