package daluai.app.cpptmin.ui;

import static java.util.Objects.requireNonNull;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import daluai.app.cpptmin.R;
import daluai.app.cpptmin.api.Stop;
import daluai.app.cpptmin.dto.StationDto;

public class StationPanelAdapter extends ArrayAdapter<StationDto> {

    private static final int MAX_INCOMING_TRAINS_DISPLAYED = 100;
    private static final int NICER_YELLOW = Color.parseColor("#DAA520");

    private final Context context;

    public StationPanelAdapter(Context context, List<StationDto> stationDtos) {
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

        stationNameTextView.setText(stationDto.getStation().getDesignation());

        List<Stop> stops = stationDto.getNextTrains().getStationStops();

        if (stops == null || stops.isEmpty()) {
            nextTrainsTextView.setText("No upcoming trains");
        } else {
            nextTrainsTextView.setText(renderStops(stops));
        }

        return convertView;

    }

    private CharSequence renderStops(List<Stop> stops) {
        SpannableStringBuilder builder = new SpannableStringBuilder();

        for (int i = 0; i < Math.min(stops.size(), MAX_INCOMING_TRAINS_DISPLAYED); i++) {
            Stop stop = stops.get(i);
            builder.append(renderStop(stop));
            if (i < stops.size() - 1) {
                builder.append("\n");
            }
        }
        return builder;
    }

    private static CharSequence renderStop(Stop stop) {
        if (stop.getSuppression() != null) {
            return renderSuppression(stop);
        }
        if (stop.getDelay() != null && stop.getDelay() != 0) {
            return renderDelay(stop);
        }
        return renderTimeAndDestination(stop);
    }

    private static CharSequence renderSuppression(Stop stop) {
        requireNonNull(stop.getSuppression());

        CharSequence timeAndDestination = renderTimeAndDestination(stop);
        String text = timeAndDestination + "  " + "Suppressed";

        var span = new SpannableString(text);
        span.setSpan(new StrikethroughSpan(), 0, timeAndDestination.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new ForegroundColorSpan(Color.RED), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return span;
    }


    private static CharSequence renderDelay(Stop stop) {
        requireNonNull(stop.getDelay());

        int delay = stop.getDelay();
        CharSequence timeAndDestination = renderTimeAndDestination(stop);
        String minString = delay == 1 ? "min" : "mins";
        String text = timeAndDestination + "  " + delay + " " + minString + " delayed";

        var span = new SpannableString(text);
        span.setSpan(new ForegroundColorSpan(NICER_YELLOW), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return span;
    }

    private static CharSequence renderTimeAndDestination(Stop stop) {
        return stop.getDepartureTime() + " " + stop.getTrainDestination().getDesignation();
    }
}
