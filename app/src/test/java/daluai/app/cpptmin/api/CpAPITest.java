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

    public void testStation() {
        Station station = api.getStation("94-39172");
        assertNotNull(station);
        assertNotNull(station.getLatitude());
        assertNotNull(station.getLongitude());
        assertEquals("94-39172", station.getCode());
        assertEquals("General Torres", station.getDesignation());
    }

    public void testNextTrains() {
        var nextTrains = api.getNextTrains("94-39172");
        assertNotNull(nextTrains);
        assertFalse(nextTrains.getStationStops().isEmpty());
        Stop stop = nextTrains.getStationStops().get(0);
        assertNotNull(stop.getArrivalTime());
        assertNotNull(stop.getDepartureTime());
        assertNotNull(stop.getTrainOrigin());
        assertNotNull(stop.getTrainDestination());

    }
}