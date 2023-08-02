package org.thermoweb.generator.name.commands;

import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;

@Slf4j
@CommandLine.Command(name = "ng", subcommands = GenerateCommand.class)
public class NameGeneratorCommand implements Runnable {

    public static void main(String[] args) {
        int exitCode = new CommandLine(new NameGeneratorCommand())
                .setCaseInsensitiveEnumValuesAllowed(true)
                .execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() {
        log.atDebug().log("ng runned");
    }
}
