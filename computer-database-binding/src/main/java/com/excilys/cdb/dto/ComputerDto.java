package com.excilys.cdb.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Component;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.validator.DateFormat;

/**
 * DTO of Computer Object. Compliant with JavaBean standard.
 *
 * @author Nicolas THIERION
 * @version 0.2.0
 */
@Component
public class ComputerDto implements Serializable {


    /**
     *
     */
    private static final long serialVersionUID = -3220828765666291714L;

    /* ***
     * ATTRIBUTES
     */

    /** name of this computer. */
    @NotEmpty(message = "computer.error.name.empty")
    private String            name;
    /** manufacturer of this computer. */
    private CompanyDto        mCompanyDto;
    /** release date. */
    @DateFormat(acceptEmpty = true)
    private String            mReleaseDate;
    /** discontinuation date. */
    @DateFormat(acceptEmpty = true)
    private String            mDiscDate;
    /** id of this computer. */
    private Long              mId;

    /* ***
     * CONSTRUCTORS
     */
    /**
     * Default constructor. Initialize this DTO with all fields to null.
     */
    public ComputerDto() {
        mNewComputerDto(null, null, null, null, null);
    }

    /**
     * Create a new ComputerDto with given parameters.
     *
     * @param id
     * @param name
     * @param releaseDate
     * @param discontDate
     * @param company
     */
    private void mNewComputerDto(Long id, String name, String releaseDate, String discontDate, CompanyDto company) {
        mId = (id != null ? new Long(id) : null);
        this.name = name;
        mReleaseDate = releaseDate;
        mDiscDate = discontDate;
        mCompanyDto = company;
    }

    public ComputerDto(Long id, String name, String releaseDate, String discontDate) {
        mNewComputerDto(id, name, releaseDate, discontDate, null);
    }

    public ComputerDto(Long id, String name, String releaseDate, String discontDate, CompanyDto company) {
        mNewComputerDto(id, name, releaseDate, discontDate, company);
    }

    public ComputerDto(Long id, String name, String releaseDate, String discontDate, Long companyId) {
        CompanyDto companyDto = null;
        if (companyId != null) {
            final Company company = new Company();
            company.setId(companyId);
            companyDto = CompanyDto.fromCompany(company);
        }
        mNewComputerDto(id, name, releaseDate, discontDate, companyDto);
    }

    /**
     * Create a new Computer DTO corresponding to the given Computer Object.
     *
     * @param computer
     *            Computer to build the DTO. Can be null.
     * @return the created ComputerDto.
     */
    public static ComputerDto fromComputer(Computer computer) {
        final ComputerDto dto = new ComputerDto();

        if (computer == null) {
            dto.mNewComputerDto(null, null, null, null, null);
        } else {
            final Company company = computer.getCompany();
            final Long id = computer.getId();
            final String name = computer.getName();
            final String releaseDate = (computer.getIntroduced() != null ? computer.getIntroduced().toString() : "");
            final String discontDate = (computer.getDiscontinued() != null ? computer.getDiscontinued().toString() : "");
            final CompanyDto companyDto = (company != null ? CompanyDto.fromCompany(company) : null);
            dto.mNewComputerDto(id, name, releaseDate, discontDate, companyDto);
        }
        return dto;
    }

    /**
     * Create a list of Computer DTOs corresponding to the given Computer
     * Objects.
     *
     * @param computers
     *            Computers to create the DTOs.
     * @return List of computer DTOs.
     */
    public static List<ComputerDto> fromComputers(List<Computer> computers) {
        final List<ComputerDto> res = new LinkedList<ComputerDto>();
        for (final Computer computer : computers) {
            res.add(ComputerDto.fromComputer(computer));
        }
        return res;
    }

    /**
     * Copy constructor.
     *
     * @param computerDto
     *            ComputerDto Object to copy.
     */
    public ComputerDto(ComputerDto computerDto) {
        mNewComputerDto(computerDto.mId, computerDto.name, computerDto.mReleaseDate, computerDto.mDiscDate,
                computerDto.mCompanyDto);
    }


