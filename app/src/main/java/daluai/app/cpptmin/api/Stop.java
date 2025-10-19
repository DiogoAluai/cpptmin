package daluai.app.cpptmin.api;

import androidx.annotation.Nullable;

public class Stop {

    private String arrivalTime;
    private String departureTime;
    private Station trainOrigin;
    private Station trainDestination;
    private Suppression suppression;
    private Integer delay; // minutes

    public Stop() {
        // keep me
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public Station getTrainOrigin() {
        return trainOrigin;
    }

    public Station getTrainDestination() {
        return trainDestination;
    }

    @Nullable
    public Suppression getSuppression() {
        return suppression;
    }

    @Nullable
    public Integer getDelay() {
        return delay;
    }
}
