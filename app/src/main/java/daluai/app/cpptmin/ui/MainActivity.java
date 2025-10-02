package daluai.app.cpptmin.ui;


import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import daluai.app.cpptmin.R;
import daluai.app.cpptmin.api.CpAPI;
import daluai.app.cpptmin.api.Station;
import daluai.app.sdk_boost.wrapper.LazyView;
import daluai.app.sdk_boost.wrapper.LazyViewFactory;
import daluai.app.sdk_boost.wrapper.Logger;
import daluai.app.sdk_boost.wrapper.ToastHandler;

public class MainActivity extends AppCompatActivity {

    private static final Logger LOG = Logger.ofClass(MainActivity.class);

    // Hardcoded ones I care about
    // todo: add database if needed
    private static final List<String> RELEVANT_DESIGNATIONS = List.of("Espinho", "Granja", "General Torres");

    private final CpAPI cpApi;
    private final ExecutorService executorService;
    private final ToastHandler toastHandler;

    // UI components
    private final LazyView<ListView> listView;

    public MainActivity() {
        super(R.layout.activity_main);
        this.cpApi = new CpAPI();
        this.executorService = Executors.newSingleThreadExecutor();
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
        executorService.execute(() -> {
            List<Station> stations = cpApi.getStations();
            if (stations == null) {
                toastHandler.showToast("Null 'stations'", Toast.LENGTH_LONG);
                return;
            }
            var relevantStations = stations.stream()
                    .filter(s -> RELEVANT_DESIGNATIONS.contains(s.getDesignation()))
                    .collect(Collectors.toList());
            runOnUiThread(() -> listView.get().setAdapter(new StationPanel(this, relevantStations)));
        });
    }

}