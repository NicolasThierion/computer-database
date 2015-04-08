package com.excilys.cdb.persistence.dao;

import java.util.List;
import java.util.NoSuchElementException;

import com.excilys.cdb.model.Identifiable;
import com.excilys.cdb.persistence.EntityField;

/**
 * Data access object interface. Establish connection to BDD, & offers several
 * services.
 *
 * @author Nicolas THIERION
 * @version 0.3.0
 */
public interface ICrudDao<T extends Identifiable<Long>> {

    /**
     * List entities. Order results by name. Search is unbounded, return all
     * existing entities.
     *
     * @param field
     *            Field to sort the entities by.
     * @return the list of results.
     */
    default List<T> listBy(EntityField<T> field) {
        return listBy(field, 0, Integer.MAX_VALUE);
    }


    /**
     * List entities. Order results by name. Search is bounded by offset & count
     * params.
     *
     * @param field
     *            Field to sort the entities by. * @param offset Search offset.
     * @param count
     *            Number of results to fetch. Set a negative value for unbounded
     *            search.
     * @throw IllegalArgumentException if search offset is negative.
     * @return the list of results.
     */
    default List<T> listBy(EntityField<T> field, int offset, int count) {
        return listLike(field, "", offset, count);
    }

    /**
     * List entities with name matching given 'name' parameter. Order results by
     * name.
     *
     * @param field
     *            Field to sort the entities by.
     * @param offset
     *            Search offset.
     * @param count
     *            Number of results to fetch. Set a negative value for unbounded
     *            search.
     * @param name
     *            Entity name to search for, non case sensitive.
     * @throw IllegalArgumentException if name is invalid (ie : null) or if
     *        search offset is negative
     * @return the list of results.
     */
    default List<T> listLike(EntityField<T> field, String value, int offset, int count) {
        throw new UnsupportedOperationException();
    }

    default List<T> listLike(EntityField<T> field, String value) {
        return listLike(field, value, 0, -1);
    }

    /**
     * Search an entity in DB given its id.
     *
     * @param value
     *            value of the requested entity.
     * @return the first entity that matches, or null if no entity found.
     * @throws IllegalArgumentException
     *             if the given id is invalid. Valid id must be positive.
     */
    T searchBy(EntityField<T> field, String value);

    /**
     * @return count of entities entries in database.
     */
    default int getCount() {
        throw new UnsupportedOperationException();
    }

    /**
     * @param name
     *            Name of entities to search for.
     * @return count of entity entries in database that matches the given name.
     * @throws if
     *             name is null.
     */
    default int getCount(String name) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }


    /**
     * Adds the entity to DB, provided that this entity doesn't exist already.
     *
     * @param entity
     *            Entity to add.
     * @return the added entity.
     * @throw DaoException when trying to add an invalid entity. An invalid
     *        entity is an entity with a non blank field "id". See "update()" if
     *        you want to update entity information of an existing entity.
     *        Entity may receive a new Id if adding succeed.
     */
    default T add(T entity) throws DaoException {
        throw new UnsupportedOperationException();
    }

    /**
     * Update the given entity.
     *
     * @param entity
     *            Entity to update.
     * @return the updated entity.
     * @throws DaoException
     *             if update failed.
     * @throws IllegalArgumentException
     *             if provided entity is invalid.
     * @throws NoSuchElementException
     *             if entity doesn't exist.
     */
    default T update(T entity) throws DaoException, IllegalArgumentException, NoSuchElementException {
        throw new UnsupportedOperationException();
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
     * @throws NoSuchElementException
     *             if entity doesn't exist.
     */
    default void delete(T entity) throws DaoException, IllegalArgumentException, NoSuchElementException {
        delete(entity.getId());
    }

    /**
     * Delete the given compute from DB.
     *
     * @param id
     *            The id of the entity to delete.
     * @throws DaoException
     *             if deletion failed.
     * @throws IllegalArgumentException
     *             if provided entity is invalid.
     */
    default void delete(Long id) throws DaoException, IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

}
