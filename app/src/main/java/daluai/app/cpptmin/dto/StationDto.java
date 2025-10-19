package daluai.app.cpptmin.dto;

import daluai.app.cpptmin.api.NextTrains;
import daluai.app.cpptmin.api.Station;

public class StationDto {

    private final Station station;
    private final NextTrains nextTrains;

    public StationDto(Station station, NextTrains nextTrains) {
        this.station = station;
        this.nextTrains = nextTrains;
    }

    public Station getStation() {
        return station;
    }

    public NextTrains getNextTrains() {
        return nextTrains;
    }
}
