package com.excilys.cdb.service;

import java.util.List;
import java.util.NoSuchElementException;

import com.excilys.cdb.model.Company;

/**
 *
 * @author Nicolas THIERION
 * @version 0.3.0
 */
public interface ICompanyService extends ICrudService<Company> {

    @Override
    default List<Company> listByName() {
        return listByName(0, Integer.MAX_VALUE);
    }

    /**
     * List companies with name matching given 'name' parameter. Order results
     * by name.
     *
     * @param offset
     *            Search offset.
     * @param nb
     *            Number of results to fetch. Set a negative value for unbounded
     *            search.
     * @param name
     *            Company name to search for, non case sensitive.
     * @throw IllegalArgumentException if name is invalid (ie : empty) or if
     *        search offset is negative
     * @return the list of results.
     */
    @Override
    List<Company> listLikeName(int offset, int count, String name);

    /**
     * Adds the company to DB, provided that this company doesn't exist already.
     * Calling this method will assign a new "company id" to the company.
     * Company will receive a new Id if adding succeed.
     *
     * @param company
     *            Company to add.
     * @return the added company.
     * @throw IllegalArgumentException when trying to add an invalid company. An
     *        invalid company is a company with a non blank field "Company id".
     *        See "update()" if you want to update company information of an
     *        existing company.
     */
    @Override
    Company add(Company company) throws IllegalArgumentException;

    /**
     * Attempt to retrieve a company given its id. Return null if not found.
     *
     * @param companyId
     *            Id of the company to retrieve.
     * @return The company.
     * @throws IllegalArgumentException
     *             if the given id is invalid. Valid id must be positive.
     */
    @Override
    Company search(long companyId) throws IllegalArgumentException;

    /**
     * Delete the given compute from DB.
     *
     * @param id
     *            The id of company to delete.
     * @throws NoSuchElementException
     *             if no company with this id can be found.
     */
    @Override
    void delete(Long id) throws NoSuchElementException;

    /**
     * @param name
     *            Name of companies to search for.
     * @return count of company entries in database that matches the given name.
     */
    @Override
    int getCount(String name);
}
