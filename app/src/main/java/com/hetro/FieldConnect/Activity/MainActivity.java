package com.hetro.FieldConnect.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.hetro.FieldConnect.DtoController.LoginStatus;
import com.hetro.FieldConnect.DtoController.UserDetails;
import com.hetro.FieldConnect.Util.ApplicationConstants;
import com.hetro.FieldConnect.Util.JSONParser;
import com.hetro.FieldConnect.Util.NetworkConnection;
import com.hetro.FieldConnect.Util.TelephonyInfo;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends Activity implements ConnectionCallbacks,
        OnConnectionFailedListener, LocationListener {

    public static final String MyPREFERENCES = "FieldConnectLogin" ;
    public static final String userNameKey = "userNameKey";
    public static final String passwordKey = "passwordKey";
    public static final String loginstatusKey = "loginstatusKey";

    // AIzaSyBSu1M8t9f8RSNZ1RuVSDKtziICOxQt9KE

    EditText usernameEdittext,passwordEdittext;
    Button submitButton;
    TelephonyInfo telephonyInfo;
    String imsiSIM1=null,imsiSIM2=null;
    NetworkConnection networkConnection;
    String userName,passWord;
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    JSONArray user = null;
    SharedPreferences sharedpreferences;
    private static final String TAG = MainActivity.class.getSimpleName();
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private Location mLastLocation;
    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;
    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;
    private LocationRequest mLocationRequest;
    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters
    // UI elements
    private TextView lblLocation;
    private Button btnShowLocation, btnStartLocationUpdates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEdittext = (EditText)findViewById(R.id.username_edittext);
        passwordEdittext = (EditText)findViewById(R.id.password_edittext);
        submitButton = (Button)findViewById(R.id.submit_button);

        networkConnection = new NetworkConnection(this);
        telephonyInfo = TelephonyInfo.getInstance(this);
        imsiSIM1 = telephonyInfo.getImsiSIM1();
        imsiSIM2 = telephonyInfo.getImsiSIM2();

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();

            createLocationRequest();
        }

        try {

//            Intent intent = getIntent();
//            if (intent != null) {
//                String exit= intent.getStringExtra("exit");
//
//                if (exit.equals("exit")) {
//                   finish();
//                }
//            }

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                String exit = extras.getString("exit");
                if (exit.equals("exit")) {
                    finish();
                }
            }

        }catch (Exception error){
            //    Log.e("login--",error.toString());
        }


        if (sharedpreferences.contains(userNameKey)) {
            usernameEdittext.setText(sharedpreferences.getString(userNameKey, ""));
        }
        if (sharedpreferences.contains(passwordKey)) {
            passwordEdittext.setText(sharedpreferences.getString(passwordKey, ""));

        }
        login();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (networkConnection.isNetworkAvailable()) {
                    userName = usernameEdittext.getText().toString();
                    passWord = passwordEdittext.getText().toString();
                    if(userName.length()==0){
                        Toast.makeText(MainActivity.this, "Please Enter Username", Toast.LENGTH_LONG).show();
                    }
                    else if(passWord.length()==0){
                        Toast.makeText(MainActivity.this, "Please Enter Password", Toast.LENGTH_LONG).show();
                    }
                    else {
                        try {
                            new CheckValidUser().execute();
                        }
                        catch (Exception e){
                        }
                    }
                }
                else{
                    Toast.makeText(MainActivity.this,
                            "Network is not Available. Please Check Your Internet Connection ",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void login(){
        try {


            if (networkConnection.isNetworkAvailable()) {

                int loginStatus = 0;

                if (sharedpreferences.contains(userNameKey)) {
                    usernameEdittext.setText(sharedpreferences.getString(userNameKey, ""));
                }

                if (sharedpreferences.contains(passwordKey)) {
                    passwordEdittext.setText(sharedpreferences.getString(passwordKey, ""));
                }

                if (sharedpreferences.contains(loginstatusKey)) {
                    //  loginPasswordEdittext.setText(sharedpreferences.getString(passwordKey, ""));
                    loginStatus = sharedpreferences.getInt(loginstatusKey, 0);
                }

                if (usernameEdittext.getText().toString() != null && passwordEdittext.getText().toString() != null) {

                    userName = usernameEdittext.getText().toString();
                    passWord = passwordEdittext.getText().toString();


                    if (userName.length() == 0) {
                        //   Toast.makeText(LoginActivity.this, "Please Enter Username", Toast.LENGTH_LONG).show();
                    } else if (passWord.length() == 0) {
                        //  Toast.makeText(LoginActivity.this, "Please Enter Password", Toast.LENGTH_LONG).show();
                    } else {

                        //   int loginStatus = LoginStatus.getInstance().getLoginStatus();

                        if (loginStatus == 0) {

                            new CheckValidUser1().execute();

                        }
                    }
                }
            } else {

                Toast.makeText(MainActivity.this,
                        "Network is not Available. Please Check Your Internet Connection ",
                        Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e){
            //    Log.e("login error",e.toString());
        }
    }

    class CheckValidUser1 extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Login...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected JSONObject doInBackground(String... args) {

            JSONObject json = null;

            try {

                if (networkConnection.isNetworkAvailable()) {
                    Log.e("network1===", networkConnection.isNetworkAvailable() + "");

                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("username", userName));
                    params.add(new BasicNameValuePair("password", passWord));
                    params.add(new BasicNameValuePair("imsi1", imsiSIM1));
                    params.add(new BasicNameValuePair("imsi2", imsiSIM2));

                    json = jsonParser.makeHttpRequest(ApplicationConstants.url_validate_user1,
                            "POST", params);
                    Log.e("json--",json.toString());
                }
            }
            catch (Exception e){
            }
            return json;
        }

        protected void onPostExecute(JSONObject json) {

            try {
                if ((pDialog != null) && pDialog.isShowing()) {
                    pDialog.dismiss();
                }

            } catch (final IllegalArgumentException e) {

            } catch (final Exception e) {

            } finally {
                pDialog = null;
            }

            try {
                if (json == null) {

                    Toast.makeText(MainActivity.this,
                            "Invalid User Name and Password ",
                            Toast.LENGTH_SHORT).show();
                }
                else {

                    int success = json.getInt(ApplicationConstants.TAG_SUCCESS);
                    if (success == 1) {

                        LoginStatus.getInstance().setUsername(userName);
                        LoginStatus.getInstance().setPassword(passWord);

                        SharedPreferences.Editor editor = sharedpreferences.edit();

                        editor.putString(userNameKey, userName);
                        editor.putString(passwordKey, passWord);
                        editor.putInt(loginstatusKey, 0);

                        editor.commit();

                        user = json.getJSONArray(ApplicationConstants.TAG_USER);


                        for (int i = 0; i < user.length(); i++) {
                            JSONObject c = user.getJSONObject(i);
                            String userId = c.getString(ApplicationConstants.TAG_USER_ID);
                            String userName = c.getString(ApplicationConstants.TAG_USERNAME);
                            String userCircle = c.getString(ApplicationConstants.TAG_USER_CIRCLE);
                            String groupIndex = c.getString(ApplicationConstants.TAG_GROUP_INDEX);
                            UserDetails.getInstance().setUsername(userName);
                            UserDetails.getInstance().setId(userId);
                            UserDetails.getInstance().setCircle(userCircle);
                            UserDetails.getInstance().setGroupIndex(groupIndex);

                            Intent intent = new Intent(MainActivity.this,DashBoardActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }

                    } else if (success == 0) {
                        Toast.makeText(MainActivity.this,
                                "Please Enter Valid User Name and Password ",
                                Toast.LENGTH_SHORT).show();
                        usernameEdittext.setText("");
                        passwordEdittext.setText("");
                    } else if (success == 2) {
                    }
                }

            } catch (JSONException e) {

                Log.e("eee----",e.toString());
            }
            catch (Exception e){

                Log.e("ee--",e.toString());

            }

        }
    }

    class CheckValidUser extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Login...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected JSONObject doInBackground(String... args) {

            JSONObject json = null;

            try {

                if (networkConnection.isNetworkAvailable()) {
                    Log.e("network1===", networkConnection.isNetworkAvailable() + "");

                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("username", userName));
                    params.add(new BasicNameValuePair("password", passWord));
                    params.add(new BasicNameValuePair("imsi1", imsiSIM1));
                    params.add(new BasicNameValuePair("imsi2", imsiSIM2));

                    json = jsonParser.makeHttpRequest(ApplicationConstants.url_validate_user,
                            "POST", params);


                    Log.e("login---",json.toString());
                }
            }
            catch (Exception e){
            }
            return json;
        }

        protected void onPostExecute(JSONObject json) {

            try {
                if ((pDialog != null) && pDialog.isShowing()) {
                    pDialog.dismiss();
                }

            } catch (final IllegalArgumentException e) {

            } catch (final Exception e) {

            } finally {
                pDialog = null;
            }

            try {
                if (json == null) {

                    Toast.makeText(MainActivity.this,
                            "Invalid User Name and Password ",
                            Toast.LENGTH_SHORT).show();
                }
                else {

                    int success = json.getInt(ApplicationConstants.TAG_SUCCESS);
                    if (success == 1) {


                        LoginStatus.getInstance().setUsername(userName);
                        LoginStatus.getInstance().setPassword(passWord);

                        SharedPreferences.Editor editor = sharedpreferences.edit();

                        editor.putString(userNameKey, userName);
                        editor.putString(passwordKey, passWord);
                        editor.putInt(loginstatusKey, 0);

                        editor.commit();

                        user = json.getJSONArray(ApplicationConstants.TAG_USER);


                        for (int i = 0; i < user.length(); i++) {
                            JSONObject c = user.getJSONObject(i);
                            String userId = c.getString(ApplicationConstants.TAG_USER_ID);
                            String userName = c.getString(ApplicationConstants.TAG_USERNAME);
                            String userCircle = c.getString(ApplicationConstants.TAG_USER_CIRCLE);
                            String groupIndex = c.getString(ApplicationConstants.TAG_GROUP_INDEX);
                            UserDetails.getInstance().setUsername(userName);
                            UserDetails.getInstance().setId(userId);
                            UserDetails.getInstance().setCircle(userCircle);
                            UserDetails.getInstance().setGroupIndex(groupIndex);
                            Intent intent = new Intent(MainActivity.this,DashBoardActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }

                    } else if (success == 0) {
                        Toast.makeText(MainActivity.this,
                                "Please Enter Valid User Name and Password ",
                                Toast.LENGTH_SHORT).show();
                        usernameEdittext.setText("");
                        passwordEdittext.setText("");
                    } else if (success == 2) {
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        try {

            if (mGoogleApiClient != null) {
                mGoogleApiClient.connect();
            }
        }catch (Exception e){

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {

            checkPlayServices();

            // Resuming the periodic location updates
            if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
                startLocationUpdates();
            }
        }
        catch (Exception e){

        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        try {
            if (mGoogleApiClient.isConnected()) {
                mGoogleApiClient.disconnect();
            }
        }
        catch (Exception e){

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            stopLocationUpdates();
        }
        catch (Exception e){

        }
    }

    /**
     * Method to display the location on UI
     * */
    private void displayLocation() {

        try {

            mLastLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);

            if (mLastLocation != null) {
                double latitude = mLastLocation.getLatitude();
                double longitude = mLastLocation.getLongitude();

//            lblLocation.setText(latitude + ", " + longitude);

                try {

                    Geocoder geocoder;
                    List<Address> addresses;
                    geocoder = new Geocoder(this, Locale.getDefault());

                    addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName();

                    Log.e("address--", address);
                    Log.e("city--", city);
                    Log.e("state--", state);
                    Log.e("country--", country);
                    Log.e("postalCode--", postalCode);
                    Log.e("knownName--", knownName);

                } catch (Exception e) {

                    Log.e("exception--", e.toString());

                }

            } else {

                //     lblLocation
                //             .setText("(Couldn't get the location. Make sure location is enabled on the device)");
            }
        }
        catch (Exception e){

        }
    }

    /**
     * Method to toggle periodic location updates
     * */
    private void togglePeriodicLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            // Changing the button text
         //   btnStartLocationUpdates
        //            .setText(getString(R.string.btn_stop_location_updates));

            mRequestingLocationUpdates = true;

            // Starting the location updates
            startLocationUpdates();

            Log.d(TAG, "Periodic location updates started!");

        } else {
            // Changing the button text
        //    btnStartLocationUpdates
        //           .setText(getString(R.string.btn_start_location_updates));

            mRequestingLocationUpdates = false;

            // Stopping the location updates
            stopLocationUpdates();

            Log.d(TAG, "Periodic location updates stopped!");
        }
    }

    /**
     * Creating google api client object
     * */
    protected synchronized void buildGoogleApiClient() {

        try {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API).build();
        }
        catch (Exception e){

        }
    }

    /**
     * Creating location request object
     * */
    protected void createLocationRequest() {

        try {
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(UPDATE_INTERVAL);
            mLocationRequest.setFastestInterval(FATEST_INTERVAL);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
        }
        catch (Exception e){

        }
    }

    /**
     * Method to verify google play services on the device
     * */
    private boolean checkPlayServices() {
        try {

            int resultCode = GooglePlayServicesUtil
                    .isGooglePlayServicesAvailable(this);
            if (resultCode != ConnectionResult.SUCCESS) {
                if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                    GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                            PLAY_SERVICES_RESOLUTION_REQUEST).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "This device is not supported.", Toast.LENGTH_LONG)
                            .show();
                    finish();
                }
                return false;
            }
        }
        catch (Exception e){

        }
        return true;
    }

    /**
     * Starting the location updates
     * */
    protected void startLocationUpdates() {

        try {

            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        }
        catch (Exception e){

        }

    }

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {
        try {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        }
        catch (Exception e){

        }
    }

    /**
     * Google api callback methods
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        try {
            Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                    + result.getErrorCode());
        }
        catch (Exception e){

        }
    }

    @Override
    public void onConnected(Bundle arg0) {

        try {

            // Once connected with google api, get the location
            displayLocation();

            if (mRequestingLocationUpdates) {
                startLocationUpdates();
            }
        }
        catch (Exception e){

        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {

        try {
            mGoogleApiClient.connect();
        }
        catch (Exception e){

        }
    }

    @Override
    public void onLocationChanged(Location location) {

        try {
            // Assign the new location
            mLastLocation = location;

            Toast.makeText(getApplicationContext(), "Location changed!",
                    Toast.LENGTH_SHORT).show();

            // Displaying the new location on UI
            displayLocation();
        }
        catch (Exception e){

        }
    }

}