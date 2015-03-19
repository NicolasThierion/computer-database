package com.excilys.cdb.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;

public class ComputerMapper {

    /** name of this computer. */
    private String        mName;
    /** manufacturer of this computer. */
    private Company       mCompany;
    /** release date. */
    private LocalDateTime mReleaseDate;
    /** discontinuation date. */
    private LocalDateTime mDiscDate;
    /** id of this computer. Remember to test if not null!!!! */
    private Long          mId;

    private Timestamp     mSqlReleaseDate;
    private Timestamp     mSqlDiscDate;
    /** remember to test if not null!!!! */
    private Long          mCompanyId;
    private String        mCompanyName;


    public void fromComputer(Computer computer) {
        mId = computer.getId();
        mName = computer.getName();
        mReleaseDate = computer.getIntroDate();
        mDiscDate = computer.getDiscontDate();
        mCompany = computer.getCompany();
        if (mCompany != null) {
            mCompanyId = computer.getCompany().getId();
            mCompanyName = mCompany.getName();
        }


        // convert java Date to sql Timestamp, ensuring date is not null
        mSqlReleaseDate = (mReleaseDate != null ? Timestamp.valueOf(mReleaseDate) : null);
        mSqlDiscDate = (mDiscDate != null ? Timestamp.valueOf(mDiscDate) : null);
    }

    public void fromResultSet(ResultSet res) throws SQLException {

        int colId = 1;
        mId = res.getLong(colId++);
        mName = res.getString(colId++);

        mSqlReleaseDate = res.getTimestamp(colId++);
        mSqlDiscDate = res.getTimestamp(colId++);

        // convert sql.Date to java.util.Date, asserting date is not null.
        mReleaseDate = (mSqlReleaseDate != null ? mSqlReleaseDate.toLocalDateTime() : null);
        mDiscDate = (mSqlDiscDate != null ? mSqlDiscDate.toLocalDateTime() : null);

        // get company name;
        mCompanyId = res.getLong(colId++);
        mCompanyName = res.getString(colId++);
    }

    /* ***
     * ACCESSORS
     */

    public String getName() {
        return mName;
    }

    public Company getCompany() {
        return mCompany;
    }

    public LocalDateTime getReleaseDate() {
        return mReleaseDate;
    }

    public LocalDateTime getDiscDate() {
        return mDiscDate;
    }

    public Long getId() {
        return mId;
    }

    public Timestamp getSqlReleaseDate() {
        return mSqlReleaseDate;
    }

    public Timestamp getSqlDiscDate() {
        return mSqlDiscDate;
    }

    public Long getCompanyId() {
        return mCompanyId;
    }

    public String getCompanyName() {
        return mCompanyName;
    }
}
