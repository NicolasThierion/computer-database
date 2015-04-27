package com.excilys.cdb.cli;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.service.ServiceException;
import com.excilys.cdb.validator.DateValidator;

/**
 * Pattern command for the processing of actions.
 *
 * @author Edwin Oursel
 */
public enum CliCommand {

    /**
     * Retrieve all computers.
     */
    LIST_COMPUTERS("list computers") {

        @Override
        public void execute(CliContext context) throws ServiceException {
            context.setComputers(context.getComputerService().listByName());
            System.out.println(context.getComputers());
        }

    },
    /**
     * Retrieve all companies.
     */
    LIST_COMPANIES("list companies") {

        @Override
        public void execute(CliContext context) throws ServiceException {
            context.setCompanies(context.getCompanyService().listByName());
            System.out.println(context.getCompanies());
        }

    },
    /**
     * Retrieve a computer.
     */
    GET_BY_ID_COMPUTER("list computers id=") {

        @Override
        public void execute(CliContext context) throws ServiceException {
            System.out.print("Identifier : ");
            final Long computerId = Long.valueOf(context.getScanner().next());
            context.setComputerId(computerId);
            context.setComputers(Arrays.asList(context.getComputerService().search(computerId)));
            System.out.println(context.getComputers());
        }

    },
    /**
     * Create a new computer.
     */
    CREATE_COMPUTER("create computer") {

        @Override
        public void execute(CliContext context) throws ServiceException {
            if (context == null) {
                throw new IllegalArgumentException();
            }
            Computer computer = new Computer();
            populate(context, computer);
            context.setNewComputer(computer);
            computer = context.getComputerService().add(computer);
            context.setComputerId(computer.getId());
            if (context.getComputerId() > 0) {
                System.out.println("Successfully created");
            } else {
                System.out.println("Failed to create");
            }
        }

    },
    /**
     * Update a computer.
     */
    UPDATE_COMPUTER("update computer") {

        @Override
        public void execute(CliContext context) throws ServiceException {
            if (context == null) {
                throw new IllegalArgumentException();
            }
            System.out.println("Identifier : ");
            final Computer computer = context.getComputerService().search(Long.valueOf(context.getScanner().next()));
            populate(context, computer);
            context.setNewComputer(computer);
            context.getComputerService().update(context.getNewComputer());
            System.out.println("UPDATED");
        }

    },
    /**
     * Delete a computer.
     */
    DELETE_COMPUTER("delete computer") {

        @Override
        public void execute(CliContext context) throws ServiceException {
            if (context == null) {
                throw new IllegalArgumentException();
            }
            System.out.print("Identifier : ");
            context.setComputerId(Long.valueOf(context.getScanner().next()));
            context.getComputerService().delete(context.getComputerId());
            System.out.println("Deleted");
        }

    },
    /**
     * Print some help.
     */
    HELP("help") {

        @Override
        public void execute(CliContext context) throws ServiceException {
            if (context == null) {
                throw new IllegalArgumentException();
            }
            System.out.print("Legal commands : ");
            for (final CliCommand command : values()) {
                System.out.println(command.mCommandLabel);
            }
        }

    },
    /**
     * Command to terminate a program.
     */
    EXIT("exit") {

        @Override
        public void execute(CliContext context) throws ServiceException {
            if (context == null) {
                throw new IllegalArgumentException();
            }
            System.out.println("Program terminated");
            context.setExit(true);
        }

    };

    /* ***
     * ATTRIBUTES
     */
    /** maps the String commands to the corresponding CliCommands. */
    private static Map<String, CliCommand> commands;
    static {
        commands = new HashMap<>();
        for (final CliCommand com : CliCommand.values()) {
            commands.put(com.mCommandLabel, com);
        }
    }

    private final String                mCommandLabel;

    private CliCommand(String commandLabel) {
        mCommandLabel = commandLabel;
    }

    /*
     * Populate a computer model from answers given by the user.
     */
    private static void populate(CliContext context, Computer computer) {
        //TODO

        System.out.println("Name : ");
        if (context.getScanner().hasNext()) {
            computer.setName(context.getScanner().next());
        }
        System.out.println("Introduced :");
        if (context.getScanner().hasNext()) {
            final String tok = context.getScanner().next();

            if (new DateValidator().isValid(tok, true)) {
                final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                final LocalDate releaseDate = LocalDate.parse(tok, formatter);
                computer.setReleaseDate(releaseDate);
            }
        }
        System.out.println("Discontinued :");
        if (context.getScanner().hasNext()) {
            final String tok = context.getScanner().next();
            if (new DateValidator().isValid(tok, true)) {
                final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                final LocalDate dateTime = LocalDate.parse(tok, formatter);
                computer.setDiscontinuedDate(dateTime);
            }
        }
        System.out.println("Company id : ");
        if (context.getScanner().hasNext()) {
            final Company c = new Company();
            c.setId(Long.valueOf(context.getScanner().next()));
            computer.setCompany(c);
        }

    }

    /**
     * Return a command from its textual value.
     *
     * @param command
     *            Textual command
     * @return The matching command
     */
    public static CliCommand getCommand(String command) {
        CliCommand c = commands.get(command);
        if (c == null) {
            c = CliCommand.HELP;
        }
        return c;
    }

    public abstract void execute(CliContext context) throws ServiceException;



}
