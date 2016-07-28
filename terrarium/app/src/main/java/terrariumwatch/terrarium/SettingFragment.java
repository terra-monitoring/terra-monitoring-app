package terrariumwatch.terrarium;

import android.app.TimePickerDialog;
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
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class SettingFragment extends Fragment {
    // create items
    private View rootView;
    private Switch ventSwitch;
    private TextView sunrise, sunset, ventOn, ventOff;
    private TextView sunriseVal, sunsetVal;
    private EditText ventOnVal, ventOffVal;
    private Button btnSave;
    private ToggleButton tglPower;

    /**
     * constructor
     */
    public SettingFragment() {
    }

    /**
     * create new instance of ventilator fragment
     * @return
     */
    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
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
        rootView = inflater.inflate(R.layout.fragment_setting, container, false);

        ventSwitch = (Switch) rootView.findViewById(R.id.vent_switch);
        sunrise = (TextView) rootView.findViewById(R.id.sunrise);
        sunset = (TextView) rootView.findViewById(R.id.sunset);
        ventOn = (TextView) rootView.findViewById(R.id.vent_on);
        ventOff = (TextView) rootView.findViewById(R.id.vent_off);
        sunriseVal = (TextView) rootView.findViewById(R.id.sunrise_value);
        sunsetVal = (TextView) rootView.findViewById(R.id.sunset_value);
        ventOnVal = (EditText) rootView.findViewById(R.id.vent_on_value);
        ventOffVal = (EditText) rootView.findViewById(R.id.vent_off_value);
        btnSave = (Button) rootView.findViewById(R.id.save_button);
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
                            sunriseVal.setText(responseObject.getString("sunrise"));
                            sunsetVal.setText(responseObject.getString("sunset"));
                            ventOnVal.setText(responseObject.getString("max"));
                            ventOffVal.setText(responseObject.getString("min"));
                            /*.setText(responseObject.getString("auto_mod"));
                            .setText(responseObject.getString("status"));*/
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
                    sunrise.setVisibility(View.GONE);
                    sunriseVal.setVisibility(View.GONE);
                    sunset.setVisibility(View.GONE);
                    sunsetVal.setVisibility(View.GONE);
                    ventOn.setVisibility(View.GONE);
                    ventOnVal.setVisibility(View.GONE);
                    ventOff.setVisibility(View.GONE);
                    ventOffVal.setVisibility(View.GONE);
                    btnSave.setVisibility(View.GONE);
                    tglPower.setVisibility(View.VISIBLE);
                } else {
                    // vent is automatically controlled
                    sunrise.setVisibility(View.VISIBLE);
                    sunriseVal.setVisibility(View.VISIBLE);
                    sunset.setVisibility(View.VISIBLE);
                    sunsetVal.setVisibility(View.VISIBLE);
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

        /**
         * set listener for sunrise value time picker
         */
        sunriseVal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create calendar instance
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
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
                        sunriseVal.setText(selectedHour + ":" + selectedMinute);
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
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
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
                        sunsetVal.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);// 24 hour time
                mTimePicker.setTitle("Sonnenuntergang wählen");
                mTimePicker.show();
            }
        });






        return rootView;
    }
}
