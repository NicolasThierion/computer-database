package com.excilys.cdb.cli;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.service.ServiceException;

/**
 * Pattern command for the processing of actions.
 *
 * @author Edwin Oursel
 */
public enum CliCommand {

    /**
     * Retrieve all computers.
     */
    GET_ALL_COMPUTERS("getAllComputers") {

        @Override
        public void execute(CliContext context) throws ServiceException {
            if (context == null) {
                throw new IllegalArgumentException();
            }
            context.setComputers(context.getComputerService().listByName());
            System.out.println(context.getComputers());
        }

    },
    /**
     * Retrieve all companies.
     */
    GET_ALL_COMPANIES("getAllCompanies") {

        @Override
        public void execute(CliContext context) throws ServiceException {
            if (context == null) {
                throw new IllegalArgumentException();
            }
            context.setCompanies(context.getCompanyService().listByName());
            System.out.println(context.getCompanies());
        }

    },
    /**
     * Retrieve a computer.
     */
    GET_BY_ID_COMPUTER("getByIdComputer") {

        @Override
        public void execute(CliContext context) throws ServiceException {
            if (context == null) {
                throw new IllegalArgumentException();
            }
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
    CREATE_COMPUTER("createComputer") {

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
    UPDATE_COMPUTER("updateComputer") {

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
    DELETE_COMPUTER("deleteComputer") {

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
            System.out.print("-getAllComputers");
            System.out.print("-getAllCompanies");
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
            logger.info("Program terminated with command exit");
        }

    };

    private static Map<String, CliCommand> commands;
    static {
        commands = new HashMap<>();
        for (final CliCommand com : CliCommand.values()) {
            commands.put(com.commandLabel, com);
        }
    }

    private static Logger               logger = LoggerFactory.getLogger(CliCommand.class);

    private final String                commandLabel;

    private CliCommand(String commandLabel) {
        this.commandLabel = commandLabel;
    }

    /*
     * Populate a computer model from answers given by the user.
     */
    private static void populate(CliContext context, Computer computer) {
        //TODO
        /*
        System.out.println("Name : ");
        if (context.getScanner().hasNext()) {
            computer.setName(context.getScanner().next());
        }
        System.out.println("Introduced :");
        if (context.getScanner().hasNext()) {
            final String tok = context.getScanner().next();
            final StringBuilder sb = new StringBuilder();
            sb.append(tok).append(" ").append("00:00:00");
            if (ComputerDatabaseValidator.INSTANCE.validateDateTime(sb.toString())) {
                final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                final LocalDateTime dateTime = LocalDateTime.parse(sb.toString(), formatter);
                computer.setIntroducedDate(dateTime);
            }
        }
        System.out.println("Discontinued :");
        if (context.getScanner().hasNextToken()) {
            final String tok = context.getScanner().getNextToken();
            final StringBuilder sb = new StringBuilder();
            sb.append(tok).append(" ").append("00:00:00");
            if (ComputerDatabaseValidator.INSTANCE.validateDateTime(sb.toString())) {
                final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                final LocalDateTime dateTime = LocalDateTime.parse(sb.toString(), formatter);
                computer.setDiscontinuedDate(dateTime);
            }
        }
        System.out.println("Company id : ");
        if (context.getScanner().hasNextToken()) {
            final Company c = new Company();
            c.setId(Long.valueOf(context.getScanner().getNextToken()));
            computer.setCompany(c);
        }

        */
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
            logger.info("Bad command entered, redirected on help");
            c = CliCommand.HELP;
        }
        return c;
    }

    public abstract void execute(CliContext context) throws ServiceException;



}
