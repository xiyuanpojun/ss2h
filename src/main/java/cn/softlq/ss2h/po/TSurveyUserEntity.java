package cn.softlq.ss2h.po;

import java.util.Objects;

public class TSurveyUserEntity {
    private String sUserId;
    private String sUserName;
    private String orgid;
    private String userid;

    public String getsUserId() {
        return sUserId;
    }

    public void setsUserId(String sUserId) {
        this.sUserId = sUserId;
    }

    public String getsUserName() {
        return sUserName;
    }

    public void setsUserName(String sUserName) {
        this.sUserName = sUserName;
    }

    public String getOrgid() {
        return orgid;
    }

    public void setOrgid(String orgid) {
        this.orgid = orgid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TSurveyUserEntity that = (TSurveyUserEntity) o;
        return Objects.equals(sUserId, that.sUserId) &&
                Objects.equals(sUserName, that.sUserName) &&
                Objects.equals(orgid, that.orgid) &&
                Objects.equals(userid, that.userid);
    }

    @Override
    public int hashCode() {

        return Objects.hash(sUserId, sUserName, orgid, userid);
    }

    @Override
    public String toString() {
        return "TSurveyUserEntity{" +
                "sUserId='" + sUserId + '\'' +
                ", sUserName='" + sUserName + '\'' +
                ", orgid='" + orgid + '\'' +
                ", userid='" + userid + '\'' +
                '}';
    }
}
