package terrariumwatch.terrarium;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class SunFragment extends Fragment {
    // create items
    private View rootView;
    private TextView sunrise, sunset, sunriseVal, sunsetVal, checkInput;
    private Button button;

    /**
     * constructor
     */
    public SunFragment() {
    }

    /**
     * create new instance of ventilator fragment
     * @return
     */
    public static SunFragment newInstance() {
        SunFragment fragment = new SunFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_sun, container, false);

        sunrise = (TextView) rootView.findViewById(R.id.sunrise);
        sunset = (TextView) rootView.findViewById(R.id.sunset);
        sunriseVal = (TextView) rootView.findViewById(R.id.sunrise_value);
        sunsetVal = (TextView) rootView.findViewById(R.id.sunset_value);
        button = (Button) rootView.findViewById(R.id.save_buttonSun);
        checkInput = (TextView) rootView.findViewById(R.id.checkInput);



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
                            sunriseVal.setText(responseObject.getString("sunrise"));
                            sunsetVal.setText(responseObject.getString("sunset"));
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
         * set listener for sunrise value time picker
         */
        sunriseVal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create calendar instance
                SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
                int hour = 0;
                try {
                    hour = dateFormat.parse(sunriseVal.getText().toString()).getHours();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                int minute = 0;
                try {
                    minute = dateFormat.parse(sunriseVal.getText().toString()).getMinutes();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                // creat time picker dialog
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    /**
                     * set listener for time set
                     * @param timePicker
                     * @param selectedHour
                     * @param selectedMinute
                     */
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        sunriseVal.setText(String.format("%02d:%02d", selectedHour, selectedMinute));

                    }
                }, hour, minute, true);// 24 hour time
                mTimePicker.setTitle("Sonnenaufgang wählen");
                mTimePicker.show();
            }
        });

        /**
         * set listener for sunset value time picker
         */
        sunsetVal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // creater calendar instance
                SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
                int hour = 0;
                try {
                    hour = dateFormat.parse(sunsetVal.getText().toString()).getHours();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                int minute = 0;
                try {
                    minute = dateFormat.parse(sunsetVal.getText().toString()).getMinutes();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                // create time picker dialog
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    /**
                     * set listener for time set
                     * @param timePicker
                     * @param selectedHour
                     * @param selectedMinute
                     */
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        sunsetVal.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                    }
                }, hour, minute, true);// 24 hour time
                mTimePicker.setTitle("Sonnenuntergang wählen");
                mTimePicker.show();
            }
        });

        /**
         * get Data if button pushed
         */
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userSunrise = sunriseVal.getText().toString();

                final String userSunset = sunsetVal.getText().toString();

                TelephonyManager telephonyManager = (TelephonyManager)getActivity().getSystemService(Context.TELEPHONY_SERVICE);
                String imei = telephonyManager.getDeviceId();

                final HashMap<String, String> params = new HashMap<String, String>();
                params.put("action", "set");
                params.put("imei",  imei);
                params.put("page", "sun");
                params.put("sunrise", userSunrise);
                params.put("sunset", userSunset);


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
