package com.excilys.cdb.tests.view;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.excilys.cdb.dto.ComputerDto;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.dao.mysql.ComputerDao;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.service.IComputerService;
import com.excilys.cdb.servlets.ViewConfig;

/**
 * Test if addComputer Page loads correctly.
 *
 * @author Nicolas THIERION.
 *
 */
public final class DeleteComputerTest extends CdbViewTest {

    /* ***
     * TEST CONSTANTS & PARAMETERS
     */
    private static final String TEST_URI       = ViewConfig.Dashboard.MAPPING;

    /* ***
     * FIELDS TO TEST
     */
    private static final String SEARCH_INPUT_ID       = "searchbox";
    private static final String SEARCH_TITLE_ID       = "homeTitle";
    private static final String SUBMIT_DELETE_FORM_ID = "deleteSelected";
    private static final String CHECKBOX_CLASSNAME    = "cb";
    private static final String EDIT_BUTTON_ID        = "editComputer";

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
    private IComputerService    mComputerService;

    /**
     * Init webDriver, DAOs & Service.
     */
    @Before
    public void init() {
        mWebDriver = new FirefoxDriver();
        mComputerService = new ComputerService(ComputerDao.getInstance());

        super.setUri(TEST_URI);
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
    }


    @Test
    public void testDelComputer() {


        // insert a new computer
        final String computerName = "TestComputerName" + java.time.Clock.systemUTC().millis();
        final String rd = "2010-01-01";
        final String dd = "2011-01-01";
        final int initialComputerCount = mComputerService.getCount(computerName);

        // add one computer
        final Computer computer = new ComputerDto(null, computerName, rd, dd).toComputer();
        mComputerService.add(computer);
        assertTrue(computer.getId() != null);
        assertTrue(initialComputerCount + 1 == mComputerService.getCount(computerName));

        // search inserted computer...
        // find "search computer" input.
        final WebElement searchBox = mWebDriver.findElement(By.id(SEARCH_INPUT_ID));
        searchBox.sendKeys(computerName);
        searchBox.submit();

        // ensure title displays the right count of results.
        final String searchTitle = mWebDriver.findElement(By.id(SEARCH_TITLE_ID)).getText();
        assertTrue(searchTitle.startsWith("" + mComputerService.getCount(computerName)));

        // find form elements
        final List<WebElement> inputs = mWebDriver.findElements(By.tagName("input"));

        // find checkbox corresponding to this computer.
        WebElement checkBox = null;
        for (final WebElement input : inputs) {

            final String atClass = (input.getAttribute("class") != null ? input.getAttribute("class") : "");
            final String atType = (input.getAttribute("type") != null ? input.getAttribute("type") : "");
            final String atValue = (input.getAttribute("value") != null ? input.getAttribute("value") : "");

            if (atClass.equals(CHECKBOX_CLASSNAME) && atType.equals("checkbox")
                    && atValue.equals("" + computer.getId())) {
                checkBox = input;
                break;
            }
        }
        assertTrue(checkBox != null);
        //enable edit mode
        final WebElement editButton = mWebDriver.findElement(By.id(EDIT_BUTTON_ID));
        editButton.click();

        // select computer for deletion
        checkBox.click();

        // send form to delete computer.
        final WebElement submitForm = mWebDriver.findElement(By.id(SUBMIT_DELETE_FORM_ID));
        submitForm.click();
        // confirm alert dialog
        mWebDriver.switchTo().alert().accept();

        // assert computer has been deleted.:
        assertTrue(initialComputerCount == mComputerService.getCount(computerName));

    }
}
