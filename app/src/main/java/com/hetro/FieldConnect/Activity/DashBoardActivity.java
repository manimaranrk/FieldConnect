package com.hetro.FieldConnect.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.hetro.FieldConnect.DtoController.TowerDetails;
import com.hetro.FieldConnect.DtoController.UserDetails;
import com.hetro.FieldConnect.DtoController.VisitId;
import com.hetro.FieldConnect.Util.ApplicationConstants;
import com.hetro.FieldConnect.Util.JSONParser;
import com.hetro.FieldConnect.Util.NetworkConnection;
import com.hetro.FieldConnect.Util.TelephonyInfo;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DashBoardActivity extends Activity {

    public static final String MyPREFERENCES = "MyPrefs" ;

    public static final String TowerId = "towerId";
    public static final String UniqId = "uniqId";


    SharedPreferences sharedpreferences;

    String userName,userId,userCircle,groupIndex;

    TextView usernameTextview , timeTextview;

    String currentDateTime;

    Button submit;

    private ProgressDialog pDialog;

    NetworkConnection networkConnection;
    JSONParser jsonParser = new JSONParser();
    TelephonyInfo telephonyInfo;

    JSONArray tower = null;

    ToggleButton tButton;

    int toggle;

    String towerId,uiqId;


    String completeStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dashboard);

        usernameTextview = (TextView)findViewById(R.id.username_textview);

        timeTextview = (TextView)findViewById(R.id.time_textview);

        tButton = (ToggleButton) findViewById(R.id.toggleButton1);

        submit = (Button)findViewById(R.id.dashboard_submit);

        networkConnection = new NetworkConnection(this);
        telephonyInfo = TelephonyInfo.getInstance(this);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        towerId = sharedpreferences.getString(TowerId, "");
        uiqId = sharedpreferences.getString(UniqId, "");

        Log.e("towerId===",towerId);

        userName = UserDetails.getInstance().getUsername();
        userId = UserDetails.getInstance().getId();
        userCircle = UserDetails.getInstance().getCircle();
        groupIndex = UserDetails.getInstance().getGroupIndex();

        usernameTextview.setText("Welcome " + userName);

        try {

            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
            String strDate = sdf.format(c.getTime());

            SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
            String strDate1 = sdf1.format(c.getTime());

            SimpleDateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date date = inFormat.parse(strDate1);
            SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
            String goal = outFormat.format(date);
            Log.e("goal--", goal);

            SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MMM-yyyy"); //Date and time
            String currentDate = sdf2.format(c.getTime());

            currentDateTime = goal+" "+currentDate;

            timeTextview.setText(currentDateTime);

        }
        catch (Exception e){
            Log.e("eeee",e.toString());
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(toggle==1){

                }else{
               //     new SiteidisPresent().execute();
                    if(towerId.equals("")) {
                        new CheckValidUser().execute();
                    }
                    else{
                        new SiteidisPresent().execute();
                    }
                }
            }
        });

