package com.excilys.cdb.dto;

import java.io.Serializable;
import java.util.List;

import com.excilys.cdb.model.Page;

/**
 *
 * @author Nicolas THIERION
 *
 *         TODO JavaDoc.
 * @param <T>
 */
public class PageDto implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -8288880287578374239L;

    /* ***
     * ATTRIBUTES
     */
    /** optional query string. */
    private String            mQueryString;
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
    private String            mSortOrder;
    /** content type. */
    private String            mContentType;

    /* ***
     * CONSTRUCTORS
     */
    private void mNewPageDto(String contentType, int size, int offset, int maxResults, String queryString,
            String sortBy, String order) {

        mContentType = contentType;
        mOffset = offset;
        mMaxResults = maxResults;
        mQueryString = queryString;
        mSortBy = sortBy;
        mSortOrder = order;
        mSize = size;

        if (mSize > 0) {
            mPageNum = (mOffset / mSize) + 1;
        } else {
            mPageNum = 0;
        }
    }

    public PageDto() {
        mNewPageDto(null, 0, 0, 0, null, null, null);
    }

    public PageDto(String contentType, int size, int offset, int maxResults, String queryString, String sortBy,
            String order) {
        mNewPageDto(contentType, size, offset, maxResults, queryString, sortBy, order);
    }

    public PageDto(PageDto pageDto) {
        final PageDto d = pageDto;
        mNewPageDto(d.mContentType, d.mSize, d.mOffset, d.mMaxResults, d.mQueryString, d.mSortBy, d.mSortOrder);
    }

    public static PageDto fromPage(Page<?> page) {
        final List<?> content = page.getContent();
        final String contentType = null;
        int size = 0;
        if (content != null) {
            content.getClass().getSimpleName();
            size = content.size();
        }
        final PageDto dto = new PageDto();
        dto.mNewPageDto(contentType, size, page.getOffset(), page.getMaxNum(), page.getSearch(), page.getSortBy(),
                page
                .getSortOrder().toString());
        return dto;
    }



    /* ***
     * GETTERS
     */

    public String getQueryString() {
        return mQueryString;
    }

    public int getOffset() {
        return mOffset;
    }

    public int getSize() {
        return mSize;
    }

    public int getPageNum() {
        return mPageNum;
    }

    public int getMaxResults() {
        return mMaxResults;
    }

    public String getSortBy() {
        return mSortBy;
    }

    public String getSortOrder() {
        return mSortOrder;
    }

    public String getContentType() {
        return mContentType;
    }

    /* ***
     * SETTERS
     */
    public void setQueryString(String queryString) {
        this.mQueryString = queryString;
    }

    public void setOffset(int offset) {
        this.mOffset = offset;
    }

    public void setSize(int size) {
        this.mSize = size;
    }

    public void setPageNum(int pageNum) {
        this.mPageNum = pageNum;
    }

    public void setMaxResults(int maxResults) {
        this.mMaxResults = maxResults;
    }

    public void setSortBy(String sortBy) {
        this.mSortBy = sortBy;
    }

    public void setSortOrder(String sortOrder) {
        this.mSortOrder = sortOrder;
    }

    public void setContentType(String contentType) {
        this.mContentType = contentType;
    }

}
