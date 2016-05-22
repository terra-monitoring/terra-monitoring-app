package terrariumwatch.datasource;

import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.view.View;
import android.widget.ToggleButton;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class ActivityDataSource extends AsyncTask<String, Void, String> {

    public static final String AUTHKEY = "terra5#";

    public static final String POST_PARAM_KEYVALUE_SEPARATOR = "=";
    public static final String POST_PARAM_SEPARATOR = "&";

    private String DESTINATION_METHOD = "";

    private View[] viewArr;
    private View view;
    private TextView textView;
    private Switch ventSwitch;
    private ToggleButton ventPower;
    private EditText editText;

    private URLConnection conn;

    public ActivityDataSource(){}

    public ActivityDataSource(View view){
        this.view = view;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            DESTINATION_METHOD = params[0];
            openConnection();
            return readResult();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Öffnet eine Verbindung {@link URLConnection}.
     * @throws IOException
     */
    private void openConnection() throws IOException{
        //StringBuffer für das zusammensetzen der URL
        StringBuffer dataBuffer = new StringBuffer();
        dataBuffer.append(URLEncoder.encode("authkey", "UTF-8"));
        dataBuffer.append(POST_PARAM_KEYVALUE_SEPARATOR);
        dataBuffer.append(URLEncoder.encode(AUTHKEY, "UTF-8"));
        dataBuffer.append(POST_PARAM_SEPARATOR);
        dataBuffer.append(URLEncoder.encode("method", "UTF-8"));
        dataBuffer.append(POST_PARAM_KEYVALUE_SEPARATOR);
        dataBuffer.append(URLEncoder.encode(DESTINATION_METHOD, "UTF-8"));

        //Adresse der PHP Schnittstelle für die Verbindung zur MySQL Datenbank
        URL url = new File("connect.php").toURI().toURL();
        conn = url.openConnection();
        conn.setDoOutput(true);
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(dataBuffer.toString());
        wr.flush();
    }

    /**
     * Ließt das Ergebnis aus der geöffneten Verbindung.
     * @return liefert ein String mit dem gelesenen Werten.
     * @throws IOException
     */
    private String readResult()throws IOException{
        String result = null;
        //Lesen der Rückgabewerte vom Server
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line = null;
        //Solange Daten bereitstehen werden diese gelesen.
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        if(!isBlank(result)) {
            String[] resultArr = result.split("\\|");
            String idStr = view.getResources().getResourceName(view.getId());
            if (DESTINATION_METHOD == "allEntrys") {
                switch (idStr) {
                    // TextView's of Overview-Fragment
                    case "last_entry_value":
                        this.textView = (TextView) view;
                        this.textView.setText(resultArr[0]);
                        break;
                    case "temp_top_value":
                        this.textView = (TextView) view;
                        this.textView.setText(resultArr[1] + " " + (char) 0x00B0 + "C");
                        break;
                    case "temp_mid_value":
                        this.textView = (TextView) view;
                        this.textView.setText(resultArr[2] + " " + (char) 0x00B0 + "C");
                        break;
                    case "temp_bottom_value":
                        this.textView = (TextView) view;
                        this.textView.setText(resultArr[3] + " " + (char) 0x00B0 + "C");
                        break;
                    case "humidity_value":
                        this.textView = (TextView) view;
                        this.textView.setText(resultArr[4] + " " + (char) 0x0025);
                        break;
                    // Switch of Vent for Mode
                    case "vent_switch":
                        this.ventSwitch = (Switch) view;
                        if (resultArr[0] == "true") {
                            this.ventSwitch.setChecked(false);
                        } else if (resultArr[0] == "false") {
                            this.ventSwitch.setChecked(true);
                        }
                        break;
                    case "vent_on_value":
                        this.editText = (EditText) view;
                        this.editText.setText(resultArr[1]);
                    case "vent_off_value":
                        this.editText = (EditText) view;
                        this.editText.setText(resultArr[2]);
                    // TextView's for Sunrise and Sunset
                    case "sunset_value":
                        this.textView = (TextView) view;
                        this.textView.setText(resultArr[0]);
                        break;
                    case "sunrise_value":
                        this.textView = (TextView) view;
                        this.textView.setText(resultArr[1]);
                        break;
                }
            }
        }
    }

    private boolean isBlank(String value){
        return value == null || value.trim().isEmpty();
    }
}
