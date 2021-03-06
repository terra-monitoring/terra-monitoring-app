package terrariumwatch.terrarium;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FeedFragment extends Fragment {
    //create items
    private View rootView;
    private TextView date, dateText, fastentagText, futterText, mengeText, vitamineText, calciumText, bemerkungText, mengeAnzeige, checkInput;
    private CheckBox fastentag, vitamine, calcium;
    private Spinner futter;
    private EditText bemerkung;
    private Button button;
    private DiscreteSeekBar menge;


    /**
     * constructor
     */
    public FeedFragment() {
    }

    /**
     * create new instance of feed fragment
     * @return
     */
    public static FeedFragment newInstance() {
        FeedFragment fragment = new FeedFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_feed, container, false);

        dateText = (TextView) rootView.findViewById(R.id.date);
        fastentagText = (TextView) rootView.findViewById(R.id.fastentag);
        futterText = (TextView) rootView.findViewById(R.id.futter);
        mengeText = (TextView) rootView.findViewById(R.id.menge);
        vitamineText = (TextView) rootView.findViewById(R.id.vitamine);
        calciumText = (TextView) rootView.findViewById(R.id.calcium);
        bemerkungText = (TextView) rootView.findViewById(R.id.bemerkung);


        date = (TextView) rootView.findViewById(R.id.datePicker);
        fastentag = (CheckBox) rootView.findViewById(R.id.fastentagCheckBox);
        futter = (Spinner) rootView.findViewById(R.id.futterSpinner);
        mengeAnzeige = (TextView) rootView.findViewById(R.id.mengeAnzeige);
        menge = (DiscreteSeekBar) rootView.findViewById(R.id.mengeSeekBar);
        vitamine = (CheckBox) rootView.findViewById(R.id.vitamineCheckBox);
        calcium = (CheckBox) rootView.findViewById(R.id.calciumCheckBox);
        bemerkung = (EditText) rootView.findViewById(R.id.bemerkungTextField);
        button = (Button) rootView.findViewById(R.id.feedSaveButton);
        checkInput = (TextView) rootView.findViewById(R.id.checkInput);

        /**
         * get current date
         */
        long currentDate = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        date.setText(sdf.format(currentDate));


        /**
         * DB Connection to get data
         */
        // Instantiate the MySingleton.
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getContext());
        final String urlGet ="http://pi-terra.ddnss.de/terra/app/appIndex.php";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlGet,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //paring data
                        try{
                            JSONObject responseObject = new JSONObject(response);

                            String futterString = responseObject.getString("food");


                            List<String> futterArrayList = Arrays.asList(futterString.split(","));

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                                    android.R.layout.simple_spinner_item, futterArrayList);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            futter.setAdapter(adapter);


                        }catch(JSONException e1){
                            Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_LONG).show();
                }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "get");

                return params;
            }
        };
        // Add the request to the MySingleton.
        queue.add(stringRequest);





        /**
         * set listener for fastentag
         */
        fastentag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // fastentag true
                    dateText.setVisibility(View.VISIBLE);
                    date.setVisibility(View.VISIBLE);
                    fastentagText.setVisibility(View.VISIBLE);
                    fastentag.setVisibility(View.VISIBLE);
                    futterText.setVisibility(View.GONE);
                    futter.setVisibility(View.GONE);
                    mengeAnzeige.setVisibility(View.GONE);
                    mengeText.setVisibility(View.GONE);
                    menge.setVisibility(View.GONE);
                    vitamineText.setVisibility(View.GONE);
                    vitamine.setVisibility(View.GONE);
                    calciumText.setVisibility(View.GONE);
                    calcium.setVisibility(View.GONE);
                    bemerkungText.setVisibility(View.VISIBLE);
                    bemerkung.setVisibility(View.VISIBLE);
                    button.setVisibility(View.VISIBLE);
                } else {
                    // fastentag false
                    dateText.setVisibility(View.VISIBLE);
                    date.setVisibility(View.VISIBLE);
                    fastentagText.setVisibility(View.VISIBLE);
                    fastentag.setVisibility(View.VISIBLE);
                    futterText.setVisibility(View.VISIBLE);
                    futter.setVisibility(View.VISIBLE);
                    mengeAnzeige.setVisibility(View.VISIBLE);
                    mengeText.setVisibility(View.VISIBLE);
                    menge.setVisibility(View.VISIBLE);
                    vitamineText.setVisibility(View.VISIBLE);
                    vitamine.setVisibility(View.VISIBLE);
                    calciumText.setVisibility(View.VISIBLE);
                    calcium.setVisibility(View.VISIBLE);
                    bemerkungText.setVisibility(View.VISIBLE);
                    bemerkung.setVisibility(View.VISIBLE);
                    button.setVisibility(View.VISIBLE);
                }
            }
        });

        /**
         * set listener for menge
         */
        menge.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                mengeAnzeige.setText(String.valueOf(value));
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {


            }
        });

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

                String userFastentag = null;
                if(fastentag.isChecked()){
                    userFastentag = "true";
                } else {
                    userFastentag = "false";
                }

                final String userFutter = futter.getSelectedItem().toString();

                final String userMenge = mengeAnzeige.getText().toString();

                String userVitamine = null;
                if(vitamine.isChecked()){
                    userVitamine = "true";
                } else {
                    userVitamine = "false";
                }

                String userCalcium = null;
                if(calcium.isChecked()){
                    userCalcium = "true";
                } else {
                    userCalcium = "false";
                }

                final String userBemerkung = bemerkung.getText().toString();

                TelephonyManager telephonyManager = (TelephonyManager)getActivity().getSystemService(Context.TELEPHONY_SERVICE);
                String imei = telephonyManager.getDeviceId();

                final HashMap<String, String> params = new HashMap<String, String>();
                params.put("action", "set");
                params.put("imei",  imei);
                params.put("page", "feed");
                params.put("date", userDate);
                params.put("fastentag", userFastentag);
                params.put("futter", userFutter);
                params.put("menge", userMenge);
                params.put("vitamine", userVitamine);
                params.put("calcium", userCalcium);
                params.put("bemerkung", userBemerkung);

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