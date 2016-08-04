package terrariumwatch.terrarium;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class CamFragment extends Fragment {
    //create items
    private View rootView;
    private TextView checkInput;
    private ToggleButton camToogle;

    /**
     * constructor
     */
    public CamFragment() {
    }

    /**
     * create new instance of cam fragment
     * @return
     */
    public static CamFragment newInstance() {
        CamFragment fragment = new CamFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cam, container, false);
        camToogle = (ToggleButton) rootView.findViewById(R.id.cam_toggle);
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
                            camToogle.setChecked(responseObject.getBoolean("status"));


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
         * set listener for save button
         */
        camToogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userCam = null;
                if(camToogle.isChecked()){
                    userCam = "true";
                } else {
                    userCam = "false";
                }


                TelephonyManager telephonyManager = (TelephonyManager)getActivity().getSystemService(Context.TELEPHONY_SERVICE);
                String imei = telephonyManager.getDeviceId();

                final HashMap<String, String> params = new HashMap<String, String>();
                params.put("action", "set");
                params.put("imei",  imei);
                params.put("page", "camSetting");
                params.put("cam", userCam);



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
