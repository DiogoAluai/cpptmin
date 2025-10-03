package daluai.app.cpptmin.ui;


import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import daluai.app.cpptmin.R;
import daluai.app.sdk_boost.wrapper.LazyView;
import daluai.app.sdk_boost.wrapper.LazyViewFactory;
import daluai.app.sdk_boost.wrapper.Logger;

public class MainActivity extends AppCompatActivity {

    private static final Logger LOG = Logger.ofClass(MainActivity.class);

    private final ExecutorService uiExecutorService;

    // UI components
    private final LazyView<ListView> listView;

    public MainActivity() {
        super(R.layout.activity_main);
        this.uiExecutorService = Executors.newSingleThreadExecutor();
        var lazyViewFactory = new LazyViewFactory(this);
        this.listView = lazyViewFactory.createView(R.id.list_view);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parallelPopulateStations();

    }

    private void parallelPopulateStations() {
        uiExecutorService.execute(() -> {
            var stations = new ViewModelProvider(this).get(StationsViewModel.class).get();
            var adapter = new StationPanelAdapter(this, stations);
            runOnUiThread(() -> listView.get().setAdapter(adapter));
        });
    }
}