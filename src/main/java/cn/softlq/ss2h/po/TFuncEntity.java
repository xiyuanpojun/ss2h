package cn.softlq.ss2h.po;

import java.util.Objects;

public class TFuncEntity {
    private String fId;
    private String fName;
    private String fUrl;

    public TFuncEntity() {
    }

    public TFuncEntity(String fId, String fName, String fUrl) {
        this.fId = fId;
        this.fName = fName;
        this.fUrl = fUrl;
    }

    public String getfId() {
        return fId;
    }

    public void setfId(String fId) {
        this.fId = fId;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getfUrl() {
        return fUrl;
    }

    public void setfUrl(String fUrl) {
        this.fUrl = fUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TFuncEntity that = (TFuncEntity) o;
        return Objects.equals(fId, that.fId) &&
                Objects.equals(fName, that.fName) &&
                Objects.equals(fUrl, that.fUrl);
    }

    @Override
    public int hashCode() {

        return Objects.hash(fId, fName, fUrl);
    }

    @Override
    public String toString() {
        return "TFuncEntity{" +
                "fId='" + fId + '\'' +
                ", fName='" + fName + '\'' +
                ", fUrl='" + fUrl + '\'' +
                '}';
    }
}
