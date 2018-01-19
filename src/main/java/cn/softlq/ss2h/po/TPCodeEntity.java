package cn.softlq.ss2h.po;

import java.util.Objects;

public class TPCodeEntity {
    private String pid;
    private String pname;
    private String ptype;

    public TPCodeEntity() {
    }

    public TPCodeEntity(String ptype) {
        this.ptype = ptype;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPtype() {
        return ptype;
    }

    public void setPtype(String ptype) {
        this.ptype = ptype;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TPCodeEntity that = (TPCodeEntity) o;
        return Objects.equals(pid, that.pid) &&
                Objects.equals(pname, that.pname) &&
                Objects.equals(ptype, that.ptype);
    }

    @Override
    public int hashCode() {

        return Objects.hash(pid, pname, ptype);
    }

    @Override
    public String toString() {
        return "TPCodeEntity{" +
                "pid='" + pid + '\'' +
                ", pname='" + pname + '\'' +
                ", ptype='" + ptype + '\'' +
                '}';
    }
}
