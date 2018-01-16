package cn.softlq.ss2h.po;

import java.sql.Time;
import java.util.Objects;

public class TUserLoginEntity {
    private String userid;
    private Time logTime;
    private Time outTime;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Time getLogTime() {
        return logTime;
    }

    public void setLogTime(Time logTime) {
        this.logTime = logTime;
    }

    public Time getOutTime() {
        return outTime;
    }

    public void setOutTime(Time outTime) {
        this.outTime = outTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TUserLoginEntity that = (TUserLoginEntity) o;
        return Objects.equals(userid, that.userid) &&
                Objects.equals(logTime, that.logTime) &&
                Objects.equals(outTime, that.outTime);
    }

    @Override
    public int hashCode() {

        return Objects.hash(userid, logTime, outTime);
    }

    @Override
    public String toString() {
        return "TUserLoginEntity{" +
                "userid='" + userid + '\'' +
                ", logTime=" + logTime +
                ", outTime=" + outTime +
                '}';
    }
}
