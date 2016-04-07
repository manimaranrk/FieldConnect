package com.hetro.FieldConnect.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

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

public class SurveyDataActivity extends Activity {

    Spinner siteTypeSpinner,towerTypeSpinner,suplySpinner;

    EditText operatorEdittext;

    CheckBox mncheckbox,dgcheckbox;

    ImageView back;

    Button surveySubmitButton;

    JSONParser jsonParser = new JSONParser();
    NetworkConnection networkConnection;
    private ProgressDialog pDialog;

    int preStatus,postStatus;
    String prestatus;

    int mn=0,dg=0;

    String operatorno;

    String siteType,towerType,supply;

    String siteId,uniqId;

    String pssword,userId,userName,preStatusName,postStatusName,scheduleDate,visitDate,towerSiteId,teamId,reMarks,
            towerLatiTude,towerLogniTude,approvedBy,completeStatus,visitIndex;

    GPSTracker1 gps;

    double latitude,longitude;

    public static final String MyPREFERENCES = "MyPrefs" ;

    public static final String TowerId = "towerId";

    String userCircle,groupIndex;

    String towerId;

    PopupMenu popupMenu;

    SharedPreferences sharedpreferences;
    JSONArray tower = null;
    JSONArray towerTypeArray = null;
    JSONArray siteTypeArray = null;
    List<String> towerList = new ArrayList<String>();
    List<String> siteList = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_data);

        siteTypeSpinner = (Spinner)findViewById(R.id.sitetype_spinner);
        towerTypeSpinner = (Spinner)findViewById(R.id.towertype_spinner);
        suplySpinner = (Spinner)findViewById(R.id.supply_spinner);

        back = (ImageView)findViewById(R.id.svback);

        operatorEdittext = (EditText)findViewById(R.id.operatorno_edittext);

        mncheckbox = (CheckBox)findViewById(R.id.mn_checkbox);
        dgcheckbox = (CheckBox)findViewById(R.id.dg_checkbox);

        surveySubmitButton = (Button)findViewById(R.id.surveysubmit_button);

        networkConnection = new NetworkConnection(this);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        towerId = sharedpreferences.getString(TowerId, "");

        userCircle = UserDetails.getInstance().getCircle();
        groupIndex = UserDetails.getInstance().getGroupIndex();





        List<String> list = new ArrayList<String>();
        list.add("Select Site Type");
        list.add("Indoor");
        list.add("Outdoor");
        list.add("In-shelter");
        list.add("Repeater");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        siteTypeSpinner.setAdapter(dataAdapter);


        List<String> list1 = new ArrayList<String>();
        list1.add("Select Tower Type");
        list1.add("RTT");
        list1.add("GBT");
        list1.add("RTP");
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list1);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        towerTypeSpinner.setAdapter(dataAdapter1);

      //  new SelectTowerType().execute();


        List<String> list2 = new ArrayList<String>();
        list2.add("Select Supply Voltage");
        list2.add("48");
        list2.add("24");

        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list2);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        suplySpinner.setAdapter(dataAdapter2);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SurveyDataActivity.this,DashBoardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }
        });


        surveySubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                gps = new GPSTracker1(SurveyDataActivity.this);

                // check if GPS enabled
                if(gps.canGetLocation()){

                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();

                    // \n is for new line
                    // Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                }else{
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }

                siteId = TowerDetails.getInstance().getTowerSiteId();

                uniqId = TowerDetails.getInstance().getUniqId();


                userName = LoginStatus.getInstance().getUsername();
                pssword = LoginStatus.getInstance().getPassword();
                userId = TowerDetails.getInstance().getUserId();

                siteType = String.valueOf(siteTypeSpinner.getSelectedItem());
                towerType = String.valueOf(towerTypeSpinner.getSelectedItem());
                supply = String.valueOf(suplySpinner.getSelectedItem());

                operatorno = operatorEdittext.getText().toString();



                if(mncheckbox.isChecked()){

                    mn=1;

                }
                if(dgcheckbox.isChecked()){
                    dg=1;
                }

                if(operatorno != "") {

                    if (siteTypeSpinner.getSelectedItem().toString().trim().equals("Select Site Type")) {
                        Toast.makeText(SurveyDataActivity.this, "Select Site TYpe", Toast.LENGTH_SHORT).show();
                    } else {

                        if (towerTypeSpinner.getSelectedItem().toString().trim().equals("Select Tower Type")) {
                            Toast.makeText(SurveyDataActivity.this, "Select Tower Type", Toast.LENGTH_SHORT).show();
                        } else {

                            if (suplySpinner.getSelectedItem().toString().trim().equals("Select Supply Voltage")) {
                                Toast.makeText(SurveyDataActivity.this, "Select Supply Voltage", Toast.LENGTH_SHORT).show();
                            } else {

                                pssword = LoginStatus.getInstance().getPassword();
                                userId = TowerDetails.getInstance().getUserId();
                                userName = LoginStatus.getInstance().getUsername();

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

                                prestatus = "5";

                                new updateStatus().execute();


                            }

                        }

                    }
                }
                else{

                    Toast.makeText(SurveyDataActivity.this, "Please Invalid Operator No.", Toast.LENGTH_SHORT).show();

                }




