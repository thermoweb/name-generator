package org.thermoweb.generator.name.commands;

import java.util.Map;
import java.util.Optional;

import org.thermoweb.generator.name.FirstnameData;
import org.thermoweb.generator.name.FrequencyLoader;
import org.thermoweb.generator.name.Gender;
import org.thermoweb.generator.name.Language;
import org.thermoweb.generator.name.NameGenerator;
import org.thermoweb.generator.name.RandomCollection;
import org.thermoweb.generator.name.Type;

import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;

@Slf4j
@CommandLine.Command(name = "generate")
public class GenerateCommand implements Runnable {

    @CommandLine.Option(names = {"--number", "-n"}, defaultValue = "10")
    private int number;

    @CommandLine.Option(names = {"--size", "-s"}, defaultValue = "30")
    private int size;

    @CommandLine.Option(names = {"--gender", "-g"})
    private Gender gender;

    @CommandLine.Option(names = {"--language", "-l"}, defaultValue = "ENGLISH")
    private Language language;

    @CommandLine.Option(names = {"--type", "-t"}, defaultValue = "firstname")
    private Type type;

    @Override
    public void run() {
        log.atInfo().log("Generating {} {} in {}", number, type, language);
        Map<String, RandomCollection<String>> transitionsMap = getTransitionsMap();

        for (int i = 0; i < number; i++) {
            String randomFirstname = NameGenerator.generateRandomFirstname(transitionsMap, size);
            log.atInfo().log("generated : {}", randomFirstname);
        }
    }

    private Map<String, RandomCollection<String>> getTransitionsMap() {
        if (Type.FIRSTNAME.equals(type)) {
            return FrequencyLoader.getTransitionMap(NameGenerator.getFirstnames().stream()
                    .filter(f -> Optional.ofNullable(gender).map(g -> g.equals(f.gender())).orElse(true)
                            && language.equals(f.language()))
                    .map(FirstnameData::firstname));
        }

        return FrequencyLoader.getTransitionMap(NameGenerator.getNames().stream());
    }
}
