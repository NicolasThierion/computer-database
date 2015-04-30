package com.excilys.cdb.service;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import com.excilys.cdb.model.Identifiable;

/**
 *
 * @author Nicolas THIERION
 * @version 0.2.0
 *
 */
public interface ICrudService<I, T extends Identifiable<I>> {

    /**
     * List entities. Order results by name.
     *
     * @param offset
     *            Search offset.
     * @param count
     *            Number of results to fetch. Set a negative value for unbounded
     *            search.
     * @throw IllegalArgumentException if search offset is negative.
     * @return the list of entities.
     */
    default List<T> listByName(int offset, int count) {
        return listLikeName(offset, count, "");
    }

    // TODO doc
    default List<T> listByName() {
        return listByName(0, Integer.MAX_VALUE);
    }

    /**
     * List entities with name matching given 'name' parameter. Order results by
     * name.
     *
     * @param offset
     *            Search offset.
     * @param count
     *            Number of results to fetch. Set a negative value for unbounded
     *            search.
     * @param name
     *            Entity name to search for, non case sensitive.
     * @throw IllegalArgumentException if name is invalid (ie : empty) or if
     *        search offset is negative
     * @return the list of results.
     */
    default List<T> listLikeName(int offset, int count, String name) {
        throw new UnsupportedOperationException();
    }

    default List<T> listLikeName(String name) {
        return listLikeName(0, Integer.MAX_VALUE, name);
    }

    /**
     * Adds the entity to DB, provided that this entity doesn't exist already.
     * Calling this method will assign a new "entity id" to the entity. Entity
     * will receive a new Id if adding succeed.
     *
     * @param entity
     *            Entity to add.
     * @return the added entity.
     * @throw IllegalArgumentException when trying to add an invalid entity. An
     *        invalid entity is a entity with a non blank field "Entity id". See
     *        "update()" if you want to update entity information of an existing
     *        entity.
     */
    default T add(T entity) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    /**
     * Attempt to retrieve a entity given its id. Throws an exception if not
     * found.
     *
     * @param entityId
     *            Id of the entity to retrieve.
     * @return The entity.
     * @throws NoSuchElementException
     *             if no entity with this id can be found.
     * @throws IllegalArgumentException
     *             if the given id is invalid. Valid id must be positive.
     */
    default T retrieve(I entityId) throws NoSuchElementException, IllegalArgumentException {
        final T entity;
        entity = search(entityId);
        if (entity == null) {
            throw new NoSuchElementException("no entity with id = " + entityId + " was found.");
        }

        return entity;
    }

    /**
     * Attempt to retrieve a entity given its id. Return null if not found.
     *
     * @param entityId
     *            Id of the entity to retrieve.
     * @return The entity.
     * @throws IllegalArgumentException
     *             if the given id is invalid. Valid id must be positive.
     */
    default T search(I entityId) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    /**
     * Update the given entity.
     *
     * @param entity
     *            entity to update.
     * @return the updated entity.
     * @throws NoSuchElementException
     *             if no entity with this id can be found.
     */
    default void update(T entity) throws NoSuchElementException {
        throw new UnsupportedOperationException();
    }

    /**
     * Delete the given compute from DB.
     *
     * @param entity
     *            The entity to delete.
     * @throws NoSuchElementException
     *             if no entity with this id can be found.
     */
    default void delete(T entity) throws NoSuchElementException {
        delete(entity.getId());
    }

    /**
     * Delete the given compute from DB.
     *
     * @param ids
     *            The id of entity to delete.
     * @throws NoSuchElementException
     *             if no entity with this id can be found.
     */
    default void delete(I id) throws NoSuchElementException {
        final LinkedList<I> list = new LinkedList<I>();
        list.add(id);
        delete(list);
    }

    default void delete(List<I> ids) throws NoSuchElementException {
        throw new UnsupportedOperationException();
    }

    /**
     * @return count of entity entries in database.
     */
    default int getCount() {
        return getCount("");
    }


    /**
     * @param name
     *            Name of entities to search for.
     * @return count of entity entries in database that matches the given name.
     */
    default int getCount(String name) {
        throw new UnsupportedOperationException();
    }

}
