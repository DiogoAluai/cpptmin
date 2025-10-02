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
import java.util.stream.Collectors;

import daluai.app.cpptmin.R;
import daluai.app.cpptmin.api.Stop;
import daluai.app.cpptmin.dto.StationDto;

public class StationPanel extends ArrayAdapter<StationDto> {

    private final Context context;

    public StationPanel(Context context, List<StationDto> stationDtos) {
        super(context, 0, stationDtos);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_station, parent, false);
        }

        StationDto stationDto = getItem(position);
        if (stationDto == null) return convertView;

        TextView stationNameTextView = convertView.findViewById(R.id.station_name);
        TextView nextTrainsTextView = convertView.findViewById(R.id.next_trains);

        stationNameTextView.setText(stationDto.getDesignation());

        // Example: join upcoming trains as "HH:mm - Destination"
        List<String> incomingTrains = stationDto.getNextTrains().getStationStops().stream()
                .map(stop -> renderStop(stop))
                .limit(2)
                .collect(Collectors.toList());
        if (incomingTrains != null && !incomingTrains.isEmpty()) {
            nextTrainsTextView.setText(TextUtils.join("\n", incomingTrains));
        } else {
            nextTrainsTextView.setText("No upcoming trains");
        }

        return convertView;
        // todo: open modal with extra details
//        convertView.setOnClickListener(view -> startMessageActivity(nextTrains));

    }

    @NonNull
    private static String renderStop(Stop stop) {
        return stop.getArrivalTime() + " " + stop.getTrainDestination().getDesignation();
    }
//
//    private void startMessageActivity(NextTrains station) {
//        String username = station.getPropertyString(PROP_USER_ALIAS);
//
//        Intent intent = new Intent(context, MessageActivity.class);
//        intent.putExtra(INTENT_MESSAGE_STATION_IP, station.getInet4Addresses()[0].getHostAddress());
//        intent.putExtra(INTENT_MESSAGE_USER, username);
//        context.startActivity(intent);
//    }
}
