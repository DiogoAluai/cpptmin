package daluai.app.cpptmin.ui;


import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import daluai.app.cpptmin.R;
import daluai.app.cpptmin.api.CpAPI;
import daluai.app.cpptmin.api.Station;
import daluai.app.cpptmin.dto.StationDto;
import daluai.app.sdk_boost.wrapper.LazyView;
import daluai.app.sdk_boost.wrapper.LazyViewFactory;
import daluai.app.sdk_boost.wrapper.Logger;
import daluai.app.sdk_boost.wrapper.ToastHandler;

public class MainActivity extends AppCompatActivity {

    private static final Logger LOG = Logger.ofClass(MainActivity.class);

    // Hardcoded ones I care about
    // todo: add database if needed
    private static final List<String> RELEVANT_DESIGNATIONS = List.of("Espinho", "Granja", "General Torres");
    private static final int MAX_N_THREADS = 5;

    private final CpAPI cpApi;
    private final ExecutorService uiExecutorService;
    private final ExecutorService apiThreadPool;
    private final ToastHandler toastHandler;

    // UI components
    private final LazyView<ListView> listView;

    public MainActivity() {
        super(R.layout.activity_main);
        this.cpApi = new CpAPI();
        this.uiExecutorService = Executors.newSingleThreadExecutor();
        this.apiThreadPool = Executors.newFixedThreadPool(MAX_N_THREADS);
        var lazyViewFactory = new LazyViewFactory(this);
        this.listView = lazyViewFactory.createView(R.id.list_view);
        this.toastHandler = new ToastHandler(this);
        LOG.i("Relying on hardcoded relevant designations: " + RELEVANT_DESIGNATIONS);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        parallelPopulateStations();
    }

    private void parallelPopulateStations() {
        uiExecutorService.execute(() -> {
            List<Station> stations = cpApi.getStations();
            if (stations == null) {
                toastHandler.showToast("Null 'stations'", Toast.LENGTH_LONG);
                return;
            }
            var relevantStations = getNextTrains(stations);
            runOnUiThread(() -> listView.get().setAdapter(new StationPanel(this, relevantStations)));
        });
    }

    /**
     * API calls to CP to populate every row with information
     * Improvements can most likely be done.
     */
    private List<StationDto> getNextTrains(List<Station> stations) {
        var relevantStations = stations.stream()
                .filter(s -> RELEVANT_DESIGNATIONS.contains(s.getDesignation()))
                .collect(Collectors.toList());
        try {
            List<Callable<StationDto>> tasks = relevantStations.stream()
                    .<Callable<StationDto>>map(station -> () -> cpApi.getStationDto(station))
                    .collect(Collectors.toList());

            List<StationDto> stationDtos = new ArrayList<>();
            List<Future<StationDto>> nextTrainsFuture = apiThreadPool.invokeAll(tasks);
            for (var future : nextTrainsFuture) {
                StationDto result = future.get();
                if (result == null) {
                    LOG.e("Got null result from api call");
                    continue;
                }
                // order should be kept, I think
                stationDtos.add(result);
            }
            return stationDtos;
        } catch (InterruptedException | ExecutionException e) {
            LOG.e("Exception during api calls", e);
            toastHandler.showToast(e.getMessage(), Toast.LENGTH_LONG);
            return Collections.emptyList();
        }
    }

}