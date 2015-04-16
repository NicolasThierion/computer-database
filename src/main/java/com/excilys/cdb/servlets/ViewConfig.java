package com.excilys.cdb.servlets;


public final class ViewConfig {
    private ViewConfig() {
    }

    public static class AddComputer {
        public static final String MAPPING = "/addComputer";
        public static class Set {
            public static final String COMPANIES_PAGE_BEAN = "companiesPageBean";
        }
    }

    public static class SearchComputer extends Dashboard {
        public static final String MAPPING = "/searchComputer";
    }

    public static class Dashboard {
        public static final String MAPPING = "/dashboard";
        public static final int    DEFAULT_PAGE_SIZE = 10;

        /** input parameters. */
        public static final class Get extends GetParam {
            /** search parameter name. */
            public static final String SEARCH      = "search";
            /** page size parameter name. */
            public static final String PAGE_SIZE   = "size";
            /** search offset parameter name. */
            public static final String PAGE_OFFSET = "offset";
        }

        /** output parameters. */
        public static final class Set extends SetParam {
            /** Page attribute to be sent to JSP. */
            public static final String PAGE_BEAN = "pageBean";
        }

    }

    public abstract static class SetParam {
    }

    public abstract static class GetParam {
    }
}
