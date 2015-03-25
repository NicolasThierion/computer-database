package com.excilys.cdb.service;

import java.util.List;

import com.excilys.cdb.model.Company;

/**
 *
 * @author Nicolas THIERION
 * @version 0.2.0 TODO doc.
 */
public interface ICompanyService {

    /* ***
     * COMPANIES METHODS
     */

    List<Company> listByName(int begin, int nb);

    int getCount();

    int getCount(String name);

    Company retrieve(long companyId);

    Company search(long companyId);

    List<Company> listByName();

}
