package cn.softlq.ss2h.po;

import java.util.Objects;

public class TSurveyTypeEntity {
    private String typeId;
    private String surveyType;
    private String tab;
    private Long showNum;

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getSurveyType() {
        return surveyType;
    }

    public void setSurveyType(String surveyType) {
        this.surveyType = surveyType;
    }

    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }

    public Long getShowNum() {
        return showNum;
    }

    public void setShowNum(Long showNum) {
        this.showNum = showNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TSurveyTypeEntity that = (TSurveyTypeEntity) o;
        return Objects.equals(typeId, that.typeId) &&
                Objects.equals(surveyType, that.surveyType) &&
                Objects.equals(tab, that.tab) &&
                Objects.equals(showNum, that.showNum);
    }

    @Override
    public int hashCode() {

        return Objects.hash(typeId, surveyType, tab, showNum);
    }

    @Override
    public String toString() {
        return "TSurveyTypeEntity{" +
                "typeId='" + typeId + '\'' +
                ", surveyType='" + surveyType + '\'' +
                ", tab='" + tab + '\'' +
                ", showNum=" + showNum +
                '}';
    }
}
