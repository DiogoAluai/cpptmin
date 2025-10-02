package daluai.app.cpptmin.ui;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import daluai.app.cpptmin.R;
import daluai.app.cpptmin.api.Station;

public class StationPanel extends ArrayAdapter<Station> {

    private final Context context;

    public StationPanel(Context context, List<Station> stations) {
        super(context, 0, stations);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_station, parent, false);
        }

        Station station = getItem(position);
        if (station == null) return convertView;

        TextView stationName = convertView.findViewById(R.id.station_name);
        TextView nextTrains = convertView.findViewById(R.id.next_trains);

        stationName.setText(station.getDesignation());

        // Example: join upcoming trains as "HH:mm - Destination"
        List<String> trains = List.of("asd","dsa", "and such");
        if (trains != null && !trains.isEmpty()) {
            nextTrains.setText(TextUtils.join("\n", trains));
        } else {
            nextTrains.setText("No upcoming trains");
        }

        return convertView;
        // todo: open modal with extra details
//        convertView.setOnClickListener(view -> startMessageActivity(station));

    }
//
//    private void startMessageActivity(Station station) {
//        String username = station.getPropertyString(PROP_USER_ALIAS);
//
//        Intent intent = new Intent(context, MessageActivity.class);
//        intent.putExtra(INTENT_MESSAGE_STATION_IP, station.getInet4Addresses()[0].getHostAddress());
//        intent.putExtra(INTENT_MESSAGE_USER, username);
//        context.startActivity(intent);
//    }
}
