package com.bs.tphoto;

public class PageBean {

    public PageBean() {
    }

    public PageBean(int offset, int rows) {
        this.offset = offset;
        this.rows = rows;
    }

    private int offset;

    private int rows;

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }
}
