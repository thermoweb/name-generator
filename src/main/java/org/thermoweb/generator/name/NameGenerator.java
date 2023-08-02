package org.thermoweb.generator.name;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.random.RandomGenerator;

import org.thermoweb.generator.name.commands.GenerateCommand;

import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;

@Slf4j
@CommandLine.Command(name = "ng", subcommands = GenerateCommand.class)
public class NameGenerator implements Runnable {
    private static final RandomGenerator generator = RandomGenerator.getDefault();
    public static List<FirstnameFileLine> firstnames = loadFile();

    public static void main(String[] args) {
        int exitCode = new CommandLine(new NameGenerator())
                .setCaseInsensitiveEnumValuesAllowed(true)
                .execute(args);
        System.exit(exitCode);
    }

    private static void exemple() {
        Map<String, RandomCollection<String>> transitionsMap = FrequencyLoader.getTransitionMap(firstnames.stream()
                .filter(f -> f.gender().equals(Gender.MALE) && f.language().equals(Language.FRENCH))
                .map(FirstnameFileLine::firstname));

        for (int i = 0; i < 50; i++) {
            String randomFirstname = generateRandomFirstname(transitionsMap);
            log.atInfo().log("generated : {}", randomFirstname);
        }
    }

    private static List<FirstnameFileLine> loadFile() {
        String firstnameFile = Optional.ofNullable(NameGenerator.class.getClassLoader().getResource("firstnames.csv"))
                .map(URL::getFile)
                .orElseThrow();
        return FrequencyLoader.loadFirstnames(firstnameFile);
    }

    public static String generateRandomFirstname(Map<String, RandomCollection<String>> transitionsMap) {
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

    @Override
    public void run() {
        log.atInfo().log("ng runned");
    }
}
