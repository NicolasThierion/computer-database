package com.excilys.cdb.tests.view;

public abstract class CdbViewTest {

    /** webserver parameters. */
    public static final String PROTOCOL = "http";
    public static final String HOST = "localhost";
    public static final int PORT = 8080;
    public static final String ROOT_URL = "computer-database";
    public static final String PAGE = "";
    /** Url of webPage to test. */
    private String             mUrl;

    public void setUri(String pageUrn) {
        final StringBuilder sb = new StringBuilder();
        sb.append(PROTOCOL).append("://").append(HOST).append(":").append(PORT);
        sb.append("/").append(ROOT_URL);
        if (!pageUrn.startsWith("/")) {
            sb.append("/");
        }
        sb.append(pageUrn);
        mUrl = sb.toString();
    }

    public String getUrl() {
        return mUrl;
    }

}
