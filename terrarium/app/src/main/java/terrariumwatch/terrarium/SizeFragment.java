package terrariumwatch.terrarium;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SizeFragment extends Fragment{
    //create items
    private View rootView;
    private TextView dateText, gewichtText, laengeText, date, gewicht, laenge, checkInput;
    private Button button;
    /**
     * constructor
     */
    public SizeFragment() {
    }

    /**
     * create new instance of feed fragment
     * @return
     */
    public static SizeFragment newInstance() {
        SizeFragment fragment = new SizeFragment();
        return fragment;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_size, container, false);

        dateText = (TextView) rootView.findViewById(R.id.date);
        gewichtText = (TextView) rootView.findViewById(R.id.gewicht);
        laengeText = (TextView) rootView.findViewById(R.id.laenge);

        date = (TextView) rootView.findViewById(R.id.datePicker);
        gewicht = (TextView) rootView.findViewById(R.id.gewichtEditText);
        laenge = (TextView) rootView.findViewById(R.id.laengeEditText);
        button = (Button) rootView.findViewById(R.id.sizeSaveButton);
        checkInput = (TextView) rootView.findViewById(R.id.checkInput);

        /**
         * get current date
         */
        long currentDate = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        date.setText(sdf.format(currentDate));



        /**
         * set listener for date picker
         */

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create calendar instance
                Calendar mcurrentDate = Calendar.getInstance();
                int year = mcurrentDate.get(Calendar.YEAR);
                int month = mcurrentDate.get(Calendar.MONTH);
                int day = mcurrentDate.get(Calendar.DATE);
                // creat date picker dialog
                DatePickerDialog mDatePicker;
                mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    /**
                     * set listener for date set
                     * @param datePicker
                     * @param selectedYear
                     * @param selectedMonth
                     * @param selectedDay
                     */
                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                    date.setText(selectedDay + "." + (selectedMonth+1) + "." + selectedYear);
                    }
                }, year, month, day);
                mDatePicker.show();
            }
        });


        /**
         * get Data if button pushed
         */
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userDate = date.getText().toString();

                final String userGewicht = gewicht.getText().toString();

                final String userLaenge = laenge.getText().toString();

                TelephonyManager telephonyManager = (TelephonyManager)getActivity().getSystemService(Context.TELEPHONY_SERVICE);
                String imei = telephonyManager.getDeviceId();

                final HashMap<String, String> params = new HashMap<String, String>();
                params.put("action", "set");
                params.put("imei",  imei);
                params.put("page", "size");
                params.put("date", userDate);
                params.put("gewicht", userGewicht);
                params.put("laenge", userLaenge);


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
