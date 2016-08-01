package terrariumwatch.terrarium;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
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

import java.util.HashMap;
import java.util.Map;

public class LuefterFragment extends Fragment {
    // create items
    private View rootView;
    private Switch ventSwitch;
    private TextView ventOn, ventOff, checkInput, unit1, unit2;
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
        checkInput = (TextView) rootView.findViewById(R.id.checkInput);
        unit1 = (TextView) rootView.findViewById(R.id.textViewUnit1);
        unit2 = (TextView) rootView.findViewById(R.id.textViewUnit2);




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
                            ventOnVal.setText(responseObject.getString("max"));
                            ventOffVal.setText(responseObject.getString("min"));
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



        /**
         * set listener for ventilator mode switch
         */
        ventSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // vent is automatically controlled
                    ventOn.setVisibility(View.VISIBLE);
                    ventOnVal.setVisibility(View.VISIBLE);
                    ventOff.setVisibility(View.VISIBLE);
                    ventOffVal.setVisibility(View.VISIBLE);
                    btnSave.setVisibility(View.VISIBLE);
                    tglPower.setVisibility(View.GONE);
                    unit1.setVisibility(View.VISIBLE);
                    unit2.setVisibility(View.VISIBLE);
                    tglPower.setChecked(false);
                } else {
                    // vent is manually controlled
                    ventOn.setVisibility(View.GONE);
                    ventOnVal.setVisibility(View.GONE);
                    ventOff.setVisibility(View.GONE);
                    ventOffVal.setVisibility(View.GONE);
                    btnSave.setVisibility(View.VISIBLE);
                    tglPower.setVisibility(View.VISIBLE);
                    unit1.setVisibility(View.GONE);
                    unit2.setVisibility(View.GONE);
                }
            }
        });


        /**
         * set listener for save button
         */
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userAutomod = null;
                if(ventSwitch.isChecked()){
                    userAutomod = "true";
                } else {
                    userAutomod = "false";
                }

                String userManuel = null;
                if(tglPower.isChecked()){
                    userManuel = "true";
                } else {
                    userManuel = "false";
                }

                final String userMax = ventOnVal.getText().toString();

                final String userMin = ventOffVal.getText().toString();

                TelephonyManager telephonyManager = (TelephonyManager)getActivity().getSystemService(Context.TELEPHONY_SERVICE);
                String imei = telephonyManager.getDeviceId();


                final HashMap<String, String> params = new HashMap<String, String>();
                params.put("action", "set");
                params.put("imei",  imei);
                params.put("page", "luefter");
                params.put("automod", userAutomod);
                params.put("manuel", userManuel);
                params.put("max", userMax);
                params.put("min", userMin);


                final String urlSet ="http://pi-terra.ddnss.de/terra/app/appIndex.php";
                StringRequest sendRequest = new StringRequest(Request.Method.POST, urlSet, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        checkInput.setText(response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        checkInput.setText(error.toString());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {

                        return params;
                    }
                };
                // Add the request to the MySingleton.
                com.android.volley.RequestQueue queue = Volley.newRequestQueue(getContext());
                queue.add(sendRequest);
            }
        });







        return rootView;
    }
}
