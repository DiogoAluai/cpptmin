package daluai.app.cpptmin.ui;


import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import daluai.app.cpptmin.R;
import daluai.app.sdk_boost.wrapper.LazyView;
import daluai.app.sdk_boost.wrapper.LazyViewFactory;
import daluai.app.sdk_boost.wrapper.Logger;

public class MainActivity extends AppCompatActivity {

    private static final Logger LOG = Logger.ofClass(MainActivity.class);

    // UI components
    private final LazyView<ListView> listView;
    private final LazyView<Toolbar> toolbar;

    public MainActivity() {
        super(R.layout.activity_main);
        var lazyViewFactory = new LazyViewFactory(this);
//        this.swipeRefresh = lazyViewFactory.createView(R.id.swipe_refresh);
        this.listView = lazyViewFactory.createView(R.id.list_view);
        this.toolbar = lazyViewFactory.createView(R.id.toolbar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
//
//        swipeRefresh.get().setOnRefreshListener(() -> {
//            Executors.newSingleThreadExecutor().execute(() -> {
//                stationsViewModel.refetchData();
//                swipeRefresh.get().setRefreshing(false);
//            });
//        });
    }
}