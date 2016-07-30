package terrariumwatch.terrarium;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LuefterFragment extends Fragment {
    // create items
    private View rootView;
    private Switch ventSwitch;
    private TextView ventOn, ventOff;
    private EditText ventOnVal, ventOffVal;
    private Button btnSave;
    private ToggleButton tglPower;

    /**
     * constructor
     */
    public LuefterFragment() {
    }

    /**
     * create new instance of ventilator fragment
     * @return
     */
    public static LuefterFragment newInstance() {
        LuefterFragment fragment = new LuefterFragment();
        return fragment;
    }

    /**
     * create view
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_luefter, container, false);

        ventSwitch = (Switch) rootView.findViewById(R.id.vent_switch);
        ventOn = (TextView) rootView.findViewById(R.id.vent_on);
        ventOff = (TextView) rootView.findViewById(R.id.vent_off);
        ventOnVal = (EditText) rootView.findViewById(R.id.vent_on_value);
        ventOffVal = (EditText) rootView.findViewById(R.id.vent_off_value);
        btnSave = (Button) rootView.findViewById(R.id.save_buttonLuefter);
        tglPower = (ToggleButton) rootView.findViewById(R.id.power_toggle);


        /**
         * DB Connection to get data
         */
        // Instantiate the MySingleton.
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getContext());
        String url ="http://pi-terra.ddnss.de/terra/app/appIndex.php";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //paring data
                        try{
                            JSONObject responseObject = new JSONObject(response);
                            ventOnVal.setText(responseObject.getString("max") + " °C");
                            ventOffVal.setText(responseObject.getString("min") + " °C");
                            ventSwitch.setChecked(responseObject.getBoolean("auto_mod"));
                            tglPower.setChecked(responseObject.getBoolean("status"));
                        }catch(JSONException e1){
                            Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_LONG).show();
            }
        });
        // Add the request to the MySingleton.
        queue.add(stringRequest);



        /**
         * set listener for ventilator mode switch
         */
        ventSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // vent is manually controlled
                    ventOn.setVisibility(View.VISIBLE);
                    ventOnVal.setVisibility(View.VISIBLE);
                    ventOff.setVisibility(View.VISIBLE);
                    ventOffVal.setVisibility(View.VISIBLE);
                    btnSave.setVisibility(View.VISIBLE);
                    tglPower.setVisibility(View.VISIBLE);
                } else {
                    // vent is automatically controlled
                    ventOn.setVisibility(View.VISIBLE);
                    ventOnVal.setVisibility(View.VISIBLE);
                    ventOff.setVisibility(View.VISIBLE);
                    ventOffVal.setVisibility(View.VISIBLE);
                    btnSave.setVisibility(View.VISIBLE);
                    tglPower.setVisibility(View.GONE);
                }
            }
        });

        /**
         * set listener for power toggle button
         */
        tglPower.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton button, boolean isChecked) {
                // if toggle button is checked  = "On"
                if (isChecked) {
                    Toast.makeText(getActivity(), "Lüfter wird eingeschaltet", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Lüfter wird ausgeschaltet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /**
         * set listener for save button
         */
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });







        return rootView;
    }
}
