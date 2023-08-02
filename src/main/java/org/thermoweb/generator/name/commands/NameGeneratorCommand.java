package org.thermoweb.generator.name.commands;

import java.util.List;
import java.util.Map;
import java.util.random.RandomGenerator;

import org.thermoweb.generator.name.FirstnameData;
import org.thermoweb.generator.name.FrequencyLoader;
import org.thermoweb.generator.name.RandomCollection;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;

@Slf4j
@CommandLine.Command(name = "ng", subcommands = GenerateCommand.class)
public class NameGeneratorCommand implements Runnable {

    @Getter
    private static final List<FirstnameData> firstnames = loadFirstnamesFile();

    @Getter
    private static final List<String> names = loadNamesFile();

    public static void main(String[] args) {
        int exitCode = new CommandLine(new NameGeneratorCommand())
                .setCaseInsensitiveEnumValuesAllowed(true)
                .execute(args);
        System.exit(exitCode);
    }

    private static List<FirstnameData> loadFirstnamesFile() {
        log.atInfo().log("loading firstnames file...");
        return FrequencyLoader.loadFirstnames(Thread.currentThread().getContextClassLoader().getResourceAsStream("firstnames.csv"));
    }

    private static List<String> loadNamesFile() {
        log.atInfo().log("loading names file...");
        return FrequencyLoader.loadNames(Thread.currentThread().getContextClassLoader().getResourceAsStream("names.csv"));
    }

    @Override
    public void run() {
        log.atDebug().log("ng runned");
    }
}
