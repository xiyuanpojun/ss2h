package com.hill.gwyb.api.maputil;

public class MapUtil {
    private static final double EARTH_RADIUS = 6378137;//赤道半径单位为米
    private static double PI = 3.14159265;
    private static double RAD = Math.PI / 180.0;

    /// <summary>
    /// 根据提供的经度和纬度、以及半径，取得此半径内的最大最小经纬度
    /// </summary>
    /// <param name="lat">纬度</param>
    /// <param name="lon">经度</param>
    /// <param name="raidus">半径(米)</param>
    /// <returns></returns>
    public static double[] GetAround(double lat, double lon, int raidus) {

        Double latitude = lat;
        Double longitude = lon;

        Double degree = (24901 * 1609) / 360.0;
        double raidusMile = raidus;

        Double dpmLat = 1 / degree;
        Double radiusLat = dpmLat * raidusMile;
        Double minLat = latitude - radiusLat;
        Double maxLat = latitude + radiusLat;
        Double mpdLng = degree * Math.cos(latitude * (PI / 180));
        Double dpmLng = 1 / mpdLng;
        Double radiusLng = dpmLng * raidusMile;
        Double minLng = longitude - radiusLng;
        Double maxLng = longitude + radiusLng;
        return new double[]{minLat, minLng, maxLat, maxLng};
    }


    /**
     * 根据两点间经纬度坐标（double值），计算两点间距离，单位为米
     *
     * @param lon1
     * @param lat1
     * @param lon2
     * @param lat2
     * @return
     */
    public static double getDistance(double lon1, double lat1, double lon2, double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lon1) - rad(lon2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        return s;
    }

    //把经纬度转为度（°）
    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }


}