//        submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if(toggle==1){
//
//                }else{
//
//                    new CheckSiteid().execute();
//
//
//
//                    if(towerId.equals("")) {
//                        new CheckValidUser().execute();
//                    }
//                    else{
//                        new CheckValidUser1().execute();
//                    }
//
//                }
//
//            }
//        });

        tButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {

                    toggle = 1;

                } else {
                    toggle = 0;
                }

            }
        });

    }

    class CheckValidUser1 extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DashBoardActivity.this);
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

                    json = jsonParser.makeHttpRequest(ApplicationConstants.url_get_tower_by_siteid1,
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
                            String siteId = c.getString(ApplicationConstants.TAG_TOWER_SITE_ID);

                            TowerDetails.getInstance().setTowerSiteId(siteId);
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

                            //  TowerDetails.getInstance().setUserId(userId);
                            TowerDetails.getInstance().setUniqId(uniqId);
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
                            VisitId.getInstance().setVisitId(visitIndex);

                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(TowerId, siteId);
                            editor.putString(UniqId, uniqId);
                            editor.commit();

                            Intent intent = new Intent(DashBoardActivity.this,MapActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();

                        }

                    } else if (success == 0) {

                        Intent intent = new Intent(DashBoardActivity.this,MapActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();

                    } else if (success == 2) {

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class SiteidisPresent extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DashBoardActivity.this);
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
                    params.add(new BasicNameValuePair("siteid", towerId));


                    json = jsonParser.makeHttpRequest(ApplicationConstants.url_check_siteid,
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

                        new CheckSiteid().execute();

                    }
                    else if (success == 0) {

                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(TowerId, "");
                        editor.putString(UniqId, "");
                        editor.commit();

                        new CheckValidUser().execute();

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class CheckSiteid extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DashBoardActivity.this);
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
                    params.add(new BasicNameValuePair("siteid", towerId));


                    json = jsonParser.makeHttpRequest(ApplicationConstants.url_check_complet_status,
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

                        tower = json.getJSONArray(ApplicationConstants.TAG_CHECK_SITE);
                        for (int i = 0; i < tower.length(); i++) {
                            JSONObject c = tower.getJSONObject(i);

                            completeStatus = c.getString(ApplicationConstants.TAG_COMPLETE_STATUS);

                            if(completeStatus.equals("1")) {

                                new CheckValidUser().execute();
                            }
                            else{
                                new CheckValidUser1().execute();

                            }



                        }

                    } else if (success == 0) {

                        Intent intent = new Intent(DashBoardActivity.this,MapActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();

                    } else if (success == 2) {

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

//    class CheckValidUser2 extends AsyncTask<String, String, JSONObject> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pDialog = new ProgressDialog(DashBoardActivity.this);
//            pDialog.setMessage("Login...");
//            pDialog.setIndeterminate(true);
//            pDialog.setCancelable(false);
//            pDialog.show();
//        }
//
//        protected JSONObject doInBackground(String... args) {
//
//            JSONObject json = null;
//
//            try {
//
//                if (networkConnection.isNetworkAvailable()) {
//                    Log.e("network1===", networkConnection.isNetworkAvailable() + "");
//
//                    List<NameValuePair> params = new ArrayList<NameValuePair>();
//                    params.add(new BasicNameValuePair("userid", groupIndex));
//                    params.add(new BasicNameValuePair("usercircle", userCircle));
//
//                    json = jsonParser.makeHttpRequest(ApplicationConstants.url_get_tower_userid1,
//                            "POST", params);
//                }
//            }
//            catch (Exception e){
//            }
//            return json;
//        }
//
//        protected void onPostExecute(JSONObject json) {
//
//            try {
//                if ((pDialog != null) && pDialog.isShowing()) {
//                    pDialog.dismiss();
//                }
//
//            } catch (final IllegalArgumentException e) {
//
//            } catch (final Exception e) {
//
//            } finally {
//                pDialog = null;
//            }
//
//            try {
//                if (json == null) {
//
////                    Toast.makeText(DashBoardActivity.this,
////                            "Invalid User Name and Password ",
////                            Toast.LENGTH_SHORT).show();
//                }
//                else {
//
//                    int success = json.getInt(ApplicationConstants.TAG_SUCCESS);
//                    if (success == 1) {
//
//                        tower = json.getJSONArray(ApplicationConstants.TAG_TOWER);
//                        for (int i = 0; i < tower.length(); i++) {
//                            JSONObject c = tower.getJSONObject(i);
//
//                            String uniqId = c.getString(ApplicationConstants.TAG_TOWER_UNIQ_ID);
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
//
//                            String towerRecordNo = c.getString(ApplicationConstants.TAG_TOWER_RECORDNO);
//                            String towerCircle = c.getString(ApplicationConstants.TAG_TOWER_CIRCLE);
//                            String towerSiteid = c.getString(ApplicationConstants.TAG_TOWER_SITEID);
//                            String towerAddress = c.getString(ApplicationConstants.TAG_TOWER_ADDRESS);
//                            String towerSiteName = c.getString(ApplicationConstants.TAG_TOWER_SITE_NAME);
//                            String towerSiteLatitude = c.getString(ApplicationConstants.TAG_TOWER__SITE_LATITUDE);
//                            String towerSiteLongitude = c.getString(ApplicationConstants.TAG_TOWER_SITE_LONGITUDE);
//                            String towerEngineerName = c.getString(ApplicationConstants.TAG_TOWER_ENGINEER_NAME);
//                            String towerEngineerPhone = c.getString(ApplicationConstants.TAG_TOWER_ENGINEER_PHONE);
//                            String towerIdOd = c.getString(ApplicationConstants.TAG_TOWER_ID_OD);
//                            String siteId = c.getString(ApplicationConstants.TAG_TOWER_SITE_ID);
//
//                            TowerDetails.getInstance().setTowerSiteId(siteId);
//                            TowerDetails.getInstance().setTowerRecordNo(towerRecordNo);
//                            TowerDetails.getInstance().setTowerCircle(towerCircle);
//                            TowerDetails.getInstance().setTowerSiteId(towerSiteid);
//                            TowerDetails.getInstance().setTowerAddress(towerAddress);
//                            TowerDetails.getInstance().setTowerSiteName(towerSiteName);
//                            TowerDetails.getInstance().setTowerSiteLatitude(towerSiteLatitude);
//                            TowerDetails.getInstance().setTowerSiteLongitude(towerSiteLongitude);
//                            TowerDetails.getInstance().setTowerEngineerName(towerEngineerName);
//                            TowerDetails.getInstance().setTowerEngineerPhone(towerEngineerPhone);
//                            TowerDetails.getInstance().setTowerIdOd(towerIdOd);
//
//                            TowerDetails.getInstance().setUniqId(uniqId);
//                            TowerDetails.getInstance().setUserName(userName);
//                            TowerDetails.getInstance().setPreStatusName(preStatusName);
//                            TowerDetails.getInstance().setPostStatusName(postStatusName);
//                            TowerDetails.getInstance().setPreStatus(preStatus);
//                            TowerDetails.getInstance().setPostStatus(postStatus);
//                            TowerDetails.getInstance().setScheduleDate(scheduleDate);
//                            TowerDetails.getInstance().setVisitDate(visitDate);
//                            TowerDetails.getInstance().setTowerSiteId(towerSiteId);
//                            TowerDetails.getInstance().setTeamId(teamId);
//                            TowerDetails.getInstance().setReMarks(reMarks);
//                            TowerDetails.getInstance().setLatiTude(latiTude);
//                            TowerDetails.getInstance().setLongiTude(longiTude);
//                            TowerDetails.getInstance().setApprovedBy(approvedBy);
//                            TowerDetails.getInstance().setCompleteStatus(completeStatus);
//                            TowerDetails.getInstance().setVisitIndex(visitIndex);
//                            VisitId.getInstance().setVisitId(visitIndex);
//
//
//
//
//
//                        }
//
//                    } else if (success == 0) {
//
//                        Intent intent = new Intent(DashBoardActivity.this,MapActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(intent);
//                        finish();
//
//                    } else if (success == 2) {
//
//                    }
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    class CheckValidUser extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DashBoardActivity.this);
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

                    json = jsonParser.makeHttpRequest(ApplicationConstants.url_get_tower_userid1,
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
                            String siteId = c.getString(ApplicationConstants.TAG_TOWER_SITE_ID);

                            TowerDetails.getInstance().setTowerSiteId(siteId);
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

                            TowerDetails.getInstance().setUniqId(uniqId);
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
                            VisitId.getInstance().setVisitId(visitIndex);

                            SharedPreferences.Editor editor = sharedpreferences.edit();

                            editor.putString(TowerId, siteId);
                            editor.putString(UniqId, uniqId);
                            editor.commit();

                            Intent intent = new Intent(DashBoardActivity.this,MapActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();



                        }

                    } else if (success == 0) {

                        Intent intent = new Intent(DashBoardActivity.this,MapActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();

                    } else if (success == 2) {

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}