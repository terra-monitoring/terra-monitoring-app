package terrariumwatch.terrarium;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class OverviewFragment extends Fragment {

    private TextView lastEntry, tempTop, tempMid, tempBottom, humidity,
            lastEntryValue, tempTopValue, tempMidValue, tempBottomValue, humidityValue;

    public OverviewFragment() {
    }

    public static OverviewFragment newInstance() {
        OverviewFragment fragment = new OverviewFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_overview, container, false);
        lastEntry = (TextView) rootView.findViewById(R.id.last_entry);
        lastEntryValue = (TextView) rootView.findViewById(R.id.last_entry_value);
        tempTop = (TextView) rootView.findViewById(R.id.temp_top);
        tempTopValue = (TextView) rootView.findViewById(R.id.temp_top_value);
        tempMid = (TextView) rootView.findViewById(R.id.temp_mid);
        tempMidValue = (TextView) rootView.findViewById(R.id.temp_mid_value);
        tempBottom = (TextView) rootView.findViewById(R.id.temp_bottom);
        tempBottomValue = (TextView) rootView.findViewById(R.id.temp_bottom_value);
        humidity = (TextView) rootView.findViewById(R.id.humidity);
        humidityValue = (TextView) rootView.findViewById(R.id.humidity_value);
        return rootView;
    }
}
