package daluai.app.cpptmin.ui;


import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
    private final LazyView<SwipeRefreshLayout> swipeRefresh;

    public MainActivity() {
        super(R.layout.activity_main);
        this.uiExecutorService = Executors.newFixedThreadPool(3);
        var lazyViewFactory = new LazyViewFactory(this);
        this.swipeRefresh = lazyViewFactory.createView(R.id.swipe_refresh);
        this.listView = lazyViewFactory.createView(R.id.list_view);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureStationListView();
    }

    /**
     * Populates station data in list view. Only after that sets the refresh listener up
     */
    private void configureStationListView() {
        // In case the user is too fast, we provide neutral refreshing
        swipeRefresh.get().setOnRefreshListener(() -> swipeRefresh.get().setRefreshing(false));

        uiExecutorService.execute(() -> {
            var stations = new ViewModelProvider(this).get(StationsViewModel.class).get();
            var adapter = new StationPanelAdapter(this, stations);
            runOnUiThread(() -> listView.get().setAdapter(adapter));
            swipeRefresh.get().setOnRefreshListener(() -> refreshListener(adapter));
        });
    }

    private void refreshListener(StationPanelAdapter adapter) {
        var stationsViewModel = new ViewModelProvider(this).get(StationsViewModel.class);
        runOnUiThread(() -> {
            stationsViewModel.clearData();
            adapter.notifyDataSetChanged();
        });
        uiExecutorService.execute(() -> {
            stationsViewModel.refreshData();
            runOnUiThread(() -> {
                adapter.notifyDataSetChanged();
                swipeRefresh.get().setRefreshing(false);
            });
        });
    }
}