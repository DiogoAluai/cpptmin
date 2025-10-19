package daluai.app.cpptmin.api;

import java.util.ArrayList;
import java.util.List;

public class NextTrains {

    private final List<Stop> stationStops;

    public NextTrains() {
        // also needed for deserialization
        this.stationStops = new ArrayList<>();
    }

    public List<Stop> getStationStops() {
        return stationStops;
    }
}
