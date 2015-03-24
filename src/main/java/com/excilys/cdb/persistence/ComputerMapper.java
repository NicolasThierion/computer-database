package com.excilys.cdb.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;

/**
 * TODO doc.
 *
 * @author Nicolas THIERION.
 *
 */
public class ComputerMapper implements EntityMapper<Computer> {

    /* ***
     * ATTRIBUTES
     */

    /** name of this computer. */
    private String    mName;
    /** manufacturer of this computer. */
    private Company       mCompany;
    /** release date. */
    private LocalDate mReleaseDate;
    /** discontinuation date. */
    private LocalDate mDiscDate;
    /** id of this computer. Remember to test if not null!!!! */
    private Long          mId;
    /** SQL release timestamp to be stored in DB. */
    private Timestamp     mSqlReleaseDate;
    /** SQL discontinuation timestamp to be stored in DB. */
    private Timestamp     mSqlDiscDate;
    /** remember to test if not null!!!! */
    private Long          mCompanyId;
    private String        mCompanyName;

    /* ***
     * CONSTRUCTORS
     */

    @Override
    public void fromEntity(Computer computer) {
        mId = computer.getId();
        mName = computer.getName();
        mReleaseDate = computer.getReleaseDate();
        mDiscDate = computer.getDiscontDate();
        mCompany = computer.getCompany();
        if (mCompany != null) {
            mCompanyId = computer.getCompany().getId();
            mCompanyName = mCompany.getName();
        }

        // convert java LocalDate to java LocalDateTime to sql Timestamp,
        // ensuring date is not null
        mSqlReleaseDate = (mReleaseDate != null ? Timestamp.valueOf(mReleaseDate.atTime(LocalTime.of(0, 0))) : null);
        mSqlDiscDate = (mDiscDate != null ? Timestamp.valueOf(mDiscDate.atTime(LocalTime.of(0, 0))) : null);
    }

    @Override
    public Computer fromResultSet(ResultSet res) throws SQLException {

        // TODO use named columns
        int colId = 1;
        mId = res.getLong(colId++);
        mName = res.getString(colId++);

        mSqlReleaseDate = res.getTimestamp(colId++);
        mSqlDiscDate = res.getTimestamp(colId++);

        // convert sql.Date to java.util.Date, asserting date is not null.
        mReleaseDate = (mSqlReleaseDate != null ? LocalDate.from(mSqlReleaseDate.toLocalDateTime()) : null);
        mDiscDate = (mSqlDiscDate != null ? LocalDate.from(mSqlDiscDate.toLocalDateTime()) : null);

        // get company name;
        mCompanyId = res.getLong(colId++);
        mCompanyName = res.getString(colId++);

        return new Computer(mId, mName, new Company(mCompanyId, mCompanyName), mReleaseDate, mDiscDate);
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

    public LocalDate getReleaseDate() {
        return mReleaseDate;
    }

    public LocalDate getDiscDate() {
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
