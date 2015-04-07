package com.excilys.cdb.persistence.dao;

import java.util.List;

import org.apache.commons.lang.NotImplementedException;


/**
 * Data access object interface. Establish connection to BDD, & offers several
 * services.
 *
 * @author Nicolas THIERION
 * @version 0.3.0
 */
public interface ICrudDao<T> {


    /**
     * List entities. Order results by name. Search is unbounded, return all
     * existing entities.
     *
     * @return the list of results.
     */
    default List<T> listByName() {
        throw new NotImplementedException("Method not implemented");
    }


    /**
     * List entities. Order results by name. Search is bounded by offset & nb
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
    default List<T> listByName(int offset, int nb) {
        throw new NotImplementedException("Method not implemented");
    }

    /**
     * List entities with name matching given 'name' parameter. Order results by
     * name.
     *
     * @param offset
     *            Search offset.
     * @param nb
     *            Number of results to fetch. Set a negative value for unbounded
     *            search.
     * @param name
     *            Entity name to search for, non case sensitive.
     * @throw IllegalArgumentException if name is invalid (ie : null) or if
     *        search offset is negative
     * @return the list of results.
     */
    default List<T> listLikeName(int offset, int nb, String name) {
        throw new NotImplementedException("Method not implemented");
    }

    /**
     * Search an entity in DB given its id.
     *
     * @param id
     *            Id of the requested entity.
     * @return the first entity that matches, or null if no entity found.
     * @throws IllegalArgumentException
     *             if the given id is invalid. Valid id must be positive.
     */
    default T searchById(long id) throws IllegalArgumentException {
        throw new NotImplementedException("Method not implemented");
    }

    /**
     * @return count of entities entries in database.
     */
    default int getCount() {
        throw new NotImplementedException("Method not implemented");
    }

    /**
     * @param name
     *            Name of entities to search for.
     * @return count of entity entries in database that matches the given name.
     * @throws if
     *             name is null.
     */
    default int getCount(String name) throws IllegalArgumentException {
        throw new NotImplementedException("Method not implemented");
    }


    /**
     * Adds the entity to DB, provided that this entity doesn't exist already.
     *
     * @param entity
     *            Entity to add.
     * @return the added computer.
     * @throw DaoException when trying to add an invalid computer. An invalid
     *        computer is a computer with a non blank field "Computer id". See
     *        "updateComputer()" if you want to update computer information of
     *        an existing computer. Computer will receive a new Id if adding
     *        succeed.
     */
    default T add(T computer) throws DaoException {
        throw new NotImplementedException("Method not implemented");
    }

    /**
     * Update the given entity.
     *
     * @param entity
     *            Entity to update.
     * @return the updated entity.
     * @throws DaoException
     *             if update failed or entity doesn't exist.
     * @throws IllegalArgumentException
     *             if provided entity is invalid.
     */
    default T update(T computer) throws DaoException, IllegalArgumentException {
        throw new NotImplementedException("Method not implemented");
    }

    /**
     * Delete the given compute from DB.
     *
     * @param entity
     *            The entity to delete.
     * @throws DaoException
     *             if deletion failed or entity doesn't exist.
     * @throws IllegalArgumentException
     *             if provided entity is invalid.
     */
    default void delete(T computer) throws DaoException, IllegalArgumentException {
        throw new NotImplementedException("Method not implemented");
    }

    /**
     * Delete the given compute from DB.
     *
     * @param id
     *            The id of the entity to delete.
     * @throws DaoException
     *             if deletion failed or entity doesn't exist.
     * @throws IllegalArgumentException
     *             if provided entity is invalid.
     */
    default void delete(Long id) throws DaoException, IllegalArgumentException {
        throw new NotImplementedException("Method not implemented");
    }



}
