package com.excilys.cdb.dao;

import java.util.List;

import com.excilys.cdb.model.Computer;


/**
 * Data access object interface. Establish connection to BDD, & offers several services.
 * @author Nicolas THIERION
 * @version 0.2.0
 */
public interface IComputerDao {


    /**
     * List computers with name matching given 'name' parameter. Order results
     * by name.
     *
     * @param offset
     *            Search offset.
     * @param nb
     *            Number of results to fetch. Set a negative value for unbounded
     *            search.
     * @throw IllegalArgumentException if search offset is negative.
     * @return the list of results.
     */
    List<Computer> listByName(int offset, int nb);

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
    List<Computer> listLikeName(int offset, int nb, String name);

    /**
     * @return count of computer entries in database.
     */
    int getCount();

    /**
     * Adds the computer to DB, provided that this computer doesn't exist already.
     * @param computer Computer to add to DB.
     */
    void add(Computer computer);

    /**
     * Update the given computer.
     *
     * @param computer
     *            Computer to update.
     * @return the updated computer.
     */
    Computer update(Computer computer);

    /**
     * Delete the given compute from DB.
     *
     * @param computer
     *            The computer to delete.
     *
     */
    void delete(Computer computer);
}
