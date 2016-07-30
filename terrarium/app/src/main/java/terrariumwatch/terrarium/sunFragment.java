package terrariumwatch.terrarium;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import java.util.Calendar;

public class SunFragment extends Fragment {
    // create items
    private View rootView;
    private TextView sunrise, sunset;
    private TextView sunriseVal, sunsetVal;
    private Button btnSave;

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
        btnSave = (Button) rootView.findViewById(R.id.save_buttonSun);



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
                            sunriseVal.setText(responseObject.getString("sunrise") + " Uhr");
                            sunsetVal.setText(responseObject.getString("sunset") + " Uhr");
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