    /* ***
     * PUBLIC METHODS
     */
    public Computer toComputer() {

        final Computer computer = new Computer();
        final Company company = new Company();

        computer.setId(mId);
        if (this.name != null) {
            computer.setName(this.name);
        }
        if (mReleaseDate != null && !mReleaseDate.trim().isEmpty()) {
            computer.setIntroduced(LocalDate.parse(mReleaseDate));
        }
        if (mDiscDate != null && !mDiscDate.trim().isEmpty()) {
            computer.setDiscontinued(LocalDate.parse(mDiscDate));
        }
        if (mCompanyDto != null) {
            company.setId(mCompanyDto.getId());
            company.setName(mCompanyDto.getName());
            computer.setCompany(company);
        }
        return computer;
    }

    /* ***
     * GETTERS
     */

    public String getName() {
        return this.name;
    }

    public String getCompanyName() {
        return (mCompanyDto != null ? mCompanyDto.getName() : null);
    }

    public Long getCompanyId() {
        return (mCompanyDto != null ? mCompanyDto.getId() : null);
    }

    /**
     * Same as {@code #getIntroducedDate()}.
     *
     * @return
     */
    public String getReleaseDate() {
        return mReleaseDate;
    }

    public String getIntroducedDate() {
        return mReleaseDate;
    }


    public String getDiscontinuedDate() {
        return mDiscDate;
    }

    public Long getId() {
        return mId;
    }

    public CompanyDto getCompany() {
        return mCompanyDto;
    }

    /**
     * same as {@link #getCompany()}.
     *
     * @return
     */
    public CompanyDto getManufacturer() {
        return (mCompanyDto != null ? new CompanyDto(mCompanyDto) : null);
    }

    @SuppressWarnings("deprecation")
    public boolean isValid() {
        return this.name != null && !this.name.trim().isEmpty() && toComputer().isValid();
    }

    /* ***
     * SETTERS
     */
    public void setName(String name) {
        this.name = name;
    }

    public void setCompanyDto(CompanyDto companyDto) {
        mCompanyDto = companyDto;
    }

    /**
     * same as {@code #setCompany(CompanyDto)}.
     *
     * @param companyDto
     */
    public void setManufacturer(CompanyDto companyDto) {
        setCompanyDto(companyDto);
    }


    public void setReleaseDate(String releaseDate) {
        mReleaseDate = releaseDate;
    }

    /**
     * same as {@code #setReleaseDate(String)}.
     *
     * @param releaseDate
     */
    public void setIntroducedDate(String releaseDate) {
        mReleaseDate = releaseDate;
    }

    public void setDiscontinuedDate(String discDate) {
        mDiscDate = discDate;
    }

    public void setId(Long id) {
        mId = id;
    }

    /**
     * Same as {@code #setCompanyName(String)}.
     *
     * @param companyName
     */
    public void setCompany(String companyName) {
        setCompanyName(companyName);
    }

    /**
     * Same as {@code #setCompanyId(long)}.
     *
     * @param companyId
     */
    public void setCompany(Long companyId) {
        setCompanyId(companyId);
    }

    public void setCompanyName(String companyName) {
            if (mCompanyDto != null) {
            mCompanyDto.setName(companyName);
            } else {
            mCompanyDto = new CompanyDto(null, companyName);
            }
    }

    public void setCompanyId(Long companyId) {

        if (companyId == null) {
            mCompanyDto = null;
        } else if (mCompanyDto != null) {
            mCompanyDto.setId(companyId);
        } else {
            mCompanyDto = new CompanyDto(companyId, null);
        }
    }
    /* ***
     * Object OVERRIDES
     */

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mCompanyDto == null) ? 0 : mCompanyDto.hashCode());
        result = prime * result + ((mDiscDate == null) ? 0 : mDiscDate.hashCode());
        result = prime * result + ((mId == null) ? 0 : mId.hashCode());
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
        result = prime * result + ((mReleaseDate == null) ? 0 : mReleaseDate.hashCode());
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ComputerDto other = (ComputerDto) obj;
        if (mCompanyDto == null) {
            if (other.mCompanyDto != null) {
                return false;
            }
        } else if (!mCompanyDto.equals(other.mCompanyDto)) {
            return false;
        }
        if (mDiscDate == null) {
            if (other.mDiscDate != null) {
                return false;
            }
        } else if (!mDiscDate.equals(other.mDiscDate)) {
            return false;
        }
        if (mId == null) {
            if (other.mId != null) {
                return false;
            }
        } else if (!mId.equals(other.mId)) {
            return false;
        }
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (mReleaseDate == null) {
            if (other.mReleaseDate != null) {
                return false;
            }
        } else if (!mReleaseDate.equals(other.mReleaseDate)) {
            return false;
        }
        return true;
    }
}
