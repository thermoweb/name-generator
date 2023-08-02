package org.thermoweb.generator.name.commands;

import java.util.Map;
import java.util.Optional;

import org.thermoweb.generator.name.FirstnameData;
import org.thermoweb.generator.name.FrequencyLoader;
import org.thermoweb.generator.name.Gender;
import org.thermoweb.generator.name.Language;
import org.thermoweb.generator.name.NameGenerator;
import org.thermoweb.generator.name.RandomCollection;

import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;

@Slf4j
@CommandLine.Command(name = "generate")
public class GenerateCommand implements Runnable {

    @CommandLine.Option(names = {"--number", "-n"}, defaultValue = "10")
    private int number;

    @CommandLine.Option(names = {"--gender", "-g"})
    private Gender gender;

    @CommandLine.Option(names = {"--language", "-l"}, defaultValue = "ENGLISH")
    private Language language;

    @Override
    public void run() {
        log.atInfo().log("Generating {} firstnames in {}", number, language);
        Map<String, RandomCollection<String>> transitionsMap = FrequencyLoader.getTransitionMap(NameGenerator.firstnames.stream()
                .filter(f -> Optional.ofNullable(gender).map(g -> g.equals(f.gender())).orElse(true)
                        && language.equals(f.language()))
                .map(FirstnameData::firstname));

        for (int i = 0; i < number; i++) {
            String randomFirstname = NameGenerator.generateRandomFirstname(transitionsMap);
            log.atInfo().log("generated : {}", randomFirstname);
        }
    }
}
