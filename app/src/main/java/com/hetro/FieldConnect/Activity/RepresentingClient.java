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
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hetro.FieldConnect.DtoController.LoginStatus;
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


public class RepresentingClient extends Activity implements LocationListener {

    public static final String MyPREFERENCES = "MyPrefs" ;

    public static final String TowerId = "towerId";
    public static final String UniqId = "uniqId";


    SharedPreferences sharedpreferences;

    Spinner clientSpinner;
    Button clientSubmitButton;
    ImageView siteDetailsButton;
    ImageView back;

    EditText clientNameEdittext,clientNoEdittext;

    int status=0;
    LinearLayout l1;

    double latitude,longitude;

    PopupMenu popupMenu;
    JSONParser jsonParser = new JSONParser();
    NetworkConnection networkConnection;
    private ProgressDialog pDialog;
    String selectedItem;

    String clientName,clientNo,uniqId;

    String pssword,userId,userName;

    String scheduleDate,visitDate,towerSiteId,teamId,reMarks,
            latiTude,logniTude,towerLatiTude,towerLogniTude,approvedBy,completeStatus,visitIndex;
    int preStatus,postStatus;
    String preStatusName,postStatusName;

    JSONArray tower = null;

    String userCircle,groupIndex;
    GPSTracker1 gpsTracker1;


    String towerId;

