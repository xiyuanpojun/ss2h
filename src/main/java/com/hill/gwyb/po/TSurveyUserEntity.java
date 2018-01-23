package com.hill.gwyb.po;

public class TSurveyUserEntity {
    private String sUserId;
    private String sUserName;
    private String orgid;
    private String userid;
    private String disrm;

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

    public String getDisrm() {
        return disrm;
    }

    public void setDisrm(String dis_rm) {
        this.disrm = dis_rm;
    }

    @Override
    public String toString() {
        return "TSurveyUserEntity{" +
                "sUserId='" + sUserId + '\'' +
                ", sUserName='" + sUserName + '\'' +
                ", orgid='" + orgid + '\'' +
                ", userid='" + userid + '\'' +
                ", dis_rm='" + disrm + '\'' +
                '}';
    }
}
