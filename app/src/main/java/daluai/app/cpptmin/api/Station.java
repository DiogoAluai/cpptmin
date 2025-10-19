package daluai.app.cpptmin.api;

import java.util.Objects;

public class Station {

    private String code;
    private String designation;
    private Double latitude;
    private Double longitude;

    public Station() {
        // deserializer
    }

    public Station(String code, String designation) {
        this.code = code;
        this.designation = designation;
    }

    public String getCode() {
        return code;
    }

    public String getDesignation() {
        return designation;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Station station = (Station) o;
        return Objects.equals(code, station.code);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(code);
    }
}
