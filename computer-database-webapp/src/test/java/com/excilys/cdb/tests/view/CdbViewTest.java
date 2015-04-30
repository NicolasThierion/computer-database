package com.excilys.cdb.tests.view;

public abstract class CdbViewTest {

    /** webserver parameters. */
    public static final String PROTOCOL = "http";
    public static final String HOST = "localhost";
    public static final int PORT = 8080;
    public static final String ROOT_URL = "computer-database";
    public static final String PAGE = "";


    public static final String ADMIN_USERNAME = "admin";
    public static final String ADMIN_PASSWORD = "admin";

    public static final String USER_USERNAME  = "user";
    public static final String USER_PASSWORD  = "user";

    /** Url of webPage to test. */
    private String              mUrn;
    private String             mUsername      = "";
    private String             mPassword      = "";

    public CdbViewTest setUri(String pageUrn) {
        mUrn = pageUrn;
        return this;
    }

    public CdbViewTest authAsAsmin() {
        mUsername = ADMIN_USERNAME;
        mPassword = ADMIN_PASSWORD;
        return this;
    }

    public CdbViewTest authAsUser() {
        mUsername = USER_USERNAME;
        mPassword = USER_PASSWORD;
        return this;
    }

    public CdbViewTest authAsAnonymous() {
        mUsername = "";
        mPassword = "";
        return this;
    }

    public String getUrl() {

        if (mUsername.isEmpty()) {
            return mUrlWithoutAuth();
        } else {
            return mUrlWithAuth();
        }
    }

    private String mUrlWithoutAuth() {

        final StringBuilder sb = new StringBuilder();
        sb.append(PROTOCOL).append("://").append(HOST).append(":").append(PORT);
        sb.append("/").append(ROOT_URL);
        if (!mUrn.startsWith("/")) {
            sb.append("/");
        }
        sb.append(mUrn);
        final String url = sb.toString();
        return url;
    }

    private String mUrlWithAuth() {

        final StringBuilder sb = new StringBuilder();
        sb.append(PROTOCOL).append("://").append(mUsername).append(":").append(mPassword);
        sb.append("@").append(HOST).append(":").append(PORT);
        sb.append("/").append(ROOT_URL);
        if (!mUrn.startsWith("/")) {
            sb.append("/");
        }
        sb.append(mUrn);
        final String url = sb.toString();
        return url;
    }

}
