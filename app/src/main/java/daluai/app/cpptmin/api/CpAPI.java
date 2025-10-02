package daluai.app.cpptmin.api;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import daluai.app.sdk_boost.wrapper.Logger;


public class CpAPI {

    private static final Logger LOG = Logger.ofClass(CpAPI.class);

    public static final String SERVICES_API_URL = "https://api-gateway.cp.pt/cp/services";
    private static final String STATIONS_API_URL = SERVICES_API_URL + "/stations-api";
    private static final String TRAVEL_API_URL = SERVICES_API_URL + "/travel-api";
    private static final int TIMEOUT = 5000; // 5s timeout
    private static final String GET = "GET";

    private final ObjectMapper objectMapper;

    public CpAPI() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    /**
     * Full proof URL object creation
     */
    private URL sushCreateUrl(String urlString) {
        try {
            return new URL(urlString);
        } catch (MalformedURLException e) {
            // never happens, since it's not malformed, duh
            return null;
        }
    }

    public List<Station> getStations() {
        String jsonResponse = get(sushCreateUrl(STATIONS_API_URL + "/stations/infos"));
        var stations = parseResponse(jsonResponse, new TypeReference<List<Station>>() {});
        return stations != null ? stations : Collections.emptyList();
    }

    public NextTrains getNextTrains(Station station) {
        String date = LocalDate.now().toString();
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        String urlString = TRAVEL_API_URL + String.format("/stations/%s/timetable/%s?start=%s", station.getCode(), date, time);
        String jsonResponse = get(sushCreateUrl(urlString));
        return parseResponse(jsonResponse, new TypeReference<>() {});
    }

    private <T> T parseResponse(String json, TypeReference<T> type) {
        try {
            return objectMapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            LOG.e("Failed parsing json response", e);
            return null;
        }
    }

    private String get(URL url) {
        try {
            return getChecked(url);
        } catch (IOException e) {
            LOG.e("Error during get request", e);
            return null;
        }
    }

    @NonNull
    private static String getChecked(URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        configureConnection(conn);
        conn.setRequestMethod(GET);

        return getResponseString(conn);
    }

    private static void configureConnection(HttpURLConnection conn) {
        conn.setConnectTimeout(TIMEOUT);
        conn.setReadTimeout(TIMEOUT);
        // tokens seem to be hardcoded
        conn.setRequestProperty("x-cp-connect-secret", "74bd06d5a2715c64c2f848c5cdb56e6b");
        conn.setRequestProperty("x-cp-connect-id", "1483ea620b920be6328dcf89e808937a");
        conn.setRequestProperty("X-Api-Key", "DUMMY");
    }

    @NonNull
    private static String getResponseString(HttpURLConnection conn) throws IOException {
        int status = conn.getResponseCode();
        var reader = new BufferedReader(
                new InputStreamReader(status >= 300 ? conn.getErrorStream() : conn.getInputStream())
        );

        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        conn.disconnect();

        return response.toString();
    }
}
