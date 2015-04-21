package com.excilys.cdb.servlet;


/**
 *
 * @author Nicolas THIERION. TODO doc.
 *
 */
public final class ViewConfig {

    private ViewConfig() {
    }

    public static class AddComputer {
        public static final String MAPPING = "/addComputer";

        public static class Get extends GetParam {
            /** Computer dto to sent by the JSP. */
            public static final String COMPUTER_DTO = "computerDto";
        }

        /** output parameters. */
        public static class Set extends SetParam {
            /** company list for filling <select> in form. */
            public static final String COMPANY_DTO_LIST = "companyDtoList";
            /** Computer dto to be sent to JSP. */
            public static final String COMPUTER_DTO     = EditComputer.Get.COMPUTER_DTO;
        }

    }

    public static class EditComputer extends AddComputer {
        public static final String MAPPING = "/editComputer";

        /** input parameters. sent by JSP. */
        public static final class Get extends AddComputer.Get {
            /** Id of computer to edit. */
            public static final String COMPUTER_ID      = "computerId";
            /** name of computer to update. */
            public static final String COMPUTER_NAME    = "computerName";
            /** release date of computer to update. */
            public static final String COMPUTER_RELEASE = "introduced";
            /** discontinuation date of computer to update. */
            public static final String COMPUTER_DISCONT = "discontinued";
            /** company id of computer to update. */
            public static final String COMPANY_ID       = "companyId";
        }

        public static class Set extends AddComputer.Set {
        }


    }

    public static class SearchComputer extends Dashboard {
        public static final String MAPPING = "/searchComputer";
    }

    public static class DeleteComputer {
        public static final String MAPPING = "/deleteComputer";

        /** input parameters. sent by JSP. */
        public static final class Get extends GetParam {
            /** Id of computer to edit. */
            public static final String COMPUTER_IDS = "selection";
        }
    }

    public static class Dashboard {
        public static final String MAPPING = "/dashboard";
        public static final int    DEFAULT_PAGE_SIZE = 10;

        /** input parameters. */
        public static final class Get extends GetParam {
            /** Page progerties (search parameter, page size & offset). */
            public static final String PAGE_BEAN = "pageBean";
        }

        /** output parameters. */
        public static final class Set extends SetParam {
            /** Page attribute to be sent to JSP. */
            public static final String PAGE_BEAN = Get.PAGE_BEAN;
        }

    }

    public abstract static class SetParam {
    }

    public abstract static class GetParam {
    }
}
