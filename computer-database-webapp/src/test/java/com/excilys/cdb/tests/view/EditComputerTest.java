package com.excilys.cdb.tests.view;

import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.LinkedList;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.excilys.cdb.dto.ComputerDto;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.service.ICompanyService;
import com.excilys.cdb.service.IComputerService;

/**
 * Test if editComputer Page loads correctly. Test from pre-filling.
 *
 * @author Nicolas THIERION.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext.xml"})
public final class EditComputerTest extends CdbViewTest {

    /* ***
     * TEST CONSTANTS & PARAMETERS
     */
    private static final String TEST_URI        = "editComputer?computerId=%d";

    static final int            COMPUTERS_TO_FETCH = 25;

    /* ***
     * FIELDS TO TEST
     */
    private static final String EDIT_MAIN_TITLE    = "Edit computer";
    private static final String PAGE_TITLE         = "Computer Database - " + EDIT_MAIN_TITLE;


    /* ***
     * ATTRIBUTES
     */
    /** WebDriver implementation used for this test cases. */
    private WebDriver mWebDriver;
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
     * test if a wrong url (ie : wrong computerId) leads to error page.
     */
    @Test
    public void testWrongUrl() {
        for (final Long currentId : new Long[] {-1L, Long.MAX_VALUE, 0L}) {
            final String uri = String.format(TEST_URI, currentId);
            super.setUri(uri).authAsAsmin();
            mUrl = super.getUrl();
            mWebDriver.get(mUrl);
            try {
                // DIRTY CHECKING, I know this is bad.... no matter :P
                final String mainTitle = mWebDriver.findElements(By.tagName("h1")).get(0).getText();
                assertTrue(!mainTitle.equals(EDIT_MAIN_TITLE));
            } catch (final Exception e) {
            }
        }
    }

    @Test
    public void testCompanyListFilled() {
        final String uri = String.format(TEST_URI, 1L);
        super.setUri(uri).authAsAsmin();
        mUrl = super.getUrl();
        mWebDriver.get(mUrl);

        // find form elements
        final WebElement form = mWebDriver.findElement(By.tagName("form"));
        final Select companySelect = new Select(form.findElement(By.id("companyId")));

        // list must be filled by as many companies as it exists, + 1 null
        // choice.
        assertTrue(companySelect.getOptions().size() == mCompanyService.getCount() + 1);
    }

    /**
     * Test if form is well filled with computers information.
     */
    @Test
    public void testFormPreFill() {

        //build a list of existing computer ids to construct valid urls.
        final List<Computer> computers = mComputerService.listByName(0, COMPUTERS_TO_FETCH);
        final List<Long> computerIds = new LinkedList<Long>();
        for (final Computer computer : computers) {
            computerIds.add(computer.getId());
        }

        final Iterator<Computer> computerIterator = computers.iterator();
        // test editComputer for each computerId
        for (final Long currentComputerId : computerIds) {
            final ComputerDto computer = ComputerDto.fromComputer(computerIterator.next());
            final String uri = String.format(TEST_URI, currentComputerId);
            super.setUri(uri).authAsAsmin();
            mUrl = super.getUrl();
            mWebDriver.get(mUrl);
            assertTrue(!mWebDriver.getCurrentUrl().equals("about:blank"));
            assertTrue(mWebDriver.getTitle().equals(PAGE_TITLE));
            final String mainTitle = mWebDriver.findElements(By.tagName("h1")).get(0).getText();
            assertTrue(mainTitle.equals(EDIT_MAIN_TITLE));

            // find form elements
            final WebElement form = mWebDriver.findElement(By.tagName("form"));
            final WebElement nameInput = form.findElement(By.id("computerName"));
            final WebElement releaseInput = form.findElement(By.id("introduced"));
            final WebElement discontinuedInput = form.findElement(By.id("discontinued"));
            final Select companySelect = new Select(form.findElement(By.id("companyId")));
            final WebElement companyInput = companySelect.getFirstSelectedOption();

            // check form values
            assertTrue(nameInput.getAttribute("value").equals(computer.getName()));
            if (computer.getReleaseDate() != null && !computer.getReleaseDate().trim().isEmpty()) {
                assertTrue(releaseInput.getAttribute("value").equals(computer.getReleaseDate()));
            }
            if (computer.getDiscontinuedDate() != null && !computer.getDiscontinuedDate().trim().isEmpty()) {
                assertTrue(discontinuedInput.getAttribute("value").equals(computer.getDiscontinuedDate()));
            }
            if (computer.getCompanyName() != null && !computer.getCompanyName().trim().isEmpty()) {
                assertTrue(companyInput.getText().equals(computer.getCompanyName()));
            }
        }
    }
}
