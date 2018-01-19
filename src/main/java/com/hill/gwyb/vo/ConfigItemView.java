package com.hill.gwyb.vo;

public class ConfigItemView {
    private String surveyType;
    private String showNum;
    private String col;
    private String colName;

    public ConfigItemView() {
    }

    public ConfigItemView(String surveyType, String showNum, String col, String colName) {
        this.surveyType = surveyType;
        this.showNum = showNum;
        this.col = col;
        this.colName = colName;
    }

    public String getSurveyType() {
        return surveyType;
    }

    public void setSurveyType(String surveyType) {
        this.surveyType = surveyType;
    }

    public String getShowNum() {
        return showNum;
    }

    public void setShowNum(String showNum) {
        this.showNum = showNum;
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

    @Override
    public String toString() {
        return "ConfigItemView{" +
                "surveyType='" + surveyType + '\'' +
                ", showNum='" + showNum + '\'' +
                ", col='" + col + '\'' +
                ", colName='" + colName + '\'' +
                '}';
    }
}
