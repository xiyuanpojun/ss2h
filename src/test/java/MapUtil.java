public class MapUtil {
    private static final double EARTH_RADIUS = 6378137;//赤道半径单位为米

    /**
     * 根据两点间经纬度坐标（double值），计算两点间距离，单位为米
     * @param lon1
     * @param lat1
     * @param lon2
     * @param lat2
     * @return
     */
    public static double getDistance(double lon1,double lat1,double lon2,double lat2){
       double radLat1=rad(lat1);
       double radLat2=rad(lat2);
       double a= radLat1-radLat2;
       double b=rad(lon1)-rad(lon2);
       double s=2*Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2)+Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
       s=s*EARTH_RADIUS;
       return  s;
    }
    //把经纬度转为度（°）
    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }


}
