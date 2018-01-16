package cn.softlq.ss2h.po;

import java.util.Objects;

public class TSurveyColEntity {
    private String typeId;
    private String col;
    private String colName;
    private String surveyType;
    private Long showNum;

    public TSurveyColEntity() {
    }

    public TSurveyColEntity(String typeId, String col, String colName) {
        this.typeId = typeId;
        this.col = col;
        this.colName = colName;
    }

    public TSurveyColEntity(String typeId, String col, String colName, String surveyType, Long showNum) {
        this.typeId = typeId;
        this.col = col;
        this.colName = colName;
        this.surveyType = surveyType;
        this.showNum = showNum;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
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

    public String getSurveyType() {
        return surveyType;
    }

    public void setSurveyType(String surveyType) {
        this.surveyType = surveyType;
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
        TSurveyColEntity that = (TSurveyColEntity) o;
        return Objects.equals(typeId, that.typeId) &&
                Objects.equals(col, that.col) &&
                Objects.equals(colName, that.colName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(typeId, col, colName);
    }
}
