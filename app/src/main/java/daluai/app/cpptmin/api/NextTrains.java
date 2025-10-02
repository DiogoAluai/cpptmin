package daluai.app.cpptmin.api;

import java.util.List;

public class NextTrains {

    private List<Stop> stationStops;

    public NextTrains() {
    }

    public List<Stop> getStationStops() {
        return stationStops;
    }

    public void setStationStops(List<Stop> stationStops) {
        this.stationStops = stationStops;
    }
}
