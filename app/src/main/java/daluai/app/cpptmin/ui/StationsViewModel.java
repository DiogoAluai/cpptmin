package daluai.app.cpptmin.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import daluai.app.cpptmin.api.CpAPI;
import daluai.app.cpptmin.api.Station;
import daluai.app.cpptmin.dto.StationDto;
import daluai.app.sdk_boost.wrapper.Logger;

public class StationsViewModel extends ViewModel {

    private static final Logger LOG = Logger.ofClass(StationsViewModel.class);

    // Hardcoded ones I care about
    // todo: add database if needed
    private static final List<String> RELEVANT_DESIGNATIONS = List.of("Espinho", "Granja", "General Torres");
    private static final int MAX_N_THREADS = 5;

    private final CpAPI cpApi;
    private final ExecutorService apiThreadPool;

    private final AtomicReference<List<StationDto>> cacheAtomicReference;

    public StationsViewModel() {
        this.cpApi = CpAPI.INSTANCE;
        this.apiThreadPool = Executors.newFixedThreadPool(MAX_N_THREADS);
        this.cacheAtomicReference = new AtomicReference<>(null);
        LOG.i("Relying on hardcoded relevant designations: " + RELEVANT_DESIGNATIONS);
    }

    /**
     * Wait for api and get all StationDtos.
     * Guaranteed same reference returned everytime.
     *
     * @return non-null stations
     */
    @NonNull
    public synchronized List<StationDto> get() {
        List<StationDto> cachedStations = cacheAtomicReference.get();
        if (cachedStations != null) {
            return cachedStations;
        }
        var stations = getNextTrains();
        cacheAtomicReference.set(stations);
        return stations;
    }

    public synchronized void refreshData() {
        var cachedStations = safeCacheValue();
        List<StationDto> nextTrains = getNextTrains();
        cachedStations.clear();
        cachedStations.addAll(nextTrains);
    }

    public synchronized void clearData() {
        safeCacheValue().clear();
    }

    private List<StationDto> safeCacheValue() {
        var cachedStations = cacheAtomicReference.get();
        return cachedStations != null ? cachedStations : new ArrayList<>();
    }

    /**
     * API calls to CP to populate every row with information
     * Improvements can most likely be done.
     */
    private List<StationDto> getNextTrains() {
        var relevantStations = getRelevantStations();

        try {
            List<Callable<StationDto>> apiCallTasks = relevantStations.stream()
                    .<Callable<StationDto>>map(station -> () -> cpApi.getStationDto(station))
                    .collect(Collectors.toList());

            List<StationDto> stationDtos = new ArrayList<>();
            List<Future<StationDto>> nextTrainsFuture = apiThreadPool.invokeAll(apiCallTasks);
            for (var futureApiResponse : nextTrainsFuture) {
                StationDto result = futureApiResponse.get();
                if (result == null) {
                    LOG.e("Got null result from api call");
                } else {
                    stationDtos.add(result);
                }
            }
            return stationDtos;
        } catch (InterruptedException | ExecutionException e) {
            LOG.e("Exception during api calls", e);
            return Collections.emptyList();
        }
    }

    @NonNull
    private List<Station> getRelevantStations() {
        var stations = cpApi.getStations();
        if (stations == null) {
            LOG.e("Got null stations from api");
            return Collections.emptyList();
        }
        return stations.stream()
                .filter(s -> RELEVANT_DESIGNATIONS.contains(s.getDesignation()))
                .collect(Collectors.toList());
    }
}
