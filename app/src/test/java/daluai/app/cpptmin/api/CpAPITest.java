package daluai.app.cpptmin.api;

import junit.framework.TestCase;

import java.util.List;

public class CpAPITest extends TestCase {

    public void testStations() {
        var api = new CpAPI();
        List<Station> stations = api.getStations();
        System.out.println(stations);
    }

    public void testNextTrains() {
        var api = new CpAPI();
        var nextTrains = api.getNextTrains(new Station("94-39172", "Real code"));
        System.out.println(nextTrains);
    }


}