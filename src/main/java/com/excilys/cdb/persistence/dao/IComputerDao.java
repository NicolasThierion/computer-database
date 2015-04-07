package com.excilys.cdb.persistence.dao;

import java.util.List;

import com.excilys.cdb.model.Computer;


/**
 * Data access object interface. Establish connection to BDD, & offers several
 * services.
 *
 * @author Nicolas THIERION
 * @version 0.3.0
 */
public interface IComputerDao extends ICrudDao<Computer> {

    /**
     * List computers. Order results by name. Search is unbounded, return all
     * existing computers.
     *
     * @return the list of results.
     */
    @Override
    List<Computer> listByName();


        /**
     * List computers. Order results by name. Search is bounded by offset & nb
     * params.
     *
     * @param offset
     *            Search offset.
     * @param nb
     *            Number of results to fetch. Set a negative value for unbounded
     *            search.
     * @throw IllegalArgumentException if search offset is negative.
     * @return the list of results.
     */
    @Override
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
     * @throw IllegalArgumentException if name is invalid (ie : null) or if
     *        search offset is negative
     * @return the list of results.
     */
    @Override
    List<Computer> listLikeName(int offset, int nb, String name);

    /**
     * Search a computer in DB given its id.
     *
     * @param id
     *            Id of the requested computer.
     * @return the first computer that matches, or null if no computer found.
     * @throws IllegalArgumentException
     *             if the given id is invalid. Valid id must be positive.
     */
    @Override
    Computer searchById(long id) throws IllegalArgumentException;

    /**
     * @return count of computer entries in database.
     */
    @Override
    int getCount();

    /**
     * @param name
     *            Name of computers to search for.
     * @return count of computer entries in database that matches the given
     *         name.
     * @throws if
     *             name is null.
     */
    @Override
    int getCount(String name) throws IllegalArgumentException;


    /**
     * Adds the computer to DB, provided that this computer doesn't exist
     * already. Calling this method will assign a new "computer id" to the
     * computer.
     *
     * @param computer
     *            Computer to add.
     * @return the added computer.
     * @throw DaoException when trying to add an invalid computer. An invalid
     *        computer is a computer with a non blank field "Computer id". See
     *        "updateComputer()" if you want to update computer information of
     *        an existing computer. Computer will receive a new Id if adding
     *        succeed.
     */
    @Override
    Computer add(Computer computer) throws DaoException;

    /**
     * Update the given computer.
     *
     * @param computer
     *            Computer to update.
     * @return the updated computer.
     * @throws DaoException
     *             if update failed or computer doesn't exist.
     * @throws IllegalArgumentException
     *             if provided computer is invalid.
     */
    @Override
    Computer update(Computer computer) throws DaoException, IllegalArgumentException;

    /**
     * Delete the given compute from DB.
     *
     * @param computer
     *            The computer to delete.
     * @throws DaoException
     *             if deletion failed or computer doesn't exist.
     * @throws IllegalArgumentException
     *             if provided computer is invalid.
     */
    @Override
    default void delete(Computer computer) throws DaoException, IllegalArgumentException {
        delete(computer.getId());
    }

    /**
     * Delete the given compute from DB.
     *
     * @param id
     *            The id of the computer to delete.
     * @throws DaoException
     *             if deletion failed or computer doesn't exist.
     * @throws IllegalArgumentException
     *             if provided computer is invalid.
     */
    @Override
    void delete(Long id) throws DaoException, IllegalArgumentException;


}
