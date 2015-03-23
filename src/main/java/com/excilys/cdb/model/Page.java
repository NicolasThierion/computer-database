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

    /**
     *
     */
    private static final long serialVersionUID = -7930434399646098394L;
    /* ***
     * ATTRIBUTES
     */
    /** entities contained by this page. */
    private List<T> mContent;
    /** optional query string. */
    private String mQueryString;
    /** start element. */
    private int mOffset;
    /** page length. */
    private int mLength;
    /** page number. */
    private int mPageNum;
    /** maximum results. */
    private int mMaxResults;

    // TODO host "sort by"?

    /* ***
     * CONSTRUCTORS
     */
    private void newPage(List<T> content, int pageNum, int offset,
            int maxResults, String queryString) {
        mPageNum = pageNum;
        mOffset = offset;
        mMaxResults = maxResults;
        mQueryString = queryString;

        if (content != null) {
            mContent = new LinkedList<T>(content);
            mLength = content.size();
        } else {
            mContent = null;
            mLength = -1;
        }

    }

    public Page(List<T> content, int pageNum, int offset, int maxResults,
            String queryString) {
        newPage(content, pageNum, offset, maxResults, queryString);
    }

    public Page(List<T> content, int pageNum, int offset, int maxResults) {
        newPage(content, pageNum, offset, maxResults, null);
    }

    public Page() {
        newPage(null, -1, -1, -1, null);
    }

    /* ***
     * ACCESSORS
     */

    public List<T> getContent() {
        return mContent;
    }

    public void setContent(List<T> content) {
        mContent = content;
    }

    public String getQueryString() {
        return mQueryString;
    }

    public void setQueryString(String queryString) {
        mQueryString = queryString;
    }

    public int getOffset() {
        return mOffset;
    }

    public void setOffset(int offset) {
        mOffset = offset;
    }

    public int getLength() {
        return mLength;
    }

    public void setLength(int length) {
        mLength = length;
    }

    public int getCount() {
        return mLength;
    }

    public void setCount(int length) {
        mLength = length;
    }

    public int getPageNum() {
        return mPageNum;
    }

    public void setPageNum(int pageNum) {
        mPageNum = pageNum;
    }

    public int getTotalCount() {
        return mMaxResults;
    }

    public void setTotalCount(int total) {
        mMaxResults = total;
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
        result = prime * result + mLength;
        result = prime * result + mMaxResults;
        result = prime * result + mOffset;
        result = prime * result + mPageNum;
        result = prime * result
                + ((mQueryString == null) ? 0 : mQueryString.hashCode());
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
        if (mLength != other.mLength) {
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
        if (mQueryString == null) {
            if (other.mQueryString != null) {
                return false;
            }
        } else if (!mQueryString.equals(other.mQueryString)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {

        final StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName()).append("@").append(hashCode());
        sb.append("; PageNum=").append(mPageNum).append("; PageLength=").append(mLength);
        sb.append("; Offset=").append(mOffset).append("; Query=").append(mQueryString);
        return sb.toString();
    }

}
