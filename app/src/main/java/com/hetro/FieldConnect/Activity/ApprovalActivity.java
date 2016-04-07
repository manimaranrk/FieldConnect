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
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.List;

public class ApprovalActivity extends Activity {

    public static final String MyPREFERENCES = "MyPrefs" ;

    public static final String TowerId = "towerId";


    SharedPreferences sharedpreferences;

    ImageView reloagImageview;

    String userCircle;

    String userName,userId,groupIndex;

    private ProgressDialog pDialog;
    NetworkConnection networkConnection;
    JSONParser jsonParser = new JSONParser();
    TelephonyInfo telephonyInfo;

    JSONArray tower = null;

    String towerId;

    ImageView back;
    PopupMenu popupMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval);

        reloagImageview = (ImageView)findViewById(R.id.areload_imageview);

        back =(ImageView)findViewById(R.id.aback);

        groupIndex = UserDetails.getInstance().getGroupIndex();
        userId = UserDetails.getInstance().getId();

        networkConnection = new NetworkConnection(this);
        telephonyInfo = TelephonyInfo.getInstance(this);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        towerId = sharedpreferences.getString(TowerId, "");

        Log.e("towerId===",towerId);

        userCircle = UserDetails.getInstance().getCircle();

        reloagImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userId = UserDetails.getInstance().getId();

                new CheckValidUser().execute();

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ApprovalActivity.this,DashBoardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }
        });
    }

    public void amenuButtonClickEvent(View view){

        popupMenu = new PopupMenu(ApprovalActivity.this, view);
        popupMenu.inflate(R.menu.menu_logout);
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.menu_report:

                        Log.e("-logout-", "-logout-");
                        new CheckValidUser1().execute();
                        finish();
                        break;

                }
                return true;
            }
        });
    }

    class CheckValidUser1 extends AsyncTask<String, String, JSONObject> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ApprovalActivity.this);
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
                Toast.makeText(ApprovalActivity.this,
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
                    Intent intent=new Intent(ApprovalActivity.this, MainActivity.class);
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

    class CheckValidUser extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ApprovalActivity.this);
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

                          //  String userId = c.getString(ApplicationConstants.TAG_TOWER_USER_ID);
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



//                            String towerIndex = c.getString(ApplicationConstants.TAG_TOWER_INDEX);
//                            String siteId = c.getString(ApplicationConstants.TAG_SITE_ID);
//                            String siteName = c.getString(ApplicationConstants.TAG_SITE_NAME);
//                            String towerType = c.getString(ApplicationConstants.TAG_TOWER_TYPE);
//                            String sitePlacement = c.getString(ApplicationConstants.TAG_SITE_PLACEMENT);
//
//                            String siteTechName = c.getString(ApplicationConstants.TAG_SITE_TECH_NAME);
//                            String siteTechPhone = c.getString(ApplicationConstants.TAG_SITE_TECH_PHONE);
//                            String siteTechEmail = c.getString(ApplicationConstants.TAG_SITE_TECH_EMAIL);
//                            String siteEngineerName = c.getString(ApplicationConstants.TAG_SITE_ENGINEER_NAME);
//                            String siteEngineerPhone = c.getString(ApplicationConstants.TAG_SITE_ENGINEER_PHONE);
//
//                            String siteLatitude = c.getString(ApplicationConstants.TAG_SITE_LATITUDE);
//                            String siteLongitude = c.getString(ApplicationConstants.TAG_SITE_LONGITUDE);
//                            String siteAddr1 = c.getString(ApplicationConstants.TAG_SITE_ADDR1);
//                            String siteAddr2 = c.getString(ApplicationConstants.TAG_SITE_ADDR2);
//                            String sitePostOffice = c.getString(ApplicationConstants.TAG_SITE_POST_OFFICE);
//
//                            String siteZipCode = c.getString(ApplicationConstants.TAG_SITE_ZIP_CODE);
//                            String siteDistrict = c.getString(ApplicationConstants.TAG_SITE_DISTRICT);
//                            String siteState = c.getString(ApplicationConstants.TAG_SITE_STATE);
//                            String siteCountry = c.getString(ApplicationConstants.TAG_SITE_COUNTRY);
//                            String siteStatusActive = c.getString(ApplicationConstants.TAG_SITE_STATUS_ACTIVE);
//
//                            String siteType = c.getString(ApplicationConstants.TAG_SITE_TYPE);
//                            String siteLastUpdatedby = c.getString(ApplicationConstants.TAG_SITE_LAST_UPDATEDBY);
//                            String siteLastUpdatedDate = c.getString(ApplicationConstants.TAG_SITE_LAST_UPDATED_DATE);
//                            String siteClusterIndex = c.getString(ApplicationConstants.TAG_SITE_CLUSTER_INDEX);
//                            String siteInfrastructureIndex = c.getString(ApplicationConstants.TAG_SITE_INFRASTRUCTURE_INDEX);

