package terrariumwatch.terrarium;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LiveFragment extends Fragment {
    private String vidAddress = "http://pi-terra.ddnss.de:8080/?action=stream";

    public LiveFragment() {
    }

    public static LiveFragment newInstance() {
        LiveFragment fragment = new LiveFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_live, container, false);

        //TODO: Stream einbinden

        return rootView;
    }
}
