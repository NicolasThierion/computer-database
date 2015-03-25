package com.excilys.cdb.tests.view;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * Test if homepage is reachable from any page of the website.
 *
 * @author Nicolas THIERION.
 *
 */
public final class HomeRedirectTest extends CdbViewTest {

    /* ***
     * TEST CONSTANTS & PARAMETERS
     */
    private static final String[] TEST_URI       = {"dashboard", "no_such_url", "editComputer"};

    /* ***
     * FIELDS TO TEST
     */
    private static final String   PAGE_TITLE      = "Computer Database";

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
    }

    /**
     * Destroy webDriver.
     */
    @After
    public void destroy() {
        mWebDriver.quit();
    }

    @Test
    public void testRedirect() {

        for (final String uri : TEST_URI) {
            super.setUri(uri);
            mUrl = super.getUrl();
            mWebDriver.get(mUrl);
            assertTrue(!mWebDriver.getCurrentUrl().equals("about:blank"));
            assertTrue(mWebDriver.getTitle().equals(PAGE_TITLE));

            // find "search computer" input.
            final WebElement homeLink = mWebDriver.findElements(By.tagName("a")).get(0);
            final String href = homeLink.getAttribute("href");

            super.setUri(uri);
            mWebDriver.get(href);
            assertTrue(mWebDriver.getTitle().equals(PAGE_TITLE));
            final String searchTitle = mWebDriver.findElement(By.id(SEARCH_TITLE_ID)).getText();
            assertTrue(searchTitle != null && !searchTitle.isEmpty());
        }

    }

}
