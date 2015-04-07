package com.excilys.cdb.tests.view;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.excilys.cdb.persistence.dao.mysql.ComputerDao;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.service.ICrudService;

public final class DashboardTest extends CdbViewTest {

    /* ***
     * TEST CONSTANTS & PARAMETERS
     */
    private static final String TEST_URI = "dashboard";

    /* ***
     * ATTRIBUTES
     */
    private ICrudService    mService;

    /* ***
     * FIELDS TO TEST
     */
    private static final String PAGE_TITLE      = "Computer Database";

    private static final String SEARCH_INPUT_ID = "searchbox";

    private static final String SEARCH_TITLE_ID = "homeTitle";
    /** WebDriver implementation used for this test cases. */
    private WebDriver mWebDriver;

    private String              mUrl;

    /**
     * Init webDriver, DAOs & Service.
     */
    @Before
    public void init() {
        mWebDriver = new HtmlUnitDriver();
        super.setUri(TEST_URI);
        mUrl = super.getUrl();
        mService = new ComputerService(ComputerDao.getInstance());
    }

    /**
     * Destroy webDriver.
     */
    @After
    public void destroy() {
        mWebDriver.quit();
    }

    @Test
    public void testDashboard() {
        mWebDriver.get(mUrl);
        assertTrue(!mWebDriver.getCurrentUrl().equals("about:blank"));
        assertTrue(mWebDriver.getTitle().equals(PAGE_TITLE));
    }

    /**
     * Ensure web page displays the correct count of search results.
     */
    @Test
    public void testSearch() {
        for (final String searchWords : new String[] {"bOoK", "", "a", "Nothing"}) {
            mWebDriver.get(mUrl);

            // find "search computer" input.
            final WebElement searchBox = mWebDriver.findElement(By.id(SEARCH_INPUT_ID));
            searchBox.sendKeys(searchWords);
            searchBox.submit();

            // ensure title displays the right count of results.
            final String searchTitle = mWebDriver.findElement(By.id(SEARCH_TITLE_ID)).getText();
            assertTrue(searchTitle.startsWith("" + mService.getCount(searchWords)));
        }
    }
}
