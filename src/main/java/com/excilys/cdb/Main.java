package com.excilys.cdb;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.cli.CliCommand;
import com.excilys.cdb.cli.CliContext;

public final class Main {
    private Main() {
    }


    public static void main(String[] args) throws Exception {
        final Scanner scanner = new Scanner(System.in);

        final CliContext context = new CliContext(scanner);

        final Logger logger = LoggerFactory.getLogger(Main.class);
        logger.info("Program started");
        while (!context.isExit()) {
            CliCommand.getCommand(scanner.next()).execute(context);
        }
        logger.info("Program terminated");
    }

}
