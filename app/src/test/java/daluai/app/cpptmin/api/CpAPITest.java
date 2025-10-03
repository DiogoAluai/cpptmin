package daluai.app.cpptmin.api;

import junit.framework.TestCase;

import java.util.List;

public class CpAPITest extends TestCase {

    private final CpAPI api = CpAPI.INSTANCE;


    public void testStations() {
        List<Station> stations = api.getStations();
        assertNotNull(stations);
        assertFalse(stations.isEmpty());
    }

    public void testNextTrains() {
        var nextTrains = api.getNextTrains(new Station("94-39172", "Real code"));
        assertNotNull(nextTrains);
        assertFalse(nextTrains.getStationStops().isEmpty());
    }
}