//                if(!siteType.equals("Select Site Type")){
//
//                }
//                else{
//
//                    if(!towerType.equals("elect Tower Type")){
//
//                    }
//                    else{
//
//                        if(supply.equals("Select Supply Voltage")){
//
//                        }
//                        else{
//
//                            new createClient().execute();
//
//                        }
//
//                    }
//
//                }







            }
        });

    }

    class CheckValidUser extends AsyncTask<String, String, JSONObject> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SurveyDataActivity.this);
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
                Toast.makeText(SurveyDataActivity.this,
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
                    Intent intent=new Intent(SurveyDataActivity.this, MainActivity.class);
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

    public void menuButtonClickEvent(View view){

        popupMenu = new PopupMenu(SurveyDataActivity.this, view);
        popupMenu.inflate(R.menu.menu_logout);
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

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

    class SelectTowerType extends AsyncTask<String, String, JSONObject>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pDialog = new ProgressDialog(SurveyDataActivity.this);
            pDialog.setMessage("Loading Tower Type..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected JSONObject doInBackground(String... args)
        {
            JSONObject json = null;
            if (networkConnection.isNetworkAvailable())
            {
                try
                {
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    json = jsonParser.makeHttpRequest(ApplicationConstants.url_select_towertype,
                            "POST", params);

                } catch (Exception e)
                {
                    Log.e("schedule Eror", e.toString());
                }

            } else {
            }

            return json;
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
                // Handle or log or ignore
            }
            catch (final Exception e)
            {
                // Handle or log or ignore
            }
            finally
            {
                pDialog = null;
            }

            try {
                int success = json.getInt(ApplicationConstants.TAG_SUCCESS);
                if (success == 1)
                {
                    siteTypeArray = json.getJSONArray(ApplicationConstants.TAG_EXISTING_TOWER);

                    for(int i = 0; i < siteTypeArray.length(); i++ ){
                        JSONObject c = siteTypeArray.getJSONObject(i);
                        String towertypeName = c.getString(ApplicationConstants.TAG_EXISTING_TOWER_TYPE);
                        String towertypeid = c.getString(ApplicationConstants.TAG_EXISTING_TOWER_ID);

                        if(towerList.size()==0){
                            towerList.add(0,"Select Tower Type");
                        }

                        towerList.add(towertypeName);

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SurveyDataActivity.this,
                                android.R.layout.simple_spinner_item, towerList);

                        towerTypeSpinner.setAdapter(adapter);

                   //     new SelectSiteType().execute();

                    }
                }
                else
                {
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

    class SelectSiteType extends AsyncTask<String, String, JSONObject>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pDialog = new ProgressDialog(SurveyDataActivity.this);
            pDialog.setMessage("Loading Site Type..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected JSONObject doInBackground(String... args)
        {
            JSONObject json = null;
            if (networkConnection.isNetworkAvailable())
            {
                try
                {
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    json = jsonParser.makeHttpRequest(ApplicationConstants.url_select_sitetype,
                            "POST", params);

                } catch (Exception e)
                {
                    Log.e("schedule Eror", e.toString());
                }

            } else {
            }

            return json;
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
                // Handle or log or ignore
            }
            catch (final Exception e)
            {
                // Handle or log or ignore
            }
            finally
            {
                pDialog = null;
            }

            try {
                int success = json.getInt(ApplicationConstants.TAG_SUCCESS);
                if (success == 1)
                {
                    siteTypeArray = json.getJSONArray(ApplicationConstants.TAG_EXISTING_SITE);

                    for(int i = 0; i < siteTypeArray.length(); i++ ){
                        JSONObject c = siteTypeArray.getJSONObject(i);
                        String sitetypeName = c.getString(ApplicationConstants.TAG_EXISTING_SITE_TYPE);
                        String sitetypeid = c.getString(ApplicationConstants.TAG_EXISTING_SITE_ID);

                        if(siteList.size()==0){
                            siteList.add(0,"Select Site Type");
                        }

                        siteList.add(sitetypeName);

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SurveyDataActivity.this,
                                android.R.layout.simple_spinner_item, siteList);

                        siteTypeSpinner.setAdapter(adapter);
                    }
                }
                else
                {
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

    class updateStatus extends AsyncTask<String, String, JSONObject> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SurveyDataActivity.this);
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
                Log.e("network1===", networkConnection.isNetworkAvailable() + "");


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
            else {
                pDialog.dismiss();
                Looper.prepare();
                Toast.makeText(SurveyDataActivity.this,
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

                    Log.e("surveyyyyy","surveyyyyy");
                    new createClient().execute();
//                    Intent intent= new Intent(SurveyStatus.this, SurveyDataActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
//                    finish();
//                    if(prestatus.equals("1")){
//                        userId = UserDetails.getInstance().getId();
//                        new CheckValidUser1().execute();
//
//
//                    }
//                    else if(prestatus.equals("2")){
//
//                        //    Intent intent= new Intent(MapActivity.this, RepresentingClient.class);
//                        //    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        //    startActivity(intent);
//                        //   finish();
//
////                        Intent intent= new Intent(MapActivity.this, SiteDetailsActivity.class);
////                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
////                        startActivity(intent);
////                        finish();
//
//                    }

                }
                else if(success == 3) {

                    Log.e("surveyyyyy","dataaaayyyyy");

                    new createClient().execute();



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

    class CheckValidUser1 extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SurveyDataActivity.this);
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
                           // VisitId.getInstance().setVisitId(visitIndex);


                            if(approvedBy.equals("1")) {

                                Intent intent = new Intent(SurveyDataActivity.this, ApprovalActivity.class);
                                startActivity(intent);
                                finish();


                            }else {

//                                Intent intent = new Intent(SurveyDataActivity.this, SurveyStatus.class);
//                                startActivity(intent);
//                                finish();
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

    class createClient extends AsyncTask<String, String, JSONObject> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SurveyDataActivity.this);
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
                Log.e("network1===", networkConnection.isNetworkAvailable() + "");


                //  Log.e("userName--",userName);
                // Log.e("passWord--",passWord);
                //  Log.e("tmDevice--",tmDevice);

                String userId = UserDetails.getInstance().getId();


                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", userName));

                params.add(new BasicNameValuePair("pssword", pssword));

                params.add(new BasicNameValuePair("siteid", siteId));
                params.add(new BasicNameValuePair("uniqid", uniqId));

                params.add(new BasicNameValuePair("sitetype", siteType));
                params.add(new BasicNameValuePair("towertype", towerType));
                params.add(new BasicNameValuePair("voltage", supply));
                params.add(new BasicNameValuePair("operator", operatorno));
                params.add(new BasicNameValuePair("dg", dg+""));
                params.add(new BasicNameValuePair("mn", mn+""));

                //  params.add(new BasicNameValuePair("device_id", tmDevice));


                // getting JSON Object
                // Note that create product url accepts POST method
                json = jsonParser.makeHttpRequest(ApplicationConstants.url_survey_data,
                        "POST", params);

                // check log cat fro response
                Log.d("Create Response", json.toString());

                // check for success tag

            }
            else {
                pDialog.dismiss();
                Looper.prepare();
                Toast.makeText(SurveyDataActivity.this,
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

                    new CheckValidUser1().execute();


//                    Intent intent=new Intent(SurveyDataActivity.this, ApprovalActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
//                    finish();
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
                if (success == 3) {
                    new CheckValidUser1().execute();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

}
