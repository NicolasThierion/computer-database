package com.excilys.cdb.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Nicolas THIERION
 *
 *         TODO JavaDoc.
 * @param <T>
 */
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
     *
     */
    private static final long serialVersionUID = -7930434399646098394L;

    public static final SortOrder DEFAULT_SORT_ORDER = SortOrder.UNSORTED;
    /* ***
     * ATTRIBUTES
     */
    /** entities contained by this page. */
    private List<T>           mContent;
    /** optional query string. */
    private String            mSearch;
    /** start element. */
    private int               mOffset;
    /** page length. */
    private int               mSize;
    /** page number. */
    private int               mPageNum;
    /** maximum results. */
    private int               mMaxResults;
    /** sort criteria. */
    private String            mSortBy;
    /** sort order. */
    private SortOrder         mSortOrder;

    /* ***
     * CONSTRUCTORS
     */
    private void mNewPage(List<T> content, int offset,
 int maxResults, String queryString, String sortBy, SortOrder order) {
        if (content != null && maxResults < content.size()) {
            throw new IllegalArgumentException("Content has more element than the provided \"maxResults\" parameter");
        }

        mOffset = offset;
        mMaxResults = maxResults;
        mSearch = queryString;
        mSortBy = sortBy;
        mSortOrder = order;
        if (content != null && !content.isEmpty()) {
            mContent = new LinkedList<T>(content);
            mSize = content.size();
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
        mNewPage(null, 0, 0, null, null, DEFAULT_SORT_ORDER);
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
        mNewPage(content, offset, maxResults, queryString, null, DEFAULT_SORT_ORDER);
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
        mNewPage(content, offset, maxResults, null, null, DEFAULT_SORT_ORDER);
    }

    /**
     * Copy constructor.
     *
     * @param page
     *            Page to copy.
     */
    public Page(Page<T> page) {
        final Page<T> p = page;
        mNewPage(p.mContent, p.mOffset, p.mMaxResults, p.mSearch, p.mSortBy, p.mSortOrder);
    }

    /**
     * Constructor with arguments. Create a new page with given content,
     * starting at offset 0, with & a null queryString.
     *
     * @param content
     *            content of the page.
     */
    public Page(List<T> companies) {
        mNewPage(companies, 0, companies.size(), null, null, DEFAULT_SORT_ORDER);
    }

    /* ***
     * PUBLIC METHODS
     */
    public String toUrlArgs() {
        final StringBuilder sb = new StringBuilder();
        sb.append("offset=").append(mOffset).append("&maxResults=").append(mMaxResults);
        sb.append("&search=").append(mSearch).append("&sortBy=").append(mSortBy);
        sb.append("&order=").append(mSortOrder).append("&size=").append(mSize);
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
        return (int) Math.ceil((double) (mMaxResults) / ((double) mSize));
    }

    /* ***
     * SETTERS
     */

    public void setContent(List<T> content) {
        mNewPage(content, mOffset, mMaxResults, mSearch, mSortBy, mSortOrder);
    }

    public void setSearch(String queryString) {
        mSearch = queryString;
    }

    public void setOffset(int offset) {
        mNewPage(mContent, offset, mMaxResults, mSearch, mSortBy, mSortOrder);
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


    /* ***
     * Object OVERRIDES
     */

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((mContent == null) ? 0 : mContent.hashCode());
        result = prime * result + mSize;
        result = prime * result + mMaxResults;
        result = prime * result + mOffset;
        result = prime * result + mPageNum;
        result = prime * result
                + ((mSearch == null) ? 0 : mSearch.hashCode());
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

        if (!(obj instanceof Page<?>)) {
            return false;
        }
        final Page<?> other = (Page<?>) obj;
        if (mContent == null) {
            if (other.mContent != null) {
                return false;
            }
        } else if (!mContent.equals(other.mContent)) {
            return false;
        }
        if (mSize != other.mSize) {
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
        return true;
    }

    @Override
    public String toString() {

        final StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName()).append("@").append(hashCode());
        sb.append("; PageNum=").append(mPageNum).append("; PageLength=").append(mSize);
        sb.append("; Offset=").append(mOffset).append("; Query=").append(mSearch);
        return sb.toString();
    }

}
