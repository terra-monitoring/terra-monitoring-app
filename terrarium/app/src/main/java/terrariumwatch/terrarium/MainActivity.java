package terrariumwatch.terrarium;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    // boolean "authenticated" for user login
    private boolean authenticated = false;

    private String user, pass;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    /**
     * create Toolbar, SectionsPagerAdapter, ViewPager and Layout
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    /**
     * create options menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * prepare options menu with items "Aktualsieren" and "Login"
     * set enabled status of items by authenticated
     * @param menu
     * @return
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem refreshOption = menu.findItem(R.id.action_refresh);
        MenuItem loginOption = menu.findItem(R.id.action_login);
        // is user is logged in successfully
        if (authenticated) {
            // disbale login button and enable refresh button
            loginOption.setEnabled(false);
            refreshOption.setEnabled(true);
        } else {
            refreshOption.setEnabled(true);
            refreshOption.setEnabled(false);
        }
        return true;
    }

    /**
     * set up actions for login and refresh button
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // get id of selected options item
        int id = item.getItemId();

        // if login button is selected
        if (id == R.id.action_login) {
            // create login dialog
            final Dialog login = new Dialog(this);
            login.setContentView(R.layout.login_dialog);

            // create login and cancel button
            Button btnLogin = (Button) login.findViewById(R.id.btnLogin);
            Button btnCancel = (Button) login.findViewById(R.id.btnCancel);

            // create text field to enter username and password
            final EditText txtUsername = (EditText) login.findViewById(R.id.txtUsername);
            final EditText txtPassword = (EditText) login.findViewById(R.id.txtPassword);

            // if login button is pressed
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // set username and password input
                    user = txtUsername.getText().toString();
                    pass = txtPassword.getText().toString();
                    // execute login task with username and password
                    new AsyncLogin().execute(user,pass);
                    // close login dialog
                    login.dismiss();
                }
            });

            // if cancel button is pressed
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // close login dialog
                    login.dismiss();
                }
            });
            login.show();
            return true;
        }
        // if refresh button is pressed
        else if (id == R.id.action_refresh) {
            // show Toast with pressed button info
            Toast.makeText(MainActivity.this, "Daten werden aktualisiert", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * check position of switched tab
         * @param position
         * @return
         */
        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    // tab at position 0 is overview fragment
                    return OverviewFragment.newInstance();
                case 1:
                    // tab at position 1 is ventilator fragment
                    return VentFragment.newInstance();
                case 2:
                    // tab at position 2 is live stream fragment
                    return LiveFragment.newInstance();
            }
            return null;
        }

        /**
         * set maximum tabs to 3
         * @return
         */
        @Override
        public int getCount() {
            return 3;
        }

        /**
         * set page titles for tabs
         * @param position
         * @return
         */
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Überblick";
                case 1:
                    return "Lüfter";
                case 2:
                    return "Live";
            }
            return null;
        }
    }

    /**
     * login class as asynchronous task
     */
    private class AsyncLogin extends AsyncTask<String, String, String> {
        // create progress dialog for login process
        ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);
        // http connection with url
        HttpURLConnection conn;
        URL url = null;

        /**
         * show progress dialog when login started
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        /**
         * main method for connection process and reading data
         * @param params
         * @return
         */
        @Override
        protected String doInBackground(String... params) {
            try {
                // set url for login php script
                url = new URL("/data/user/0/terrariumwatch.datasource/login.php");

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("username", params[0])
                        .appendQueryParameter("password", params[1]);
                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();
            } catch (IOException e1) {
                e1.printStackTrace();
                return "exception";
            }
            try {
                int response_code = conn.getResponseCode();
                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {
                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    // Pass data to onPostExecute method
                    return(result.toString());
                } else {
                    return("unsuccessful");
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                // disconnect http connection
                conn.disconnect();
            }
        }

        /**
         * get data of doInBackground
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {
            //this method will be running on UI thread
            pdLoading.dismiss();
            // check if login and reading data was successfull
            if(result.equalsIgnoreCase("true")) {
                // show result of reading data
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
            } else if (result.equalsIgnoreCase("false")){
                // If username and password does not match display a error message
                Toast.makeText(MainActivity.this, "Invalid user or password", Toast.LENGTH_SHORT);
            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {
                Toast.makeText(MainActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_SHORT);
            }
        }
    }
}
