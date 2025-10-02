package daluai.app.cpptmin.api;

public class Station {

    private String code;
    private String designation;

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

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }
}
