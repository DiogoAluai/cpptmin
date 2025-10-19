package daluai.app.cpptmin.ui;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import java.util.concurrent.Executors;

import daluai.app.cpptmin.R;
import daluai.app.sdk_boost.wrapper.LazyView;
import daluai.app.sdk_boost.wrapper.LazyViewFactory;
import daluai.app.sdk_boost.wrapper.Logger;
import daluai.app.sdk_boost.wrapper.ToastHandler;

public class MainActivity extends AppCompatActivity {

    private static final Logger LOG = Logger.ofClass(MainActivity.class);

    private static final int REQ_LOCATION = 1002;

    // UI components
    private final ToastHandler toastHandler;
    private final LazyView<ListView> listView;
    private final LazyView<Toolbar> toolbar;

    public MainActivity() {
        super(R.layout.activity_main);
        this.toastHandler = new ToastHandler(this);
        var lazyViewFactory = new LazyViewFactory(this);
        this.listView = lazyViewFactory.createView(R.id.list_view);
        this.toolbar = lazyViewFactory.createView(R.id.toolbar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        askCoarseLocationPermissionOrFail();
        
        setSupportActionBar(toolbar.get());

        var stationsViewModel = new ViewModelProvider(this).get(StationsViewModel.class);
        var stationsLiveData = stationsViewModel.getLive();

        var adapter = new StationPanelAdapter(this, stationsLiveData.getValue());

        stationsLiveData.observe(this, stationDtos -> {
            adapter.clear();
            adapter.addAll(stationDtos);
            adapter.notifyDataSetChanged();
        });

        listView.get().setAdapter(adapter);
    }

    private void askCoarseLocationPermissionOrFail() {
        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQ_LOCATION);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_option_settings) {
            // handle click
            toastHandler.showToast("Not implemented");
            return true;
        }

        if (id == R.id.action_refresh) {
            refreshStationData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void refreshStationData() {
        var stationsViewModel = new ViewModelProvider(this).get(StationsViewModel.class);
        Executors.newSingleThreadExecutor().execute(stationsViewModel::refetchData);
    }
}