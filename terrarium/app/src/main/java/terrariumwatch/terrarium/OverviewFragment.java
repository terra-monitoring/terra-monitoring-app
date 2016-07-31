package terrariumwatch.terrarium;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OverviewFragment extends Fragment {
    // create textview's for overview fragment
    private TextView lastEntry, tempTop, tempMid, tempBottom, humidity,
            lastEntryValue, tempTopValue, tempMidValue, tempBottomValue, humidityValue;

    /**
     * constructor
     */
    public OverviewFragment() {
    }

    /**
     * create new instance of overview fragment
     * @return
     */
    public static OverviewFragment newInstance() {
        OverviewFragment fragment = new OverviewFragment();
        return fragment;
    }

    /**
     * create view of fragment
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
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

        /**
         * DB Connection to get data
         */
        // Instantiate the MySingleton.
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getContext());
        String url ="http://pi-terra.ddnss.de/terra/app/appIndex.php";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //paring data
                        try{
                            JSONObject responseObject = new JSONObject(response);
                            lastEntryValue.setText(responseObject.getString("time"));
                            tempTopValue.setText(responseObject.getString("s1") + " °C");
                            tempMidValue.setText(responseObject.getString("s2") + " °C");
                            tempBottomValue.setText(responseObject.getString("s3") + " °C");
                            humidityValue.setText(responseObject.getString("s4") + " %");

                        }catch(JSONException e1){
                            Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "get");

                return params;
            }
        };
        // Add the request to the MySingleton.
        queue.add(stringRequest);


        return rootView;
    }
}
