package org.singledog.websql.web.result;

import org.apache.ibatis.features.jpa.domain.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adam on 2017/9/15.
 */
public class ListResult<T> {

    private long total;
    private List<T> rows = new ArrayList<>();
    private int pageSize;
    private long pageNumber;

    public ListResult() {
    }

    public ListResult(Page page) {
        this.total = page.getTotalElements();
        this.pageSize = page.getSize();
        this.pageNumber = page.getTotalPages();
    }

    public ListResult(long total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }

    public ListResult(long total, List<T> rows, int pageSize, long pageNumber) {
        this.total = total;
        this.rows = rows;
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public ListResult setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public long getPageNumber() {
        return pageNumber;
    }

    public ListResult<T> setPageNumber(long pageNumber) {
        this.pageNumber = pageNumber;
        return this;
    }

    public long getTotal() {
        return total;
    }

    public ListResult setTotal(long total) {
        this.total = total;
        return this;
    }

    public List<T> getRows() {
        return rows;
    }

    public ListResult setRows(List<T> rows) {
        this.rows = rows;
        return this;
    }
}
