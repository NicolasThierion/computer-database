package com.excilys.cdb.dao;

import java.util.List;

import com.excilys.cdb.model.Company;


/**
 * Data access object interface. Establish connection to BDD, & offers several
 * services.
 *
 * @author Nicolas THIERION
 * @version 0.1.0
 */
public interface ICompanyDao {

    /**
     * List computers, oder by name.
     *
     * @param begin
     *            order of the computer from which the list begins. Starts from
     *            0.
     * @param nb
     *            number of computer to return.. nb=0 for unbound search.
     * @return the list of computers
     */
    List<Company> listByName(int begin, int nb);

    /**
     * List computers that match the given name.
     *
     * @param begin
     *            order of the computer from which the list begins. Starts from
     *            0.
     * @param nb
     *            number of computer to return. List all computers available if
     *            this number is negative.
     * @param case unsensitive name of computers to search for.
     * @return the list of computers
     */
    List<Company> listByName(int begin, int nb, String name);

    /**
     *
     * @return count of companies entries in database
     */
    int getCount();


}
