package com.hetro.FieldConnect.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;

import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hetro.FieldConnect.DTO.TowerDetailDto;
import com.hetro.FieldConnect.DtoController.LoginStatus;
import com.hetro.FieldConnect.DtoController.TowerDetail;
import com.hetro.FieldConnect.DtoController.TowerDetails;
import com.hetro.FieldConnect.DtoController.UserDetails;
import com.hetro.FieldConnect.DtoController.VisitId;
import com.hetro.FieldConnect.Util.ApplicationConstants;
import com.hetro.FieldConnect.Util.GPSTracker1;
import com.hetro.FieldConnect.Util.JSONParser;
import com.hetro.FieldConnect.Util.NetworkConnection;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class MapActivity extends Activity implements ConnectionCallbacks,
        OnConnectionFailedListener, com.google.android.gms.location.LocationListener
{

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    // Google Map
    private GoogleMap googleMap;
    Button locate_button,submit_button;
    TextView onTheWay_Button;
    ImageView cancel_button;
    private ProgressDialog pDialog;
    NetworkConnection networkConnection;
    String venueName,venueAddress1,venueAddress2,city=null,state,venuePin,contactName,designation,contactNumber,
            contactEmail,ownerName,ownerNumber,venueClass,seatingCapacity,venueWebsite,pastOccupancy,venueType;

    double latitude,longitude;
    JSONParser jsonParser = new JSONParser();

   // GPSListener gpsListener;
 //   private LocationListener listener;
    private LocationManager manager;
    boolean locate=false;
    GPSTracker1 gpsTracker1;
   // GpsStatus gpsStatus;
    int status=0;

    private Location mLastLocation;

    double distance=0.0;
    PopupMenu popupMenu;

    String userId,userName,scheduleDate,visitDate,towerSiteId,teamId,reMarks,
            latiTude,logniTude,towerLatiTude,towerLogniTude,approvedBy,completeStatus,visitIndex;
    int preStatus,postStatus;
    String preStatusName,postStatusName;

    String pssword;

    String prestatus;

    String groupIndex,userCircle;
    private boolean mRequestingLocationUpdates = false;


    JSONArray tower = null;

    public static final String MyPREFERENCES = "MyPrefs" ;

    public static final String TowerId = "towerId";
    public static final String UniqId = "uniqId";


    String siteId,uniqId;

    SharedPreferences sharedpreferences;

    String towerId;

    private GoogleApiClient mGoogleApiClient;

    private LocationRequest mLocationRequest;

    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapactivity);

        locate_button = (Button)findViewById(R.id.locate_button) ;
        submit_button = (Button)findViewById(R.id.sumbit_button);
        onTheWay_Button = (TextView)findViewById(R.id.onthe_button);

        cancel_button = (ImageView)findViewById(R.id.mapcancel_button);

        networkConnection = new NetworkConnection(this);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();

            createLocationRequest();
        }

        try
        {
            String id= TowerDetails.getInstance().getTowerSiteId();

            if (id.length() == 0)
            {

            }
        }
        catch (Exception e)
        {
            onTheWay_Button.setText("No Tower Found");
        }

  //      if (sharedpreferences.contains(TowerId)) {
          //  sharedpreferences.getString(TowerId, "");
            towerId = sharedpreferences.getString(TowerId, "");

        Log.e("towerId===",towerId);
          //  usernameEdittext.setText(sharedpreferences.getString(userNameKey, ""));
      //  }

        submit_button.setEnabled(false);

        submit_button.setBackgroundColor(Color.RED);

        locate_button.setEnabled(false);

        locate_button.setBackgroundColor(Color.RED);
        //      onTheWay_Button.setVisibility(View.INVISIBLE);

        preStatus = TowerDetails.getInstance().getPreStatus();
        postStatus = TowerDetails.getInstance().getPostStatus();

        groupIndex = UserDetails.getInstance().getGroupIndex();
        userCircle = UserDetails.getInstance().getCircle();


        if(preStatus==0 && postStatus==0)
        {

            //   onTheWay_Button.setVisibility(View.VISIBLE);

            submit_button.setText("On the Way to Site");


        }
        else if(preStatus==0 && postStatus==2)
        {

            onTheWay_Button.setText("Reached");
            submit_button.setText("Reached");

        }
        else if(preStatus==0 && postStatus==3)
        {

            onTheWay_Button.setText("Reached Site");

            submit_button.setText("Start Survey");

        }
        else if(preStatus==0 && postStatus==5)
        {

            onTheWay_Button.setText("Survey is Started");

            submit_button.setText("If Wiring Completed");

        }
        else if(preStatus==0 && postStatus==6)
        {

            onTheWay_Button.setText("Wiring Completed");

            submit_button.setText("Capture Image");

        }
        else if(preStatus==0 && postStatus==8)
        {

            onTheWay_Button.setText("");

            submit_button.setText("Power Up STAY Device");

        }
        else if(preStatus==0 && postStatus==9)
        {

            onTheWay_Button.setText("");

            submit_button.setText("Check Data");

        }
        else
        {

            submit_button.setText("Reached");

            onTheWay_Button.setText("Go to Tower");
            //   onTheWay_Button.setVisibility(View.INVISIBLE);
        }

        gpsTracker1 = new GPSTracker1(MapActivity.this);

        if(gpsTracker1.canGetLocation())
        {

            try
            {

                initilizeMap();

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            LocationManager locationManager1 = (LocationManager) getSystemService(LOCATION_SERVICE);

            boolean enabledGPS1 = locationManager1
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            Criteria criteria1 = new Criteria();
            String bestProvider1 = locationManager1.getBestProvider(criteria1, true);
            Location location1 = locationManager1.getLastKnownLocation(bestProvider1);



            if (location1 != null)
            {

                onLocationChanged(location1);

            }
//            else{
//                Toast.makeText(getApplicationContext(), "Your Location nulll " + longitude, Toast.LENGTH_LONG).show();
//                onLocationChanged(location1);
//            }

       //     locationManager1.requestLocationUpdates(bestProvider1, 20000, 0, this);


            // \n is for new line
        //    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

            new CountDownTimer(15000, 1000)
            {

                public void onTick(long millisUntilFinished)
                {
                    //    mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);

                    locate_button.setText("Locate "+millisUntilFinished / 1000);

                    submit_button.setEnabled(false);
                    locate_button.setEnabled(false);

                }

                public void onFinish()
                {

                    locate_button.setText("Locate");
                    locate_button.setEnabled(true);

                    locate_button.setBackgroundColor(Color.parseColor("#01A9DB"));
                }
            }.start();

        }
        else
        {

            showSettingsAlert();

        }

        Log.e("lati--", latitude + "");
        Log.e("long--", longitude + "");



        locate_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

        //        displayLocation();

                togglePeriodicLocationUpdates();

                if (latitude != 0.0 || longitude != 0.0)
                {
                    double towerLatiTude1 = Double.parseDouble(TowerDetails.getInstance().getLatiTude());
                    double towerLogniTude1 = Double.parseDouble(TowerDetails.getInstance().getLongiTude());

                    distance =  calculateDistance(towerLatiTude1,towerLogniTude1);

                    googleMap.setMyLocationEnabled(true);

                    if(preStatus==0  && postStatus==0)
                    {

                        //   onTheWay_Button.setVisibility(View.VISIBLE);

                        submit_button.setText("On the Way to Site");
                        submit_button.setEnabled(true);
                        submit_button.setBackgroundColor(Color.parseColor("#01A9DB"));


                    }
                    else if(preStatus==0  && postStatus==3)
                    {

                        submit_button.setText("Start Survey");
                        submit_button.setEnabled(true);
                        submit_button.setBackgroundColor(Color.parseColor("#01A9DB"));

                    }
                    else if(preStatus==0 && postStatus==4)
                    {

                        submit_button.setText("Enter Survey Data");
                        submit_button.setEnabled(true);
                        submit_button.setBackgroundColor(Color.parseColor("#01A9DB"));

                    }
                    else if(preStatus==0 && postStatus==5)
                    {

                        submit_button.setText("If Wiring Completed");
                        submit_button.setEnabled(true);
                        submit_button.setBackgroundColor(Color.parseColor("#01A9DB"));

                    }
                    else if(preStatus==0 && postStatus==6)
                    {

                        submit_button.setText("Capture Image");
                        submit_button.setEnabled(true);
                        submit_button.setBackgroundColor(Color.parseColor("#01A9DB"));
                    }
                    else if(preStatus==0 && postStatus==7)
                    {

                        submit_button.setText("Tenant Mapping");
                        submit_button.setEnabled(true);
                        submit_button.setBackgroundColor(Color.parseColor("#01A9DB"));
                    }
                    else if(preStatus==0 && postStatus==8)
                    {

                        submit_button.setText("Power Up STAY Device");
                        submit_button.setEnabled(true);
                        submit_button.setBackgroundColor(Color.parseColor("#01A9DB"));
                    }
                    else if(preStatus==0 && postStatus==9)
                    {

                        submit_button.setText("Check Data");
                        submit_button.setEnabled(true);
                        submit_button.setBackgroundColor(Color.parseColor("#01A9DB"));
                    }
                    else if(preStatus==10 && postStatus==9)
                    {

                        submit_button.setText("Tower Completed");
                        submit_button.setEnabled(true);
                        submit_button.setBackgroundColor(Color.parseColor("#01A9DB"));
                    }
                    else
                    {

                        onTheWay_Button.setText("Go to Tower");
                        //   onTheWay_Button.setVisibility(View.INVISIBLE);

                        onTheWay_Button.setText("Distance :"+distance);

                        if (distance <= 100)
                        {

                            submit_button.setEnabled(false);
                            submit_button.setBackgroundColor(Color.RED);

                        //    submit_button.setText("Reached");

                            locate=true;

                            googleMap.setMyLocationEnabled(true);

                            new CountDownTimer(15000, 1000)
                            {

                                public void onTick(long millisUntilFinished)
                                {
                                    //    mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);

                              //      if(submit_button.getText().equals("Reached")) {
                                        //   On the Way to Site
                                        submit_button.setEnabled(false);
                                        locate_button.setEnabled(false);
                                        submit_button.setText("Reached " + millisUntilFinished / 1000);
                              //      }


                                }

                                public void onFinish()
                                {

                                    submit_button.setText("Reached");
                                    submit_button.setEnabled(true);
                                    submit_button.setBackgroundColor(Color.parseColor("#01A9DB"));
                                }
                            }.start();



                        }
                        else
                        {
                            Log.e("locate distance===",distance+"");
                        }

                    }



                }

            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

              //  startActivity(new Intent(MapActivity.this, DashBoardActivity.class));

                Intent intent= new Intent(MapActivity.this, DashBoardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        submit_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if (preStatus == 0 && postStatus == 2)
                {

                    Intent intent = new Intent(MapActivity.this,RepresentingClient.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                }
                else if(preStatus == 0 && postStatus == 3)
                {
                    Intent intent = new Intent(MapActivity.this,SurveyStatus.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                }
                else if(preStatus==0 && postStatus==4)
                {

                    Intent intent= new Intent(MapActivity.this, SurveyDataActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
                else if(preStatus==0 && postStatus==5)
                {

                    Intent intent= new Intent(MapActivity.this, WiringCompleted.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }

                else if(preStatus==0 && postStatus==6)
                {

                    uniqId = TowerDetails.getInstance().getUniqId();

                    siteId = TowerDetails.getInstance().getTowerSiteId();

                    if(!uniqId.equals("") || uniqId!="null")
                    {

                        if(!siteId.equals("") || siteId!="null")
                        {

                        //    TowerDetail.getInstance().setUniqId(uniqId);
                     //       TowerDetail.getInstance().setTowerSiteId(siteId);

//                            TowerDetailDto app = (TowerDetailDto) getApplication();
//                            app.setUniqId(uniqId);
//                            app.setTowerSiteId(siteId);

                            Log.e("uniqId===", uniqId);
                            Log.e("siteId===",siteId);

                            Intent intent = new Intent(MapActivity.this, CaptureImage.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
//                        else{
//
//                            Intent intent = new Intent(MapActivity.this, DashBoardActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(intent);
//                            finish();
//
//                        }
                    }
//                    else{
//
//                        Intent intent = new Intent(MapActivity.this, DashBoardActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(intent);
//                        finish();
//
//                    }
                }
                else if(preStatus==0 && postStatus==7)
                {

                    Intent intent= new Intent(MapActivity.this, TenantActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }

//                else if(preStatus==0 && postStatus==9){
//
//                    Intent intent= new Intent(MapActivity.this, PowerUpSTAYDevice.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
//                    finish();
//
//                }

                else if(preStatus==0 && postStatus==8)
                {

                    Intent intent= new Intent(MapActivity.this, PowerUpSTAYDevice.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                }
                else if(preStatus==0 && postStatus==9)
                {

                    Intent intent= new Intent(MapActivity.this, CheckDataActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                }
                else if(preStatus==10 && postStatus==9)
                {

                    Intent intent= new Intent(MapActivity.this, DashBoardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                }

                else
                {

                    if (submit_button.getText().toString().equals("On the Way to Site"))
                    {

                        pssword = LoginStatus.getInstance().getPassword();
                        userId = TowerDetails.getInstance().getUserId();
                        userName = TowerDetails.getInstance().getUserName();

                        preStatusName = TowerDetails.getInstance().getPreStatusName();
                        postStatusName = TowerDetails.getInstance().getPostStatusName();
                        preStatus = TowerDetails.getInstance().getPreStatus();
                        postStatus = TowerDetails.getInstance().getPostStatus();
                        scheduleDate = TowerDetails.getInstance().getScheduleDate();
                        visitDate = TowerDetails.getInstance().getVisitDate();
                        towerSiteId = TowerDetails.getInstance().getTowerSiteId();
                        teamId = TowerDetails.getInstance().getTeamId();
                        reMarks = TowerDetails.getInstance().getReMarks();
                        towerLatiTude = TowerDetails.getInstance().getLatiTude();
                        towerLogniTude = TowerDetails.getInstance().getLongiTude();
                        approvedBy = TowerDetails.getInstance().getApprovedBy();
                        completeStatus = TowerDetails.getInstance().getCompleteStatus();
                        visitIndex = TowerDetails.getInstance().getVisitIndex();
                        prestatus = "1";

                        new updateStatus().execute();
                    }
//                else if(onTheWay_Button.getText().toString().equals("Go to Tower")){
//
//                    if (latitude != 0.0 || longitude != 0.0) {
//                        double towerLatiTude1 = Double.parseDouble(TowerDetails.getInstance().getLatiTude());
//                        double towerLogniTude1 = Double.parseDouble(TowerDetails.getInstance().getLongiTude());
//
//                        distance = calculateDistance(towerLatiTude1, towerLogniTude1);
//
//                        if (distance <= 50) {
//                          //  onTheWay_Button.setText("You are Reached the Tower");
//                        }
//                    }
//                }

                    else if (submit_button.getText().toString().equals("Reached"))
                    {
                        pssword = LoginStatus.getInstance().getPassword();
                        userId = TowerDetails.getInstance().getUserId();
                        userName = TowerDetails.getInstance().getUserName();

                        preStatusName = TowerDetails.getInstance().getPreStatusName();
                        postStatusName = TowerDetails.getInstance().getPostStatusName();
                        preStatus = TowerDetails.getInstance().getPreStatus();
                        postStatus = TowerDetails.getInstance().getPostStatus();
                        scheduleDate = TowerDetails.getInstance().getScheduleDate();
                        visitDate = TowerDetails.getInstance().getVisitDate();
                        towerSiteId = TowerDetails.getInstance().getTowerSiteId();
                        teamId = TowerDetails.getInstance().getTeamId();
                        reMarks = TowerDetails.getInstance().getReMarks();
                        towerLatiTude = TowerDetails.getInstance().getLatiTude();
                        towerLogniTude = TowerDetails.getInstance().getLongiTude();
                        approvedBy = TowerDetails.getInstance().getApprovedBy();
                        completeStatus = TowerDetails.getInstance().getCompleteStatus();
                        visitIndex = TowerDetails.getInstance().getVisitIndex();

                        prestatus = "2";
                        new updateStatus1().execute();
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        try {
            if (mGoogleApiClient != null) {
                mGoogleApiClient.connect();
            }
        }
        catch (Exception e){

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
            initilizeMap();
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
        }catch (Exception e){

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

    private void displayLocation() {

        try {

            mLastLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);

            if (mLastLocation != null) {
                latitude = mLastLocation.getLatitude();
                longitude = mLastLocation.getLongitude();

                //      lblLocation.setText(latitude + ", " + longitude);

            } else {

                //          lblLocation
                //                  .setText("(Couldn't get the location. Make sure location is enabled on the device)");
            }
        }catch (Exception e){

        }
    }

    /**
     * Method to toggle periodic location updates
     * */
    private void togglePeriodicLocationUpdates() {

        try {
            if (!mRequestingLocationUpdates) {
                // Changing the button text
                //    btnStartLocationUpdates
                //            .setText(getString(R.string.btn_stop_location_updates));

                mRequestingLocationUpdates = true;

                // Starting the location updates
                startLocationUpdates();

                Log.d("loc---", "Periodic location updates started!");

            } else {
                // Changing the button text
                //    btnStartLocationUpdates
                //            .setText(getString(R.string.btn_start_location_updates));

                mRequestingLocationUpdates = false;

                // Stopping the location updates
                stopLocationUpdates();

                Log.d("loc---", "Periodic location updates stopped!");
            }
        }catch (Exception e){

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
            Log.i("loc---", "Connection failed: ConnectionResult.getErrorCode() = "
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
        // Assign the new location
        mLastLocation = location;

        Toast.makeText(getApplicationContext(), "Location changed!",
                Toast.LENGTH_SHORT).show();

        // Displaying the new location on UI
        displayLocation();

        try {

            googleMap.clear();
            MarkerOptions mp = new MarkerOptions();

            double lat = Double.parseDouble(TowerDetails.getInstance().getLatiTude());
            double lon = Double.parseDouble(TowerDetails.getInstance().getLongiTude());
            mp.position(new LatLng(lat, lon));

            if(location==null){
                Toast.makeText(getApplicationContext(), "Location nulllllllll" , Toast.LENGTH_LONG).show();
            }

            LatLng latLng2 = new LatLng(location.getLatitude(), location.getLongitude());

            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng2, 16));
            LatLng latLng = new LatLng(lat, lon);


            googleMap.addMarker(new MarkerOptions().position(latLng).title(
                    "Destination : " + TowerDetails.getInstance().getTowerSiteId()).icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))).showInfoWindow();


            latitude = location.getLatitude();
            longitude = location.getLongitude();

            if (latitude != 0.0 || longitude != 0.0) {

                if(locate ==true) {

                    LatLng latLng1 = new LatLng(latitude, longitude);
                    //    googleMap.addMarker(new MarkerOptions().position(latLng));

                    //   googleMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(13.0900, 78.0000), 14.0f) );

                    //        Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

                    //   googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng1));
                    //   googleMap.animateCamera(CameraUpdateFactory.zoomTo(16));



                    googleMap.setMyLocationEnabled(true);

//                    googleMap.addMarker(new MarkerOptions().position(latLng1).title(
//                            "Your Position Here").icon(BitmapDescriptorFactory
//                            .defaultMarker(BitmapDescriptorFactory.HUE_RED))).showInfoWindow();



                    googleMap.getUiSettings().setZoomControlsEnabled(true);

                    Location locationA = new Location("point A");
                    locationA.setLatitude(lat);
                    locationA.setLongitude(lon);
                    Location locationB = new Location("point B");
                    locationB.setLatitude(latitude);
                    locationB.setLongitude(longitude);
                    distance += locationA.distanceTo(locationB);
                    double kilometers = distance * 0.001;

                    Log.e("lat---", lat + "");
                    Log.e("lon---", lon + "");

                    Log.e("latitude---", latitude + "");
                    Log.e("longitude---", longitude + "");

                    Log.e("distance---", distance + "");


                    //   Toast.makeText(getApplicationContext(), "Your distance is - \n: " + distance , Toast.LENGTH_LONG).show();
                }
                else{

                }


            }else{

                Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

            }




        }catch (Exception e){

            Log.e("error++++----",e.toString());
        }


    }

    class updateStatus1 extends AsyncTask<String, String, JSONObject>
    {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pDialog = new ProgressDialog(MapActivity.this);
            pDialog.setMessage("update Status...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        /**
         * Creating product
         * */
        protected JSONObject doInBackground(String... args)
        {

            JSONObject json1 = null;

            if (networkConnection.isNetworkAvailable())
            {
                Log.e("network1===",networkConnection.isNetworkAvailable()+"");


                //  Log.e("userName--",userName);
                // Log.e("passWord--",passWord);
                //  Log.e("tmDevice--",tmDevice);

                String userId = UserDetails.getInstance().getId();

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", userName));
                params.add(new BasicNameValuePair("pssword", pssword));
                params.add(new BasicNameValuePair("prestatus", prestatus));
                params.add(new BasicNameValuePair("poststatus", postStatus+""));
                params.add(new BasicNameValuePair("towersiteid", towerSiteId));
                params.add(new BasicNameValuePair("teamid", teamId));
                params.add(new BasicNameValuePair("towerlatitude", latitude+""));
                params.add(new BasicNameValuePair("towerlognitude", longitude+""));
                params.add(new BasicNameValuePair("visitindex", visitIndex));


                json1 = jsonParser.makeHttpRequest(ApplicationConstants.url_update_status1,
                        "POST", params);

                Log.e("Update Status", json1.toString());
            }
            else
            {
                pDialog.dismiss();
                Looper.prepare();
                Toast.makeText(MapActivity.this,
                        "Network is not Available. Please Check Your Internet Connection ",
                        Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            return json1;
        }

        protected void onPostExecute(JSONObject json)
        {

            try
            {
                if ((pDialog != null) && pDialog.isShowing())
                {
                    pDialog.dismiss();
                }

            }
            catch (final IllegalArgumentException e)
            {

            } catch (final Exception e)
            {

            } finally
            {
                pDialog = null;
            }

            try
            {
                int success = json.getInt(ApplicationConstants.TAG_SUCCESS);

                if (success == 1)
                {

                    if(prestatus.equals("1"))
                    {
                        userId = UserDetails.getInstance().getId();
                        new CheckValidUser1().execute();
                        submit_button.setText("Reached");

                    }
                    else if(prestatus.equals("2"))
                    {

                        //    Intent intent= new Intent(MapActivity.this, RepresentingClient.class);
                        //    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        //    startActivity(intent);
                        //   finish();

//                        Intent intent= new Intent(MapActivity.this, SiteDetailsActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(intent);
//                        finish();

                    }

                }
                else if(success == 3)
                {

                    Intent intent= new Intent(MapActivity.this, ApprovalActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();


//                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MapActivity.this);
//                    alertDialogBuilder.setMessage("Please wait for corrdinator approval ");
//
//                    alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface arg0, int arg1) {
//
//                            Intent intent= new Intent(MapActivity.this, DashBoardActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(intent);
//                            finish();
//                            // Toast.makeText(MapActivity.this,"You clicked yes button",Toast.LENGTH_LONG).show();
//                        }
//                    });
//
//                    alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            finish();
//                        }
//                    });
//
//                    AlertDialog alertDialog = alertDialogBuilder.create();
//                    alertDialog.show();



                }

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

        }

    }

    class updateStatus extends AsyncTask<String, String, JSONObject>
    {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pDialog = new ProgressDialog(MapActivity.this);
            pDialog.setMessage("update Status...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        /**
         * Creating product
         * */
        protected JSONObject doInBackground(String... args)
        {

            JSONObject json1 = null;

            if (networkConnection.isNetworkAvailable())
            {
                Log.e("network1===",networkConnection.isNetworkAvailable()+"");


                //  Log.e("userName--",userName);
                // Log.e("passWord--",passWord);
                //  Log.e("tmDevice--",tmDevice);

                String userId = UserDetails.getInstance().getId();

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", userName));
                params.add(new BasicNameValuePair("pssword", pssword));
                params.add(new BasicNameValuePair("prestatus", prestatus));
                params.add(new BasicNameValuePair("poststatus", postStatus+""));
                params.add(new BasicNameValuePair("towersiteid", towerSiteId));
                params.add(new BasicNameValuePair("teamid", teamId));
                params.add(new BasicNameValuePair("towerlatitude", latitude+""));
                params.add(new BasicNameValuePair("towerlognitude", longitude+""));
                params.add(new BasicNameValuePair("visitindex", visitIndex));


                json1 = jsonParser.makeHttpRequest(ApplicationConstants.url_update_status,
                        "POST", params);

                Log.e("Update Status", json1.toString());
            }
            else
            {
                pDialog.dismiss();
                Looper.prepare();
                Toast.makeText(MapActivity.this,
                        "Network is not Available. Please Check Your Internet Connection ",
                        Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            return json1;
        }

        protected void onPostExecute(JSONObject json)
        {

            try
            {
                if ((pDialog != null) && pDialog.isShowing())
                {
                    pDialog.dismiss();
                }

            }
            catch (final IllegalArgumentException e)
            {

            }
            catch (final Exception e)
            {

            } finally
            {
                pDialog = null;
            }

            try {
                int success = json.getInt(ApplicationConstants.TAG_SUCCESS);

                if (success == 1)
                {

                    if(prestatus.equals("1"))
                    {
                        userId = UserDetails.getInstance().getId();
                        new CheckValidUser1().execute();
                        submit_button.setText("Reached");

                    }
                    else if(prestatus.equals("2"))
                    {

                    //    Intent intent= new Intent(MapActivity.this, RepresentingClient.class);
                    //    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    //    startActivity(intent);
                     //   finish();

//                        Intent intent= new Intent(MapActivity.this, SiteDetailsActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(intent);
//                        finish();

                    }

                }
                else if(success == 3)
                {

//                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MapActivity.this);
//                    alertDialogBuilder.setMessage("Please wait for corrdinator approval ");
//
//                    alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface arg0, int arg1) {
//
//                            Intent intent= new Intent(MapActivity.this, DashBoardActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(intent);
//                            finish();
//                           // Toast.makeText(MapActivity.this,"You clicked yes button",Toast.LENGTH_LONG).show();
//                        }
//                    });
//
//                    alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            finish();
//                        }
//                    });
//
//                    AlertDialog alertDialog = alertDialogBuilder.create();
//                    alertDialog.show();

//                    Toast.makeText(MapActivity.this,
//                            "Please wait for corrdinator approval ",
//                            Toast.LENGTH_SHORT).show();

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    public void menuButtonClickEvent(View view)
    {

        popupMenu = new PopupMenu(MapActivity.this, view);
        popupMenu.inflate(R.menu.menu_logout);
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                switch (item.getItemId())
                {

                    case R.id.menu_report:

                        Log.e("-logout-", "-logout-");
                        new CheckValidUser().execute();
                        finish();
                        break;

                }
                return true;
            }
        });
    }

    class CheckValidUser1 extends AsyncTask<String, String, JSONObject>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pDialog = new ProgressDialog(MapActivity.this);
            pDialog.setMessage("Login...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected JSONObject doInBackground(String... args)
        {

            JSONObject json = null;

            try
            {

                if (networkConnection.isNetworkAvailable())
                {
                    Log.e("network1===", networkConnection.isNetworkAvailable() + "");

                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("userid", groupIndex));
                    params.add(new BasicNameValuePair("usercircle", userCircle));
                    params.add(new BasicNameValuePair("siteid", towerId));

                    json = jsonParser.makeHttpRequest(ApplicationConstants.url_get_tower_by_siteid,
                            "POST", params);
                }
            }
            catch (Exception e)
            {
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

//                    Toast.makeText(DashBoardActivity.this,
//                            "Invalid User Name and Password ",
//                            Toast.LENGTH_SHORT).show();
                }
                else {

                    int success = json.getInt(ApplicationConstants.TAG_SUCCESS);
                    if (success == 1) {

                        tower = json.getJSONArray(ApplicationConstants.TAG_TOWER);
                        for (int i = 0; i < tower.length(); i++) {
                            JSONObject c = tower.getJSONObject(i);

                           // String userId = c.getString(ApplicationConstants.TAG_TOWER_USER_ID);
                            String uniqId = c.getString(ApplicationConstants.TAG_TOWER_UNIQ_ID);
                            //   String userName = c.getString(ApplicationConstants.TAG_TOWER_USERNAME);
                            String preStatusName = c.getString(ApplicationConstants.TAG_TOWER_PRE_STATUS_NAME);
                            String postStatusName = c.getString(ApplicationConstants.TAG_TOWER_POST_STATUS_NAME);

                            int preStatus = c.getInt(ApplicationConstants.TAG_TOWER_PRE_STATUS);
                            int postStatus = c.getInt(ApplicationConstants.TAG_TOWER_POST_STATUS);
                            String scheduleDate = c.getString(ApplicationConstants.TAG_TOWER_SCHEDULE_DATETIME);
                            String visitDate = c.getString(ApplicationConstants.TAG_TOWER_VISIT_DATETIME);
                            String towerSiteId = c.getString(ApplicationConstants.TAG_TOWER_SITE_ID);
                            String teamId = c.getString(ApplicationConstants.TAG_TOWER_TEAM_INDEX);
                            String reMarks = c.getString(ApplicationConstants.TAG_TOWER_REMARKS);
                            String latiTude = c.getString(ApplicationConstants.TAG_TOWER_LATITUDE);
                            String longiTude = c.getString(ApplicationConstants.TAG_TOWER_LONGITUDE);
                            String approvedBy = c.getString(ApplicationConstants.TAG_TOWER_APPROVED_BY);
                            String completeStatus = c.getString(ApplicationConstants.TAG_TOWER_COMPLETE_STATUS);
                            String visitIndex = c.getString(ApplicationConstants.TAG_TOWER_VISIT_INDEX);





//                            String userId = c.getString(ApplicationConstants.TAG_TOWER_USER_ID);
//                            //   String userName = c.getString(ApplicationConstants.TAG_TOWER_USERNAME);
//                            String preStatusName = c.getString(ApplicationConstants.TAG_TOWER_PRE_STATUS_NAME);
//                            String postStatusName = c.getString(ApplicationConstants.TAG_TOWER_POST_STATUS_NAME);
//
//                            int preStatus = c.getInt(ApplicationConstants.TAG_TOWER_PRE_STATUS);
//                            int postStatus = c.getInt(ApplicationConstants.TAG_TOWER_POST_STATUS);
//                            String scheduleDate = c.getString(ApplicationConstants.TAG_TOWER_SCHEDULE_DATETIME);
//                            String visitDate = c.getString(ApplicationConstants.TAG_TOWER_VISIT_DATETIME);
//                            String towerSiteId = c.getString(ApplicationConstants.TAG_TOWER_SITE_ID);
//                            String teamId = c.getString(ApplicationConstants.TAG_TOWER_TEAM_INDEX);
//                            String reMarks = c.getString(ApplicationConstants.TAG_TOWER_REMARKS);
//                            String latiTude = c.getString(ApplicationConstants.TAG_TOWER_LATITUDE);
//                            String longiTude = c.getString(ApplicationConstants.TAG_TOWER_LONGITUDE);
//                            String approvedBy = c.getString(ApplicationConstants.TAG_TOWER_APPROVED_BY);
//                            String completeStatus = c.getString(ApplicationConstants.TAG_TOWER_COMPLETE_STATUS);
//                            String visitIndex = c.getString(ApplicationConstants.TAG_TOWER_VISIT_INDEX);

                            String towerRecordNo = c.getString(ApplicationConstants.TAG_TOWER_RECORDNO);
                            String towerCircle = c.getString(ApplicationConstants.TAG_TOWER_CIRCLE);
                            String towerSiteid = c.getString(ApplicationConstants.TAG_TOWER_SITEID);
                            String towerAddress = c.getString(ApplicationConstants.TAG_TOWER_ADDRESS);
                            String towerSiteName = c.getString(ApplicationConstants.TAG_TOWER_SITE_NAME);
                            String towerSiteLatitude = c.getString(ApplicationConstants.TAG_TOWER__SITE_LATITUDE);
                            String towerSiteLongitude = c.getString(ApplicationConstants.TAG_TOWER_SITE_LONGITUDE);
                            String towerEngineerName = c.getString(ApplicationConstants.TAG_TOWER_ENGINEER_NAME);
                            String towerEngineerPhone = c.getString(ApplicationConstants.TAG_TOWER_ENGINEER_PHONE);
                            String towerIdOd = c.getString(ApplicationConstants.TAG_TOWER_ID_OD);

                            TowerDetails.getInstance().setUniqId(uniqId);
                            //TowerDetails.getInstance().setUserId(userId);
                            TowerDetails.getInstance().setUserName(userName);
                            TowerDetails.getInstance().setPreStatusName(preStatusName);
                            TowerDetails.getInstance().setPostStatusName(postStatusName);
                            TowerDetails.getInstance().setPreStatus(preStatus);
                            TowerDetails.getInstance().setPostStatus(postStatus);
                            TowerDetails.getInstance().setScheduleDate(scheduleDate);
                            TowerDetails.getInstance().setVisitDate(visitDate);
                            TowerDetails.getInstance().setTowerSiteId(towerSiteId);
                            TowerDetails.getInstance().setTeamId(teamId);
                            TowerDetails.getInstance().setReMarks(reMarks);
                            TowerDetails.getInstance().setLatiTude(latiTude);
                            TowerDetails.getInstance().setLongiTude(longiTude);
                            TowerDetails.getInstance().setApprovedBy(approvedBy);
                            TowerDetails.getInstance().setCompleteStatus(completeStatus);
                            TowerDetails.getInstance().setVisitIndex(visitIndex);

                            TowerDetails.getInstance().setTowerRecordNo(towerRecordNo);
                            TowerDetails.getInstance().setTowerCircle(towerCircle);
                            TowerDetails.getInstance().setTowerSiteId(towerSiteid);
                            TowerDetails.getInstance().setTowerAddress(towerAddress);
                            TowerDetails.getInstance().setTowerSiteName(towerSiteName);
                            TowerDetails.getInstance().setTowerSiteLatitude(towerSiteLatitude);
                            TowerDetails.getInstance().setTowerSiteLongitude(towerSiteLongitude);
                            TowerDetails.getInstance().setTowerEngineerName(towerEngineerName);
                            TowerDetails.getInstance().setTowerEngineerPhone(towerEngineerPhone);
                            TowerDetails.getInstance().setTowerIdOd(towerIdOd);
                            VisitId.getInstance().setVisitId(visitIndex);


                            Intent intent = new Intent(MapActivity.this,MapActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();



                        }

                    } else if (success == 0) {

                    } else if (success == 2) {

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class CheckValidUser extends AsyncTask<String, String, JSONObject> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MapActivity.this);
            pDialog.setMessage("Logout...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        /**
         * Creating product
         * */
        protected JSONObject doInBackground(String... args) {

            JSONObject json = null;

            if (networkConnection.isNetworkAvailable()) {
                Log.e("network1===",networkConnection.isNetworkAvailable()+"");

                String userId = UserDetails.getInstance().getId();

                String userName = UserDetails.getInstance().getUsername();

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("user_id", userName));

                json = jsonParser.makeHttpRequest(ApplicationConstants.url_logout,
                        "POST", params);

                Log.d("Create Response", json.toString());

            }
            else {
                pDialog.dismiss();
                Looper.prepare();
                Toast.makeText(MapActivity.this,
                        "Network is not Available. Please Check Your Internet Connection ",
                        Toast.LENGTH_SHORT).show();
                Looper.loop();
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
                int success = json.getInt(ApplicationConstants.TAG_SUCCESS);

                if (success == 1) {



                    String exit="exit";
                    Intent intent=new Intent(MapActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("exit",exit);

                    startActivity(intent);
                    finish();


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    public int  showSettingsAlert(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MapActivity.this);
        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");
        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                Intent intentRedirectionGPSSettings = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                intentRedirectionGPSSettings.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivityForResult(intentRedirectionGPSSettings, 1);

            }
        });

        alertDialog.show();

        return status;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("resultcode______",resultCode+"");

        if(resultCode==0){

            Log.e("refreshhh===","refresh==");

          //  Intent refresh = new Intent(this, TargetEntityActivity.class);
          //  startActivity(refresh);
         //   this.finish();
        }
        else{
            Log.e("refreshhh1111===","refresh==");
        }

    }

//    private class MyLocationListener implements LocationListener{
//        public void onLocationChanged(Location location) {
//
//            // TODO Auto-generated method stub
//            if (location != null){
//                //text.setText(("Lat: " + location.getLatitude()
//                 //       + "\nLLong: " + location.getLongitude()));
//            }
//            Toast.makeText(MapActivity.this, ""+location, Toast.LENGTH_SHORT).show();
//        }
//        public void onProviderDisabled(String provider) {
//            // TODO Auto-generated method stub
//        }
//        public void onProviderEnabled(String provider) {
//            // TODO Auto-generated method stub
//
//        }
//        @Override
//        public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
//            // TODO Auto-generated method stub
//
//        }
//    }

    public double calculateDistance(double lat,double lon){

        Location locationA = new Location("point A");
        locationA.setLatitude(lat);
        locationA.setLongitude(lon);
        Location locationB = new Location("point B");
        locationB.setLatitude(latitude);
        locationB.setLongitude(longitude);
        distance += locationA.distanceTo(locationB);
        double kilometers = distance * 0.001;

        Log.e("lat---", lat + "");
        Log.e("lon---", lon + "");

        Log.e("latitude---", latitude + "");
        Log.e("longitude---", longitude + "");

        Log.e("distance---", distance + "");
        Log.e("kilometers---", kilometers + "");

        return distance;
    }


    public void onLocationChanged1(Location location) {

        try {

            googleMap.clear();
            MarkerOptions mp = new MarkerOptions();

            double lat = Double.parseDouble(TowerDetails.getInstance().getLatiTude());
            double lon = Double.parseDouble(TowerDetails.getInstance().getLongiTude());
            mp.position(new LatLng(lat, lon));

            if(location==null){
                Toast.makeText(getApplicationContext(), "Location nulllllllll" , Toast.LENGTH_LONG).show();
            }

            LatLng latLng2 = new LatLng(location.getLatitude(), location.getLongitude());

            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng2, 16));
            LatLng latLng = new LatLng(lat, lon);


            googleMap.addMarker(new MarkerOptions().position(latLng).title(
                    "Destination : " + TowerDetails.getInstance().getTowerSiteId()).icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))).showInfoWindow();


            latitude = location.getLatitude();
            longitude = location.getLongitude();

            if (latitude != 0.0 || longitude != 0.0) {

                if(locate ==true) {

                    LatLng latLng1 = new LatLng(latitude, longitude);
                    //    googleMap.addMarker(new MarkerOptions().position(latLng));

                    //   googleMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(13.0900, 78.0000), 14.0f) );

                    //        Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

                 //   googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng1));
                 //   googleMap.animateCamera(CameraUpdateFactory.zoomTo(16));



                    googleMap.setMyLocationEnabled(true);

//                    googleMap.addMarker(new MarkerOptions().position(latLng1).title(
//                            "Your Position Here").icon(BitmapDescriptorFactory
//                            .defaultMarker(BitmapDescriptorFactory.HUE_RED))).showInfoWindow();



                    googleMap.getUiSettings().setZoomControlsEnabled(true);

                    Location locationA = new Location("point A");
                    locationA.setLatitude(lat);
                    locationA.setLongitude(lon);
                    Location locationB = new Location("point B");
                    locationB.setLatitude(latitude);
                    locationB.setLongitude(longitude);
                    distance += locationA.distanceTo(locationB);
                    double kilometers = distance * 0.001;

                    Log.e("lat---", lat + "");
                    Log.e("lon---", lon + "");

                    Log.e("latitude---", latitude + "");
                    Log.e("longitude---", longitude + "");

                    Log.e("distance---", distance + "");


                    //   Toast.makeText(getApplicationContext(), "Your distance is - \n: " + distance , Toast.LENGTH_LONG).show();
                }
                else{

                }


            }else{

                Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

            }




        }catch (Exception e){

            Log.e("error++++----",e.toString());
        }

    }

//    @Override
//    public void onLocationChanged(Location location) {
//
//        try {
//
//
//
//            latitude = location.getLatitude();
//            longitude = location.getLongitude();
//
//            if (latitude != 0.0 || longitude != 0.0) {
//
//                LatLng latLng = new LatLng(latitude, longitude);
//                //    googleMap.addMarker(new MarkerOptions().position(latLng));
//
//                //   googleMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(13.0900, 78.0000), 14.0f) );
//
//        //        Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
//
//                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//                googleMap.animateCamera(CameraUpdateFactory.zoomTo(16));
//
//                //  googleMap.setMyLocationEnabled(true);
//
//                googleMap.getUiSettings().setZoomControlsEnabled(true);
//            }
//        }catch (Exception e){
//
//            Log.e("error++++----",e.toString());
//        }
//
//    }



    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    /**
     * function to load map. If map is not created it will create it for you
     * */
    private void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();

            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

   // @Override
    protected void onResume1() {
        super.onResume();

        initilizeMap();

        Log.e("resuleeee","resuleeeee");

    }
}