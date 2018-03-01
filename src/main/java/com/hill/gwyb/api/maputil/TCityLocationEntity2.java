package com.hill.gwyb.api.maputil;

public class TCityLocationEntity2 extends TCityLocationEntity {
    /**
     * 代码
     */
    private String code;
    /**
     * 距离
     */
    private double dist;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getDist() {
        return dist;
    }

    public void setDist(double dist) {
        this.dist = dist;
    }

    @Override
    public String toString() {
        return "{\"city\"=\"" + getCity() + '\'' +
                ",province='" + getProvince() +
                ", adress=" + getAdress() +
                "\",\"lng\"=\"" + getLng() +
                "\",\"lat\"=\"" + getLat() +
                "\",\"code\"=\"" + code +
                "\",\"dist\"=\"" + dist +
                "\"}";
    }
}
