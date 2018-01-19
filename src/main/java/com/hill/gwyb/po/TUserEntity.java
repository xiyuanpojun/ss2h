package com.hill.gwyb.po;

import java.util.Objects;

public class TUserEntity {
    private String userid;
    private String upwd;
    private String urole;
    private String orgid;
    private String uname;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUpwd() {
        return upwd;
    }

    public void setUpwd(String upwd) {
        this.upwd = upwd;
    }

    public String getUrole() {
        return urole;
    }

    public void setUrole(String urole) {
        this.urole = urole;
    }

    public String getOrgid() {
        return orgid;
    }

    public void setOrgid(String orgid) {
        this.orgid = orgid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TUserEntity that = (TUserEntity) o;
        return Objects.equals(userid, that.userid) &&
                Objects.equals(upwd, that.upwd) &&
                Objects.equals(urole, that.urole) &&
                Objects.equals(orgid, that.orgid) &&
                Objects.equals(uname, that.uname);
    }

    @Override
    public int hashCode() {

        return Objects.hash(userid, upwd, urole, orgid, uname);
    }

    @Override
    public String toString() {
        return "TUserEntity{" +
                "userid='" + userid + '\'' +
                ", upwd='" + upwd + '\'' +
                ", urole='" + urole + '\'' +
                ", orgid='" + orgid + '\'' +
                ", uname='" + uname + '\'' +
                '}';
    }
}
