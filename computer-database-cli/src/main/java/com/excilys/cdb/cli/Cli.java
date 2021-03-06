package com.excilys.cdb.cli;

import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.dao.mysql.CompanyDao;
import com.excilys.cdb.persistence.dao.mysql.ComputerDao;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.service.ICompanyService;
import com.excilys.cdb.service.IComputerService;

/**
 * This is the main class for the UI; This program displays a CLI to control
 * computer equipments.
 *
 * @author Nicolas THIERION
 * @version 0.2.0
 *
 */
public final class Cli {

    private static IComputerService computerService = new ComputerService(ComputerDao.getInstance());
    private static ICompanyService  companyService  = new CompanyService(CompanyDao.getInstance());

    private static final int PAGE_SIZE   = 10;

    private Cli() {

    }

    private static void printUsage(PrintStream stream) {
        printUsage(stream, null);
    }

    private static void printUsage(PrintStream stream, String command) {
        if (command == null) {
            stream.println("usage : ");
            stream.println("help | h  : prints this message ");
            stream.println("list | l computers | companies : list computers or companies");
        } else if (command.equals("list") || command.equals("l")) {
            stream.println("usage : list computers | companies");
        }
    }

    private static void printInvalidParam(String paramName) {
        System.err.println("Invalid parameter : " + paramName);
    }

    private static void printComputerList() {
        int begin = 0;
        List<Computer> list;
        final Scanner sin = new Scanner(System.in);
        do {
            list = computerService.listByName(begin, PAGE_SIZE);
            printList(list);
            begin += PAGE_SIZE;
            System.out.println("'n' to print next page, 'q' to abort");

            String s = null;
            do {
                s = sin.next();
            } while (!(s.equals("q") || s.equals("n")));
            if (s.equals("q")) {
                break;
            }

        } while (list.size() > 0);
        sin.close();
    }

    private static void printCompaniesList() {
        int begin = 0;
        List<Company> list;
        final Scanner sin = new Scanner(System.in);
        do {
            list = companyService.listByName(begin, PAGE_SIZE);
            printList(list);
            begin += PAGE_SIZE;
            System.out.println("'n' to print next page, 'q' to abort");

            String s = null;
            do {
                s = sin.next();
            } while (!(s.equals("q") || s.equals("n")));
            if (s.equals("q")) {
                break;
            }

        } while (list.size() > 0);
        sin.close();
    }

    private static void printList(List<?> list) {
        for (final Object o : list) {
            System.out.println(o.toString());
        }
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage(System.out);
            return;
        }

        // help
        if (args[0].equals("help") || args[0].equals("h")) {
            if (args.length > 1) {
                printInvalidParam(args[1]);
                return;
            }
            printUsage(System.out);

        }

        // list
        if (args[0].equals("list") || args[0].equals("l")) {

            if (args.length == 1) {
                printUsage(System.err, "list");
            } else {
                if (args[1].equals("computers")) {
                    printComputerList();
                } else if (args[1].equals("companies")) {
                    printCompaniesList();
                }
            }
        }
    }

}
