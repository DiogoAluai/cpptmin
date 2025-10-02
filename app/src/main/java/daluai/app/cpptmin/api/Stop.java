package daluai.app.cpptmin.api;

public class Stop {

    private String arrivalTime;
    private Station trainOrigin;
    private Station trainDestination;
    private Suppression suppression;

    public Stop() {
    }

    public boolean isSuppressed() {
        return suppression != null;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Station getTrainOrigin() {
        return trainOrigin;
    }

    public void setTrainOrigin(Station trainOrigin) {
        this.trainOrigin = trainOrigin;
    }

    public Station getTrainDestination() {
        return trainDestination;
    }

    public void setTrainDestination(Station trainDestination) {
        this.trainDestination = trainDestination;
    }

    public Suppression getSuppression() {
        return suppression;
    }

    public void setSuppression(Suppression suppression) {
        this.suppression = suppression;
    }
}
