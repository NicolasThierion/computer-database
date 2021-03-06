package com.excilys.cdb.tests.view;

import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.excilys.cdb.dto.ComputerDto;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.service.ICompanyService;
import com.excilys.cdb.service.IComputerService;

/**
 * Test if addComputer Page loads correctly.
 *
 * @author Nicolas THIERION.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext.xml"})
public final class AddComputerTest extends CdbViewTest {

    private static final Logger LOG            = LoggerFactory.getLogger(AddComputerTest.class);
    /* ***
     * TEST CONSTANTS & PARAMETERS
     */
    private static final String TEST_URI       = "addComputer";

    /* ***
     * FIELDS TO TEST
     */
    private static final String ADD_MAIN_TITLE = "Add computer";
    private static final String PAGE_TITLE     = "Computer Database - " + ADD_MAIN_TITLE;

    /* ***
     * ATTRIBUTES
     */
    /** WebDriver implementation used for this test cases. */
    private WebDriver           mWebDriver;
    /** current page's url. */
    private String              mUrl;
    /**
     * Computer & Company service used for fetching entities in this test suite.
     */
    @Autowired
    private IComputerService    mComputerService;
    @Autowired
    private ICompanyService     mCompanyService;

    /**
     * Init webDriver, DAOs & Service.
     */
    @Before
    public void init() {
        mWebDriver = new FirefoxDriver();
        super.setUri(TEST_URI).authAsAsmin();
        mUrl = super.getUrl();
        mWebDriver.get(mUrl);
    }

    /**
     * Destroy webDriver.
     */
    @After
    public void destroy() {
        mWebDriver.quit();
        mComputerService = null;
        mCompanyService = null;
    }

    /**
     * test if adding wrong values fails.
     */
    @Test
    // @Ignore("not working cause javascript desactivated")
    public void testAddWrongComputer() {

        for (final String name : new String[] {"", "?"}) {
            assertTrue(!mAddComputer(name, "", "", 1L));
        }

        final String name = "Valid name";
        for (final String rd : new String[] {"azerty", "40-40-4000"}) {
            assertTrue(!mAddComputer(name, rd, "", 1L));
        }

    }

    /**
     * Test valid computers creation.
     */
    @Test
    public void testAddComputer() {

        final String name = "TestComputerName" + java.time.Clock.systemUTC().millis();
        for (final String rd : new String[] {"", "2010-01-01"}) {
            for (final String dd : new String[] {"", "2011-01-01"}) {
                for (final Long compId : new Long[] {1L, null}) {
                    assertTrue(mAddComputer(name, rd, dd, compId));
                }
            }
        }
    }

    @Test
    public void testCompanyListFilled() {
        // find form elements
        final WebElement form = mWebDriver.findElement(By.tagName("form"));
        final Select companySelect = new Select(form.findElement(By.id("companyId")));

        // list must be filled by as many companies as it exists, + 1 null
        // choice.
        assertTrue(companySelect.getOptions().size() == mCompanyService.getCount() + 1);
    }

    private boolean mAddComputer(String computerName, String computerRelease, String computerDiscont, Long companyId) {

        super.setUri(TEST_URI);
        mUrl = super.getUrl();
        mWebDriver.get(mUrl);

        final int count = mComputerService.listLikeName(computerName).size();
        assertTrue(!mWebDriver.getCurrentUrl().equals("about:blank"));
        assertTrue(mWebDriver.getTitle().equals(PAGE_TITLE));
        final String mainTitle = mWebDriver.findElements(By.tagName("h1")).get(0).getText();
        assertTrue(mainTitle.equals(ADD_MAIN_TITLE));

        // find form elements
        final WebElement form = mWebDriver.findElement(By.tagName("form"));
        final WebElement nameInput = form.findElement(By.id("computerName"));
        final WebElement releaseInput = form.findElement(By.id("introduced"));
        final WebElement discontinuedInput = form.findElement(By.id("discontinued"));
        final Select companySelect = new Select(form.findElement(By.id("companyId")));

        // fill form.
        nameInput.sendKeys(computerName);
        releaseInput.sendKeys(computerRelease);
        discontinuedInput.sendKeys(computerDiscont);
        companySelect.selectByValue("" + (companyId != null ? companyId : ""));

        // submit form
        form.submit();
        final List<Computer> computers = mComputerService.listLikeName(computerName);
        //assert computer has been added
        if (computers.size() != count + 1) {
            LOG.error("Computer not added");
            return false;
        }

        //get last inserted computer
        final Comparator<Computer> computerComparatorById = new Comparator<Computer>() {
            @Override
            public int compare(Computer o1, Computer o2) {
                return o1.getId().compareTo(o2.getId());
            }
        };
        final Computer computer = Collections.max(computers, computerComparatorById);
        final ComputerDto dto = ComputerDto.fromComputer(computer);

        boolean res = (companyId == null && dto.getCompanyId() == null || dto.getCompanyId().equals(companyId));
        res = res && (dto.getName().equals(computerName));
        res = res && (dto.getReleaseDate().equals(computerRelease));
        res = res && (dto.getDiscontinuedDate().equals(computerDiscont));
        return res;
    }
}
