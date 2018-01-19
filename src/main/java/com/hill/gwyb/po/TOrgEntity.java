package com.hill.gwyb.po;

import java.util.Objects;

public class TOrgEntity {
    private String orgid;
    private String orgname;
    private String pOrgid;
    private String pOrg;

    public String getOrgid() {
        return orgid;
    }

    public void setOrgid(String orgid) {
        this.orgid = orgid;
    }

    public String getOrgname() {
        return orgname;
    }

    public void setOrgname(String orgname) {
        this.orgname = orgname;
    }

    public String getpOrgid() {
        return pOrgid;
    }

    public void setpOrgid(String pOrgid) {
        this.pOrgid = pOrgid;
    }

    public String getpOrg() {
        return pOrg;
    }

    public void setpOrg(String pOrg) {
        this.pOrg = pOrg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TOrgEntity that = (TOrgEntity) o;
        return Objects.equals(orgid, that.orgid) &&
                Objects.equals(orgname, that.orgname) &&
                Objects.equals(pOrgid, that.pOrgid) &&
                Objects.equals(pOrg, that.pOrg);
    }

    @Override
    public int hashCode() {

        return Objects.hash(orgid, orgname, pOrgid, pOrg);
    }

    @Override
    public String toString() {
        return "TOrgEntity{" +
                "orgid='" + orgid + '\'' +
                ", orgname='" + orgname + '\'' +
                ", pOrgid='" + pOrgid + '\'' +
                ", pOrg='" + pOrg + '\'' +
                '}';
    }
}