    String prestatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.represent_client);

        clientSpinner = (Spinner)findViewById(R.id.client_spinner);

        clientNameEdittext = (EditText)findViewById(R.id.clientname_edittext);
        clientNoEdittext = (EditText)findViewById(R.id.clientno_edittext);

        clientSubmitButton = (Button)findViewById(R.id.clientsubmit_button);

        siteDetailsButton = (ImageView)findViewById(R.id.sitedetails_button);


        networkConnection = new NetworkConnection(this);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        towerId = sharedpreferences.getString(TowerId, "");

        Log.e("towerId===",towerId);

        l1= (LinearLayout)findViewById(R.id.linear1);

        userCircle = UserDetails.getInstance().getCircle();
        groupIndex = UserDetails.getInstance().getGroupIndex();

        gpsTracker1 = new GPSTracker1(RepresentingClient.this);

        Log.e("--------Uniq id-------", TowerDetails.getInstance().getUniqId());
        uniqId = TowerDetails.getInstance().getUniqId();

        if(gpsTracker1.canGetLocation()){

            try {

              //  initilizeMap();

            } catch (Exception e) {
                e.printStackTrace();
            }

            LocationManager locationManager1 = (LocationManager) getSystemService(LOCATION_SERVICE);

            boolean enabledGPS1 = locationManager1
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            Criteria criteria1 = new Criteria();
            String bestProvider1 = locationManager1.getBestProvider(criteria1, true);
            Location location1 = locationManager1.getLastKnownLocation(bestProvider1);



            if (location1 != null) {

                onLocationChanged(location1);

            }
            locationManager1.requestLocationUpdates(bestProvider1, 20000, 0, this);


            // \n is for new line
            //    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();



        }else{

            showSettingsAlert();

        }



        back = (ImageView)findViewById(R.id.cback);

        clientNameEdittext.setVisibility(View.INVISIBLE);
        clientNoEdittext.setVisibility(View.INVISIBLE);
        l1.setVisibility(View.GONE);

        List<String> categories = new ArrayList<>();
        categories.add("Select Option");
        categories.add("Site Engineer");
        categories.add("Site Guard");
        categories.add("Site Technician");
        categories.add("Other");
        categories.add("None");


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        clientSpinner.setAdapter(dataAdapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RepresentingClient.this, DashBoardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();


            }
        });

        siteDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RepresentingClient.this, SiteDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }
        });

        clientSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clientName=clientNameEdittext.getText().toString();
                clientNo = clientNoEdittext.getText().toString();

                userName = LoginStatus.getInstance().getUsername();
                pssword = LoginStatus.getInstance().getPassword();
                userId = TowerDetails.getInstance().getUserId();


               // pssword = LoginStatus.getInstance().getPassword();
              //  userId = TowerDetails.getInstance().getUserId();
              //  userName = TowerDetails.getInstance().getUserName();

                if(selectedItem.equals("")){

                    Toast.makeText(RepresentingClient.this,
                            "Select Client Designation",
                            Toast.LENGTH_SHORT).show();

                }
                else {

                    if (clientName.equals("")) {

                        Toast.makeText(RepresentingClient.this,
                                "Enter Client Name ",
                                Toast.LENGTH_SHORT).show();

                    } else {

                        if (clientNo.length() != 10) {

                            Toast.makeText(RepresentingClient.this,
                                    "Enter Contact No. ",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            new createClient().execute();
                        }

                    }
                }







            }
        });

        clientSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                if(position!=0) {

                    selectedItem = parent.getItemAtPosition(position).toString();

                    Log.v("item======", selectedItem);

                    if (selectedItem.equals("Other") || selectedItem.equals("None")) {
                        l1.setVisibility(View.GONE);
                        clientNameEdittext.setVisibility(View.INVISIBLE);
                        clientNoEdittext.setVisibility(View.INVISIBLE);

                    } else {

                        l1.setVisibility(View.VISIBLE);
                        clientNameEdittext.setVisibility(View.VISIBLE);
                        clientNoEdittext.setVisibility(View.VISIBLE);
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });


    }

    public int  showSettingsAlert(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(RepresentingClient.this);
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

    public void cmenuButtonClickEvent(View view){

        popupMenu = new PopupMenu(RepresentingClient.this, view);
        popupMenu.inflate(R.menu.menu_logout);
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
//                    case R.id.menu_venue:
//
//                        startActivity(new Intent(ScheduledVenueActivity.this, CreateVenueActivity.class));
//
//                        break;

                    case R.id.menu_report:

                        Log.e("-logout-", "-logout-");

                        new CheckValidUser().execute();

                        //     startActivity(new Intent(UserActionActivity.this, LoginActivity.class));

                        finish();


                        //   startActivity(new Intent(ScheduledVenueActivity.this, ReportActivity.class));

                        break;

                }
                return true;
            }
        });
    }

    class CheckValidUser1 extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RepresentingClient.this);
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
                    params.add(new BasicNameValuePair("userid", groupIndex));
                    params.add(new BasicNameValuePair("usercircle", userCircle));
                    params.add(new BasicNameValuePair("siteid", towerId));

                    json = jsonParser.makeHttpRequest(ApplicationConstants.url_get_tower_by_siteid,
                            "POST", params);
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

                            String uniqId = c.getString(ApplicationConstants.TAG_TOWER_UNIQ_ID);
                         //   String userId = c.getString(ApplicationConstants.TAG_TOWER_USER_ID);
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
                           // TowerDetails.getInstance().setUserId(userId);
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


                            if(approvedBy.equals("1")) {

                                Intent intent = new Intent(RepresentingClient.this, ApprovalActivity.class);
                                startActivity(intent);
                                finish();


                            }else {

                                Intent intent = new Intent(RepresentingClient.this, SurveyStatus.class);
                                startActivity(intent);
                                finish();
                            }



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

    class updateStatus1 extends AsyncTask<String, String, JSONObject> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RepresentingClient.this);
            pDialog.setMessage("update Status...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        /**
         * Creating product
         * */
        protected JSONObject doInBackground(String... args) {

            JSONObject json1 = null;

            if (networkConnection.isNetworkAvailable()) {
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
            else {
                pDialog.dismiss();
                Looper.prepare();
                Toast.makeText(RepresentingClient.this,
                        "Network is not Available. Please Check Your Internet Connection ",
                        Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            return json1;
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

                    new CheckValidUser1().execute();

//                    Intent intent = new Intent(RepresentingClient.this, SurveyStatus.class);
//
//
//                    startActivity(intent);
//                    finish();

                }
                else if(success == 3){

                    new CheckValidUser1().execute();

//                    Intent intent = new Intent(RepresentingClient.this, SurveyStatus.class);
//
//
//                    startActivity(intent);
//                    finish();

//                    Intent intent= new Intent(RepresentingClient.this, ApprovalActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
//                    finish();


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

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    class createClient extends AsyncTask<String, String, JSONObject> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RepresentingClient.this);
            pDialog.setMessage("Create Client...");
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


                //  Log.e("userName--",userName);
                // Log.e("passWord--",passWord);
                //  Log.e("tmDevice--",tmDevice);

                String userId = UserDetails.getInstance().getId();


                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", userName));

                params.add(new BasicNameValuePair("pssword", pssword));
                params.add(new BasicNameValuePair("name", clientName));
                params.add(new BasicNameValuePair("uniqid", uniqId));
                params.add(new BasicNameValuePair("roleid", selectedItem));
                params.add(new BasicNameValuePair("contactnumber", clientNo));

                //  params.add(new BasicNameValuePair("device_id", tmDevice));


                // getting JSON Object
                // Note that create product url accepts POST method
                json = jsonParser.makeHttpRequest(ApplicationConstants.url_client_represent,
                        "POST", params);

                // check log cat fro response
                Log.d("Create Response", json.toString());

                // check for success tag

            }
            else {
                pDialog.dismiss();
                Looper.prepare();
                Toast.makeText(RepresentingClient.this,
                        "Network is not Available. Please Check Your Internet Connection ",
                        Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            return json;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(JSONObject json) {
            // dismiss the dialog once done
            try {
                if ((pDialog != null) && pDialog.isShowing()) {
                    pDialog.dismiss();
                }

            } catch (final IllegalArgumentException e) {
                // Handle or log or ignore
            } catch (final Exception e) {
                // Handle or log or ignore
            } finally {
                pDialog = null;
            }


            try {
                int success = json.getInt(ApplicationConstants.TAG_SUCCESS);

                if (success == 1) {

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

                    prestatus = "3";
                    new updateStatus1().execute();


                    //  LoginStatus.getInstance().setLoginStatus(1);

                    //   SharedPreferences.Editor editor = sharedpreferences.edit();
                    //    editor.putInt(loginstatusKey, 1);
                    //   editor.commit();

                    //  startActivity(new Intent(UserActionActivity.this, LoginActivity.class));



//                    String exit="exit";
//
//                    Intent intent=new Intent(RepresentingClient.this, MainActivity.class);
//
//                    // intent.putExtra("exit",exit);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
//                    finish();


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
            pDialog = new ProgressDialog(RepresentingClient.this);
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


                //  Log.e("userName--",userName);
                // Log.e("passWord--",passWord);
                //  Log.e("tmDevice--",tmDevice);

                String userId = UserDetails.getInstance().getId();
                String userName = UserDetails.getInstance().getUsername();

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("user_id", userName));

                //  params.add(new BasicNameValuePair("device_id", tmDevice));


                // getting JSON Object
                // Note that create product url accepts POST method
                json = jsonParser.makeHttpRequest(ApplicationConstants.url_logout,
                        "POST", params);

                // check log cat fro response
                Log.d("Create Response", json.toString());

                // check for success tag

            }
            else {
                pDialog.dismiss();
                Looper.prepare();
                Toast.makeText(RepresentingClient.this,
                        "Network is not Available. Please Check Your Internet Connection ",
                        Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            return json;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(JSONObject json) {
            // dismiss the dialog once done
            try {
                if ((pDialog != null) && pDialog.isShowing()) {
                    pDialog.dismiss();
                }

            } catch (final IllegalArgumentException e) {
                // Handle or log or ignore
            } catch (final Exception e) {
                // Handle or log or ignore
            } finally {
                pDialog = null;
            }


            try {
                int success = json.getInt(ApplicationConstants.TAG_SUCCESS);

                if (success == 1) {


                    //  LoginStatus.getInstance().setLoginStatus(1);

                    //   SharedPreferences.Editor editor = sharedpreferences.edit();
                    //    editor.putInt(loginstatusKey, 1);
                    //   editor.commit();

                    //  startActivity(new Intent(UserActionActivity.this, LoginActivity.class));

                    String exit="exit";

                    Intent intent=new Intent(RepresentingClient.this, MainActivity.class);

                     intent.putExtra("exit",exit);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void onLocationChanged(Location location) {

        try {



            latitude = location.getLatitude();
            longitude = location.getLongitude();






        }catch (Exception e){

            Log.e("error++++----",e.toString());
        }

    }


    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }


}
