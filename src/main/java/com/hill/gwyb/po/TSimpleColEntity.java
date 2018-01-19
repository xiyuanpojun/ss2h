package com.hill.gwyb.po;

import java.util.Objects;

public class TSimpleColEntity {
    private String tab;
    private String tabName;
    private String col;
    private String colName;
    private Long orderNum;

    public TSimpleColEntity() {
    }

    public TSimpleColEntity(String col, String colName) {
        this.col = col;
        this.colName = colName;
    }

    public TSimpleColEntity(String tab, String tabName, String col, String colName, Long orderNum) {
        this.tab = tab;
        this.tabName = tabName;
        this.col = col;
        this.colName = colName;
        this.orderNum = orderNum;
    }

    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public String getCol() {
        return col;
    }

    public void setCol(String col) {
        this.col = col;
    }

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

    public Long getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Long orderNum) {
        this.orderNum = orderNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TSimpleColEntity that = (TSimpleColEntity) o;
        return Objects.equals(tab, that.tab) &&
                Objects.equals(tabName, that.tabName) &&
                Objects.equals(col, that.col) &&
                Objects.equals(colName, that.colName) &&
                Objects.equals(orderNum, that.orderNum);
    }

    @Override
    public int hashCode() {

        return Objects.hash(tab, tabName, col, colName, orderNum);
    }
}