//                            TowerDetails.getInstance().setTowerIndex(towerIndex);
//                            TowerDetails.getInstance().setSiteId(siteId);
//                            TowerDetails.getInstance().setSiteName(siteName);
//                            TowerDetails.getInstance().setTowerType(towerType);
//                            TowerDetails.getInstance().setSitePlacement(sitePlacement);
//
//                            TowerDetails.getInstance().setSiteTechName(siteTechName);
//                            TowerDetails.getInstance().setSiteTechPhone(siteTechPhone);
//                            TowerDetails.getInstance().setSiteTechEmail(siteTechEmail);
//                            TowerDetails.getInstance().setSiteEngineerName(siteEngineerName);
//                            TowerDetails.getInstance().setSiteEngineerPhone(siteEngineerPhone);
//
//                            TowerDetails.getInstance().setSiteLatitude(siteLatitude);
//                            TowerDetails.getInstance().setSiteLongitude(siteLongitude);
//                            TowerDetails.getInstance().setSiteAddr1(siteAddr1);
//                            TowerDetails.getInstance().setSiteAddr2(siteAddr2);
//                            TowerDetails.getInstance().setSitePostOffice(sitePostOffice);
//
//                            TowerDetails.getInstance().setSiteZipCode(siteZipCode);
//                            TowerDetails.getInstance().setSiteDistrict(siteDistrict);
//                            TowerDetails.getInstance().setSiteState(siteState);
//                            TowerDetails.getInstance().setSiteCountry(siteCountry);
//                            TowerDetails.getInstance().setSiteStatusActive(siteStatusActive);
//
//                            TowerDetails.getInstance().setSiteType(siteType);
//                            TowerDetails.getInstance().setSiteLastUpdatedby(siteLastUpdatedby);
//                            TowerDetails.getInstance().setSiteLastUpdatedDate(siteLastUpdatedDate);
//                            TowerDetails.getInstance().setSiteClusterIndex(siteClusterIndex);
//                            TowerDetails.getInstance().setSiteInfrastructureIndex(siteInfrastructureIndex);

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
                         //   VisitId.getInstance().setVisitId(visitIndex);

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
//
////                            String towerIndex = c.getString(ApplicationConstants.TAG_TOWER_INDEX);
////                            String siteId = c.getString(ApplicationConstants.TAG_SITE_ID);
////                            String siteName = c.getString(ApplicationConstants.TAG_SITE_NAME);
////                            String towerType = c.getString(ApplicationConstants.TAG_TOWER_TYPE);
////                            String sitePlacement = c.getString(ApplicationConstants.TAG_SITE_PLACEMENT);
////
////                            String siteTechName = c.getString(ApplicationConstants.TAG_SITE_TECH_NAME);
////                            String siteTechPhone = c.getString(ApplicationConstants.TAG_SITE_TECH_PHONE);
////                            String siteTechEmail = c.getString(ApplicationConstants.TAG_SITE_TECH_EMAIL);
////                            String siteEngineerName = c.getString(ApplicationConstants.TAG_SITE_ENGINEER_NAME);
////                            String siteEngineerPhone = c.getString(ApplicationConstants.TAG_SITE_ENGINEER_PHONE);
////
////                            String siteLatitude = c.getString(ApplicationConstants.TAG_SITE_LATITUDE);
////                            String siteLongitude = c.getString(ApplicationConstants.TAG_SITE_LONGITUDE);
////                            String siteAddr1 = c.getString(ApplicationConstants.TAG_SITE_ADDR1);
////                            String siteAddr2 = c.getString(ApplicationConstants.TAG_SITE_ADDR2);
////                            String sitePostOffice = c.getString(ApplicationConstants.TAG_SITE_POST_OFFICE);
////
////                            String siteZipCode = c.getString(ApplicationConstants.TAG_SITE_ZIP_CODE);
////                            String siteDistrict = c.getString(ApplicationConstants.TAG_SITE_DISTRICT);
////                            String siteState = c.getString(ApplicationConstants.TAG_SITE_STATE);
////                            String siteCountry = c.getString(ApplicationConstants.TAG_SITE_COUNTRY);
////                            String siteStatusActive = c.getString(ApplicationConstants.TAG_SITE_STATUS_ACTIVE);
////
////                            String siteType = c.getString(ApplicationConstants.TAG_SITE_TYPE);
////                            String siteLastUpdatedby = c.getString(ApplicationConstants.TAG_SITE_LAST_UPDATEDBY);
////                            String siteLastUpdatedDate = c.getString(ApplicationConstants.TAG_SITE_LAST_UPDATED_DATE);
////                            String siteClusterIndex = c.getString(ApplicationConstants.TAG_SITE_CLUSTER_INDEX);
////                            String siteInfrastructureIndex = c.getString(ApplicationConstants.TAG_SITE_INFRASTRUCTURE_INDEX);
////
////                            TowerDetails.getInstance().setTowerIndex(towerIndex);
////                            TowerDetails.getInstance().setSiteId(siteId);
////                            TowerDetails.getInstance().setSiteName(siteName);
////                            TowerDetails.getInstance().setTowerType(towerType);
////                            TowerDetails.getInstance().setSitePlacement(sitePlacement);
////
////                            TowerDetails.getInstance().setSiteTechName(siteTechName);
////                            TowerDetails.getInstance().setSiteTechPhone(siteTechPhone);
////                            TowerDetails.getInstance().setSiteTechEmail(siteTechEmail);
////                            TowerDetails.getInstance().setSiteEngineerName(siteEngineerName);
////                            TowerDetails.getInstance().setSiteEngineerPhone(siteEngineerPhone);
////
////                            TowerDetails.getInstance().setSiteLatitude(siteLatitude);
////                            TowerDetails.getInstance().setSiteLongitude(siteLongitude);
////                            TowerDetails.getInstance().setSiteAddr1(siteAddr1);
////                            TowerDetails.getInstance().setSiteAddr2(siteAddr2);
////                            TowerDetails.getInstance().setSitePostOffice(sitePostOffice);
////
////                            TowerDetails.getInstance().setSiteZipCode(siteZipCode);
////                            TowerDetails.getInstance().setSiteDistrict(siteDistrict);
////                            TowerDetails.getInstance().setSiteState(siteState);
////                            TowerDetails.getInstance().setSiteCountry(siteCountry);
////                            TowerDetails.getInstance().setSiteStatusActive(siteStatusActive);
////
////                            TowerDetails.getInstance().setSiteType(siteType);
////                            TowerDetails.getInstance().setSiteLastUpdatedby(siteLastUpdatedby);
////                            TowerDetails.getInstance().setSiteLastUpdatedDate(siteLastUpdatedDate);
////                            TowerDetails.getInstance().setSiteClusterIndex(siteClusterIndex);
////                            TowerDetails.getInstance().setSiteInfrastructureIndex(siteInfrastructureIndex);
//
//                            TowerDetails.getInstance().setUserId(userId);
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

                            Log.e("visitIndexxxxxx---", VisitId.getInstance().getVisitId());


                            Log.e("visitIndexyyyyy---", visitIndex);

                            Log.e("preStatusName---", preStatusName);
                            Log.e("postStatusName---", postStatusName);
                            Log.e("approvedBy---", approvedBy);

                            int visitid1= Integer.parseInt(VisitId.getInstance().getVisitId());
                            int visitid2= Integer.parseInt(visitIndex);




                            if(visitid1 < visitid2){



                                if(preStatus ==0 && postStatus==3){

                                    Intent intent = new Intent(ApprovalActivity.this, SurveyStatus.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();


                                }
                                else if(preStatus ==0 && postStatus==5){

                                    Intent intent = new Intent(ApprovalActivity.this, WiringCompleted.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();


                                }
                                else if(preStatus==0 && postStatus==7) {

                                    Intent intent= new Intent(ApprovalActivity.this, TenantActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }

                                else if(preStatus==0 && postStatus==8){

                                    Intent intent= new Intent(ApprovalActivity.this, PowerUpSTAYDevice.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();

                                }

                                else if(preStatus==0 && postStatus==9){

                                    Intent intent= new Intent(ApprovalActivity.this, CheckDataActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();

                                }

                                else {


                                    Intent intent = new Intent(ApprovalActivity.this, RepresentingClient.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }

                            }

                            else{

                            Toast.makeText(ApprovalActivity.this, "Please wait for Coordinator approval ",
                                    Toast.LENGTH_SHORT).show();


                            }





                        }

                    } else if (success == 0) {

//                        Intent intent = new Intent(DashBoardActivity.this,MapActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(intent);
//                        finish();

                    } else if (success == 2) {

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
