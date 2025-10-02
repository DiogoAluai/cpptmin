package daluai.app.cpptmin.dto;

import daluai.app.cpptmin.api.NextTrains;

public class StationDto {

    private final String designation;
    private final NextTrains nextTrains;

    public StationDto(String designation, NextTrains nextTrains) {
        this.designation = designation;
        this.nextTrains = nextTrains;
    }

    public String getDesignation() {
        return designation;
    }

    public NextTrains getNextTrains() {
        return nextTrains;
    }
}
