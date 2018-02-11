import java.util.Objects;

public class TCityLocationEntity {
    /**
     * 城市
     */
    private String city;
    /**
     * 经度
     */
    private double lng;
    /**
     * 纬度
     */
    private double lat;


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TCityLocationEntity that = (TCityLocationEntity) o;
        return Objects.equals(city, that.city) &&
                Objects.equals(lng, that.lng) &&
                Objects.equals(lat, that.lat);
    }

    @Override
    public int hashCode() {

        return Objects.hash(city, lng, lat);
    }

    @Override
    public String toString() {
        return "TCityLocationEntity{" +
                "city='" + city + '\'' +
                ", lng=" + lng +
                ", lat=" + lat +
                '}';
    }
}
