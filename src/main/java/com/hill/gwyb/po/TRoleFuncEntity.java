package com.hill.gwyb.po;

import java.util.Objects;

public class TRoleFuncEntity {
    private String roleid;
    private int orderNum;
    private String funcId;

    public String getRoleid() {
        return roleid;
    }

    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public String getFuncId() {
        return funcId;
    }

    public void setFuncId(String funcId) {
        this.funcId = funcId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TRoleFuncEntity that = (TRoleFuncEntity) o;
        return Objects.equals(roleid, that.roleid) &&
                Objects.equals(orderNum, that.orderNum) &&
                Objects.equals(funcId, that.funcId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(roleid, orderNum, funcId);
    }
}
