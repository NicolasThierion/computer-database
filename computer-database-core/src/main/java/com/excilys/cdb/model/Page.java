package com.excilys.cdb.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;


/**
 *
 * @author Nicolas THIERION Generic purpose search page, aimed to store search
 *         results according some search & display parameters.
 * @param <T>
 */
@Component
public class Page<T> implements Serializable {

    public enum SortOrder {
        DESC("desc"), ASC("asc"), UNSORTED("none");
        private String mLabel;

        private SortOrder(String label) {
            mLabel = label;
        }

        @Override
        public String toString() {
            return mLabel;
        }
    }

    /**
     * Page fields. Fields can used to identify page parameters, & can be passed
     * from one object to another through a string.
     *
     * @author Nicolas THIERION.
     *
     */
    public enum Field implements EntityField<Page<?>> {
        SEARCH("search"), OFFSET("offset"), SIZE("size"), SORT_BY("sort_by"), SORT_ORDER("sort_order"),
        TOTAL("total"), OPTIONS("options");

        public final String label;

        private Field(String pLabel) {
            label = pLabel;
        }

        @Override
        public String getLabel() {
            return label;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    /**
     *
     */
    private static final long serialVersionUID = -7930434399646098394L;

    public static final SortOrder DEFAULT_SORT_ORDER = SortOrder.UNSORTED;
    /* ***
     * ATTRIBUTES
     */
    /** entities contained by this page. */
    private List<T>               mContent;
    /** optional query string. */
    private String                mSearch;
    /** start element. */
    private int                   mOffset;
    /** page length. */
    private int                   mSize;
    /** page number. */
    private int                   mPageNum;
    /** maximum results. */
    private int                   mMaxResults;
    /** sort criteria. */
    private String                mSortBy;
    /** sort order. */
    private SortOrder             mSortOrder;

    private Map<String, String>   mOptions;

    /* ***
     * CONSTRUCTORS
     */
    private void mNewPage(List<T> content, int offset, int size,
 int maxResults, String queryString, String sortBy, SortOrder order) {
        if (content != null && maxResults < content.size()) {
            throw new IllegalArgumentException("Content has more element than the provided \"maxResults\" parameter");
        }

        mOffset = offset;
        mMaxResults = maxResults;
        mSearch = queryString;
        mSortBy = sortBy;
        mSortOrder = order;
        mOptions = new HashMap<String, String>();
        if (content != null && !content.isEmpty()) {
            mContent = new LinkedList<T>(content);
            mSize = (size < 0 ? content.size() : size);
            mPageNum = (mOffset / mSize) + 1;
        } else {
            mContent = null;
            mSize = 0;
            mPageNum = 0;
        }

    }

    /**
     * Default constructor. Create a new empty page, with null content, null
     * query.
     */
    public Page() {
        mNewPage(null, 0, 0, -1, null, null, DEFAULT_SORT_ORDER);
    }

    /**
     * Constructor with arguments.
     * Create a new page with given content, starting at the given offset.
     *
     * @param content
     *            content of the page.
     * @param offset
     *            search offset
     * @param maxResults
     *            count of total results.
     * @param queryString
     *            query string that generated this content.
     */
    public Page(List<T> content, int offset, int maxResults,
            String queryString) {
        mNewPage(content, offset, -1, maxResults, queryString, null, DEFAULT_SORT_ORDER);
    }

    /**
     * Constructor with arguments. Create a new page with given content,
     * starting at the given offset, & a null queryString.
     *
     * @param content
     *            content of the page.
     * @param offset
     *            search offset
     * @param maxResults
     *            count of total results.
     */
    public Page(List<T> content, int offset, int maxResults) {
        mNewPage(content, offset, -1, maxResults, null, null, DEFAULT_SORT_ORDER);
    }

    /**
     * Copy constructor.
     *
     * @param page
     *            Page to copy.
     */
    public Page(Page<T> page) {
        final Page<T> p = page;
        mNewPage(p.mContent, p.mOffset, p.mSize, p.mMaxResults, p.mSearch, p.mSortBy, p.mSortOrder);
    }

    /**
     * Constructor with arguments. Create a new page with given content,
     * starting at offset 0, with & a null queryString.
     *
     * @param content
     *            content of the page.
     */
    public Page(List<T> content) {
        mNewPage(content, 0, -1, content.size(), null, null, DEFAULT_SORT_ORDER);
    }

    /* ***
     * PUBLIC METHODS
     */
    public String toUrlArgs() {
        final StringBuilder sb = new StringBuilder();
        sb.append(Field.OFFSET).append("=").append(mOffset);
        sb.append("&").append(Field.TOTAL).append("=").append(mMaxResults);
        sb.append("&").append(Field.SEARCH).append("=").append(mSearch);
        sb.append("&").append(Field.SORT_BY).append("=").append(mSortBy);
        sb.append("&").append(Field.SORT_ORDER).append("=").append(mSortOrder);
        sb.append("&").append(Field.SIZE).append("=").append(mSize);
        sb.append("&").append(Field.OPTIONS).append("=").append(mOptions);
        return sb.toString();
    }

    /* ***
     * GETTERS
     */
    public List<T> getContent() {
        return mContent;
    }

    public String getSearch() {
        return mSearch;
    }

    public int getOffset() {
        return mOffset;
    }

    public int getSize() {
        return mSize;
    }

    public String getSortBy() {
        return mSortBy;
    }

    public SortOrder getSortOrder() {
        return mSortOrder;
    }

    public int getNum() {
        return mPageNum;
    }

    public int getTotalCount() {
        return mMaxResults;
    }

    public int getMaxNum() {
        return (int) Math.ceil((double) (mMaxResults) / ((double) mSize)) - 1;
    }

    public Map<String, String> getOptions() {
        return mOptions;
    }

    public String getOption(String option) {
        return (mOptions.keySet().contains(option) ? mOptions.get(option) : "false");
    }

    /* ***
     * SETTERS
     */

    public void setContent(List<T> content) {
        mNewPage(content, mOffset, mSize, mMaxResults, mSearch, mSortBy, mSortOrder);
    }

    public void setSearch(String queryString) {
        mSearch = queryString;
    }

    public void setOffset(int offset) {
        mNewPage(mContent, offset, mSize, mMaxResults, mSearch, mSortBy, mSortOrder);
    }

    /**
     * Same as {@link #setSort(String, SortOrder)} with
     * {@link #setSortOrder(SortOrder)}.
     *
     * @param sortBy
     * @param order
     */
    public void setSort(String sortBy, SortOrder order) {
        mSortBy = sortBy;
        mSortOrder = order;
    }

    public void setSortBy(String sortBy) {
        mSortBy = sortBy;
    }

    public void setSortOrder(SortOrder order) {
        mSortOrder = order;
    }

    public void setTotalCount(int total) {
        mMaxResults = total;
    }

    public void setNum(int num) {
        mPageNum = num;
    }

    public void setSize(int size) {
        mSize = size;
    }

    public void setOption(String option, String value) {
        mOptions.put(option, value);
    }

    public void setOption(String option) {
        mOptions.put(option, "true");
    }

    /* ***
     * Object OVERRIDES
     */

    @Override
    public String toString() {

        final StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName()).append("@").append(hashCode());
        sb.append("; PageNum=").append(mPageNum).append("; PageLength=").append(mSize);
        sb.append("; Offset=").append(mOffset).append("; Query=").append(mSearch);
        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mContent == null) ? 0 : mContent.hashCode());
        result = prime * result + ((mOptions == null) ? 0 : mOptions.hashCode());
        result = prime * result + mMaxResults;
        result = prime * result + mOffset;
        result = prime * result + mPageNum;
        result = prime * result + ((mSearch == null) ? 0 : mSearch.hashCode());
        result = prime * result + mSize;
        result = prime * result + ((mSortBy == null) ? 0 : mSortBy.hashCode());
        result = prime * result + ((mSortOrder == null) ? 0 : mSortOrder.hashCode());
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
        @SuppressWarnings("unchecked")
        final Page<T> other = (Page<T>) obj;
        if (mContent == null) {
            if (other.mContent != null) {
                return false;
            }
        } else if (!mContent.equals(other.mContent)) {
            return false;
        }
        if (mOptions == null) {
            if (other.mOptions != null) {
                return false;
            }
        } else if (!mOptions.equals(other.mOptions)) {
            return false;
        }

        if (mMaxResults != other.mMaxResults) {
            return false;
        }
        if (mOffset != other.mOffset) {
            return false;
        }
        if (mPageNum != other.mPageNum) {
            return false;
        }
        if (mSearch == null) {
            if (other.mSearch != null) {
                return false;
            }
        } else if (!mSearch.equals(other.mSearch)) {
            return false;
        }
        if (mSize != other.mSize) {
            return false;
        }
        if (mSortBy == null) {
            if (other.mSortBy != null) {
                return false;
            }
        } else if (!mSortBy.equals(other.mSortBy)) {
            return false;
        }
        if (mSortOrder != other.mSortOrder) {
            return false;
        }
        return true;
    }


}
