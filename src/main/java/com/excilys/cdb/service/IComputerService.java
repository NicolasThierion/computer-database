package com.excilys.cdb.service;

import java.util.List;
import java.util.NoSuchElementException;

import com.excilys.cdb.model.Computer;

/**
 *
 * @author Nicolas THIERION
 * @version 0.2.0
 *
 */
public interface IComputerService {

    /* ***
     * COMPUTER METHODS
     */
    /**
     * List computers. Order results by name.
     *
     * @param offset
     *            Search offset.
     * @param nb
     *            Number of results to fetch. Set a negative value for unbounded
     *            search.
     * @throw IllegalArgumentException if search offset is negative.
     * @return the list of computers.
     */
    List<Computer> listByName(int offset, int count);

    // TODO doc
    default List<Computer> listByName() {
        return listByName(0, Integer.MAX_VALUE);
    }

    /**
     * List computers with name matching given 'name' parameter. Order results
     * by name.
     *
     * @param offset
     *            Search offset.
     * @param nb
     *            Number of results to fetch. Set a negative value for unbounded
     *            search.
     * @param name
     *            Computer name to search for, non case sensitive.
     * @throw IllegalArgumentException if name is invalid (ie : empty) or if
     *        search offset is negative
     * @return the list of results.
     */
    List<Computer> listLikeName(int offset, int count, String name);

    default List<Computer> listLikeName(String name) {
        return listLikeName(0, Integer.MAX_VALUE, name);
    }

    /**
     * Adds the computer to DB, provided that this computer doesn't exist
     * already. Calling this method will assign a new "computer id" to the
     * computer. Computer will receive a new Id if adding succeed.
     *
     * @param computer
     *            Computer to add.
     * @return the added computer.
     * @throw IllegalArgumentException when trying to add an invalid computer.
     *        An invalid computer is a computer with a non blank field
     *        "Computer id". See "updateComputer()" if you want to update
     *        computer information of an existing computer.
     */
    Computer add(Computer computer) throws IllegalArgumentException;

    /**
     * Attempt to retrieve a computer given its id. Throws an exception if not
     * found.
     *
     * @param computerId
     *            Id of the computer to retrieve.
     * @return The computer.
     * @throws NoSuchElementException
     *             if no computer with this id can be found.
     * @throws IllegalArgumentException
     *             if the given id is invalid. Valid id must be positive.
     */
    Computer retrieve(long computerId) throws NoSuchElementException, IllegalArgumentException;

    /**
     * Attempt to retrieve a computer given its id. Return null if not found.
     *
     * @param computerId
     *            Id of the computer to retrieve.
     * @return The computer.
     * @throws IllegalArgumentException
     *             if the given id is invalid. Valid id must be positive.
     */
    Computer search(long computerId) throws IllegalArgumentException;

    /**
     * Update the given computer.
     *
     * @param computer
     *            Computer to update.
     * @return the updated computer.
     * @throws NoSuchElementException
     *             if no computer with this id can be found.
     */
    void update(Computer computer) throws NoSuchElementException;

    /**
     * Delete the given compute from DB.
     *
     * @param computer
     *            The computer to delete.
     * @throws NoSuchElementException
     *             if no computer with this id can be found.
     */
    default void delete(Computer computer) throws NoSuchElementException {
        delete(computer.getId());
    }

    /**
     * Delete the given compute from DB.
     *
     * @param id
     *            The id of computer to delete.
     * @throws NoSuchElementException
     *             if no computer with this id can be found.
     */
    void delete(Long id) throws NoSuchElementException;

    /**
     * @return count of computer entries in database.
     */
    default int getCount() {
        return getCount("");
    }


    /**
     * @param name
     *            Name of computers to search for.
     * @return count of computer entries in database that matches the given
     *         name.
     */
    int getCount(String name);


}
