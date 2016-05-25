package terrariumwatch.terrarium;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LiveFragment extends Fragment {
    // set url of live stream
    private String vidAddress = "http://pi-terra.ddnss.de:8080/?action=stream";

    /**
     * constructor
     */
    public LiveFragment() {
    }

    /**
     * create of new instance of live steam fragment
     * @return
     */
    public static LiveFragment newInstance() {
        LiveFragment fragment = new LiveFragment();
        return fragment;
    }

    /**
     * create view of live stream fragment
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_live, container, false);
        return rootView;
    }
}