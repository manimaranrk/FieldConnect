package com.hetro.FieldConnect.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.hetro.FieldConnect.DTO.TowerDetailDto;
import com.hetro.FieldConnect.DtoController.ImagePath;
import com.hetro.FieldConnect.DtoController.LoginStatus;
import com.hetro.FieldConnect.DtoController.TowerDetail;
import com.hetro.FieldConnect.DtoController.TowerDetails;
import com.hetro.FieldConnect.DtoController.UserDetails;
import com.hetro.FieldConnect.Util.ApplicationConstants;
import com.hetro.FieldConnect.Util.GPSTracker1;
import com.hetro.FieldConnect.Util.JSONParser;
import com.hetro.FieldConnect.Util.NetworkConnection;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CaptureImage extends Activity{

    Button imageButton,submit_image;

    String pssword,userId,userName,preStatusName,postStatusName,scheduleDate,visitDate,towerSiteId,teamId,reMarks,
            towerLatiTude,towerLogniTude,approvedBy,completeStatus,visitIndex;
    int preStatus,postStatus;
    String prestatus;

    GPSTracker1 gps;

    double latitude,longitude;

//    Bitmap bitmap;

 //   String[] imagePath;
   // ArrayList<String> imagePathList = new ArrayList<String>();

    private ProgressDialog p1Dialog,p2Dialog;
    NetworkConnection networkConnection;
    JSONParser jsonParser = new JSONParser();

  //  byte[] image;
    String siteId=null,uniqId=null;

   // String fileName;


    public static final String GridViewDemo_ImagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/FieldConnect/";

    final int CAMERA_CAPTURE = 1;
    private Uri picUri;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private GridView grid;
    private  List<String> listOfImagesPath;

    String imgpath,imagepath;

   // String encodedString;
   // String encodedString1;

   // ProgressDialog prgDialog;
    //byte[] byteArrey;

    String ba1;

    public static final String MyPREFERENCES = "MyPrefs" ;

    public static final String TowerId = "towerId";
    public static final String UniqId = "uniqId";

    public static final String MyPREFERENCES1 = "FieldConnectLogin" ;
    public static final String userNameKey = "userNameKey";
    public static final String passwordKey = "passwordKey";

    public static final String preStatusKey = "preStatusKey";

    public static final String postStatusKey = "postStatusKey";
    public static final String loginstatusKey = "loginstatusKey";

    SharedPreferences sharedpreferences,sharedpreferences1;

    String towerId;

    private ProgressDialog pDialog;

    String userCircle,groupIndex;

    JSONArray tower = null;

    ImageView back;

    PopupMenu popupMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.capture_image);

        imageButton=(Button)findViewById(R.id.image_button);
        submit_image=(Button)findViewById(R.id.image_submit);
        grid = (GridView) findViewById(R.id.gridviewimg);
        back = (ImageView)findViewById(R.id.caback);

        networkConnection = new NetworkConnection(this);

        preStatus = TowerDetails.getInstance().getPreStatus();
        postStatus = TowerDetails.getInstance().getPostStatus();
        siteId = TowerDetail.getInstance().getTowerSiteId();
        uniqId = TowerDetail.getInstance().getUniqId();

        userCircle = UserDetails.getInstance().getCircle();
        groupIndex = UserDetails.getInstance().getGroupIndex();

        pssword = LoginStatus.getInstance().getPassword();
        userId = TowerDetails.getInstance().getUserId();
        userName = LoginStatus.getInstance().getUsername();

        preStatusName = TowerDetails.getInstance().getPreStatusName();
        postStatusName = TowerDetails.getInstance().getPostStatusName();

        Log.e("imageee preStatus-----",preStatus+"");
        Log.e("image postStatus-----",postStatus+"");
    //    Log.e("siteId-----",siteId);
     //   Log.e("uniqId-----",uniqId);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        sharedpreferences1 = getSharedPreferences(MyPREFERENCES1, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString(userNameKey, userName);
        editor.putString(passwordKey, pssword);
        editor.putString(preStatusKey, preStatus+"");
        editor.putInt(postStatusKey, postStatus);
        editor.putInt(loginstatusKey, 0);

//        userName =sharedpreferences1.getString(userNameKey, "");
//        pssword = sharedpreferences1.getString(passwordKey, "");
//        preStatusKey


        towerId = sharedpreferences.getString(TowerId, "");
        uniqId = sharedpreferences.getString(UniqId, "");

        if(siteId=="" && uniqId==""){

            Intent intent = new Intent(CaptureImage.this, DashBoardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

        }

        try {
            listOfImagesPath = RetriveCapturedImagePath();
            if (listOfImagesPath != null) {

                for (int i = 0; i < listOfImagesPath.size() - 1; i++) {
                    Log.i("Value of element--==== " + i, listOfImagesPath.get(i));
                }
                // ImagePath.getInstance().setImagePathList(imagePathList);

                grid.setAdapter(new ImageListAdapter(this, listOfImagesPath));
            }
        }
        catch (Exception e){

        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CaptureImage.this,DashBoardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }
        });


//        prgDialog = new ProgressDialog(this);
//        // Set Cancelable as False
//        prgDialog.setCancelable(false);

        submit_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    gps = new GPSTracker1(CaptureImage.this);

                    if (gps.canGetLocation()) {

                        latitude = gps.getLatitude();
                        longitude = gps.getLongitude();

                        for (int i = 0; i < listOfImagesPath.size(); i++) {
                            imgpath = listOfImagesPath.get(i);
                            if (imgpath != null) {
                                try {

                                    towerId = sharedpreferences.getString(TowerId, "");
                                    uniqId = sharedpreferences.getString(UniqId, "");

                                    String image = upload();

                                    if(!image.equals("")) {
                                        new UploadImage().execute(image);
                                    }

//                                    if (i < listOfImagesPath.size()) {
//
//                                    }


                                } catch (Exception e) {
                                    Log.e("eeee####----",e.toString());

                                }
                            } else {
                                Toast.makeText(
                                        getApplicationContext(),
                                        "You must select image from gallery before you try to upload",
                                        Toast.LENGTH_LONG).show();
                            }
                        }

//                        userName = sharedpreferences1.getString(userNameKey, "");
//                        pssword = sharedpreferences1.getString(passwordKey, "");
//                        String prStatus = sharedpreferences1.getString(preStatusKey, "");
//                        int pstStatus = sharedpreferences1.getInt(postStatusKey, 0);
//
//                        //    preStatus = Integer.parseInt(prStatus);
//                        postStatus = pstStatus;
//
//                        //   pssword = LoginStatus.getInstance().getPassword();
//                        //     userId = TowerDetails.getInstance().getUserId();
//                        //    userName = LoginStatus.getInstance().getUsername();
//
//                        preStatusName = TowerDetails.getInstance().getPreStatusName();
//                        postStatusName = TowerDetails.getInstance().getPostStatusName();
//                        //   preStatus = TowerDetails.getInstance().getPreStatus();
//                        //     postStatus = TowerDetails.getInstance().getPostStatus();
//                        scheduleDate = TowerDetails.getInstance().getScheduleDate();
//                        visitDate = TowerDetails.getInstance().getVisitDate();
//                        towerSiteId = TowerDetails.getInstance().getTowerSiteId();
//                        teamId = TowerDetails.getInstance().getTeamId();
//                        reMarks = TowerDetails.getInstance().getReMarks();
//                        towerLatiTude = TowerDetails.getInstance().getLatiTude();
//                        towerLogniTude = TowerDetails.getInstance().getLongiTude();
//                        approvedBy = TowerDetails.getInstance().getApprovedBy();
//                        completeStatus = TowerDetails.getInstance().getCompleteStatus();
//                        visitIndex = TowerDetails.getInstance().getVisitIndex();
//
//                        prestatus = "7";
//
//                        new updateStatus().execute();

                        // \n is for new line
                        // Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                    } else {
                        // can't get location
                        // GPS or Network is not enabled
                        // Ask user to enable GPS/network in settings
                        gps.showSettingsAlert();
                    }
                }
                catch (Exception e){

                    Log.e("imageeee--",e.toString());

                }
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if(siteId.equals("") && siteId.length()==0 && siteId == null && siteId.isEmpty() ) {
//
//                    Intent intent = new Intent(CaptureImage.this,DashBoardActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
//                    finish();
//
//                }else {

                    try {
//use standard intent to capture an image
                        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


//we will handle the returned data in onActivityResult
                        startActivityForResult(captureIntent, CAMERA_CAPTURE);
                    } catch (ActivityNotFoundException anfe) {
//display an error message
                        String errorMessage = "Whoops - your device doesn't support capturing images!";
                        Toast toast = Toast.makeText(CaptureImage.this, errorMessage, Toast.LENGTH_SHORT);
                        toast.show();
                    }
             //   }
            }
        });
    }

    public void onBackPressed() {

        File dir = new File(GridViewDemo_ImagePath+"/"+towerId);
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            for (int j = 0; j < children.length; j++)
            {
                new File(dir, children[j]).delete();
            }
        }

    }

    public void casubmit(View view){


    }

    public void camenuButtonClickEvent(View view){

        popupMenu = new PopupMenu(CaptureImage.this, view);
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

    class CheckValidUser extends AsyncTask<String, String, JSONObject> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CaptureImage.this);
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
                Toast.makeText(CaptureImage.this,
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
                    Intent intent=new Intent(CaptureImage.this, MainActivity.class);
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
//user is returning from capturing an image using the camera
            if(requestCode == CAMERA_CAPTURE){
                Bundle extras = data.getExtras();
                Bitmap thePic = extras.getParcelable("data");

                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

                towerId = sharedpreferences.getString(TowerId, "");



                String imgcurTime = dateFormat.format(new Date());
                File imageDirectory = new File(GridViewDemo_ImagePath+"/"+towerId+"/");
                imageDirectory.mkdirs();
                String _path = GridViewDemo_ImagePath+"/"+towerId+"/" + imgcurTime+".jpg";

                Log.e("eeeeeeeeeee----",_path);



                try {
                    FileOutputStream out = new FileOutputStream(_path);
                    thePic.compress(Bitmap.CompressFormat.JPEG, 90, out);
                    out.close();
                } catch (FileNotFoundException e) {
                    e.getMessage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
              //  listOfImagesPath = null;
               // listOfImagesPath
                listOfImagesPath = RetriveCapturedImagePath();
                if(listOfImagesPath!=null){

                    for (int i=0;i < listOfImagesPath.size()-1;i++)
                    {
                        Log.i("Value of element--==== "+i,listOfImagesPath.get(i));
                    }
                   // ImagePath.getInstance().setImagePathList(imagePathList);

                    grid.setAdapter(new ImageListAdapter(this,listOfImagesPath));
                }
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
            p2Dialog = new ProgressDialog(CaptureImage.this);
            p2Dialog.setMessage("update Status...");
            p2Dialog.setIndeterminate(true);
            p2Dialog.setCancelable(false);
            p2Dialog.show();

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
                p2Dialog.dismiss();
                Looper.prepare();
                Toast.makeText(CaptureImage.this,
                        "Network is not Available. Please Check Your Internet Connection ",
                        Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            return json1;
        }

        protected void onPostExecute(JSONObject json) {

            try {
                if ((p2Dialog != null) && p2Dialog.isShowing()) {
                    p2Dialog.dismiss();
                }

            } catch (final IllegalArgumentException e) {

            } catch (final Exception e) {

            } finally {
                p2Dialog = null;
            }

            try {
                int success = json.getInt(ApplicationConstants.TAG_SUCCESS);

                if (success == 1) {

                    Log.e("survey", "survey");

                    new CheckValidUser1().execute();



//                    Intent intent= new Intent(WiringCompleted.this, CaptureImage.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
//                    finish();


                }
                else if(success == 3){

                    Log.e("survey", "dataaaa");

                    new CheckValidUser1().execute();



//                    Intent intent= new Intent(WiringCompleted.this, CaptureImage.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
//                    finish();



                }

                else if(success == 0){

                    Log.e("survey","dataaaa");

                    Intent intent= new Intent(CaptureImage.this, DashBoardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();



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
            pDialog = new ProgressDialog(CaptureImage.this);
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

                              //  File f = new File(GridViewDemo_ImagePath+"/"+towerId+"/");

                                //GridViewDemo_ImagePath+"/"+towerId+"/"

                                File dir = new File(GridViewDemo_ImagePath+"/"+towerId);
                                if (dir.isDirectory())
                                {
                                    String[] children = dir.list();
                                    for (int j = 0; j < children.length; j++)
                                    {
                                        new File(dir, children[j]).delete();
                                    }
                                }

                                Intent intent = new Intent(CaptureImage.this, ApprovalActivity.class);
                                startActivity(intent);
                                finish();


                            }else {

//                                Intent intent = new Intent(SurveyDataActivity.this, SurveyStatus.class);
//                                startActivity(intent);
//                                finish();
                            }

//                            Intent intent = new Intent(CaptureImage.this, TenantActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(intent);
//                            finish();



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


    private String  upload() {
        // Image location URL
        Log.i("path", "----------------" + imgpath);
        // Image
        Bitmap bm = BitmapFactory.decodeFile(imgpath);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 90, bao);
        byte[] ba = bao.toByteArray();
        ba1 = Base64.encodeToString(ba, 0);
        Log.i("base64", "-----" + ba1);
//        new UploadImage().execute();


        return ba1;
    }

    class UploadImage extends AsyncTask<String, String, JSONObject> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p1Dialog = new ProgressDialog(CaptureImage.this);
            p1Dialog.setMessage("Image Upload...");
            p1Dialog.setIndeterminate(true);
            p1Dialog.setCancelable(false);
            p1Dialog.show();
        }

        /**
         * Creating product
         * */
        protected JSONObject doInBackground(String... args) {

            JSONObject json = null;

            try {

                String imageButes=args[0];

                if (networkConnection.isNetworkAvailable()) {

                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("uniqid", uniqId));
                    params.add(new BasicNameValuePair("image", imageButes));
                    params.add(new BasicNameValuePair("siteid", towerId));

                    json = jsonParser.makeHttpRequest(ApplicationConstants.url_image_upload,
                            "POST", params);
                }
            }
            catch (Exception e){
                Log.e("error--", e.toString());
            }

            return json;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(JSONObject json) {

            try {
                if ((p1Dialog != null) && p1Dialog.isShowing()) {
                    p1Dialog.dismiss();
                }

            } catch (final IllegalArgumentException e) {

            } catch (final Exception e) {

            } finally {
                p1Dialog = null;
            }


            try {
                int success = json.getInt(ApplicationConstants.TAG_SUCCESS);

                if (success == 1) {


                 //   for (int i = 0; i < listOfImagesPath.size(); i++) {



//                            Toast.makeText(CaptureImage.this,
//                                    " j---"+i,
//                                    Toast.LENGTH_SHORT).show();

                            userName = sharedpreferences1.getString(userNameKey, "");
                            pssword = sharedpreferences1.getString(passwordKey, "");
                            String prStatus = sharedpreferences1.getString(preStatusKey, "");
                            int pstStatus = sharedpreferences1.getInt(postStatusKey, 0);

                            //    preStatus = Integer.parseInt(prStatus);
                            postStatus = pstStatus;

                            //   pssword = LoginStatus.getInstance().getPassword();
                            //     userId = TowerDetails.getInstance().getUserId();
                            //    userName = LoginStatus.getInstance().getUsername();

                            preStatusName = TowerDetails.getInstance().getPreStatusName();
                            postStatusName = TowerDetails.getInstance().getPostStatusName();
                            //   preStatus = TowerDetails.getInstance().getPreStatus();
                            //     postStatus = TowerDetails.getInstance().getPostStatus();
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

                            prestatus = "7";

                            new updateStatus().execute();







                //    }

               //     p1Dialog.dismiss();

//                    File file= new File(imgpath);
//                    if(file.exists())
//                    {
//                        file.delete();
//                    }



//                    Toast.makeText(CaptureImage.this,
//                            "Image Uploaded",
//                            Toast.LENGTH_SHORT).show();
                }
                else if(success == 0) {

                }
                else if(success == 2) {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private List<String> RetriveCapturedImagePath() {
        List<String> tFileList = new ArrayList<String>();
        File f = new File(GridViewDemo_ImagePath+"/"+towerId+"/");
        if (f.exists()) {
            File[] files=f.listFiles();
            Arrays.sort(files);

            for(int i=0; i<files.length; i++){
                File file = files[i];
                if(file.isDirectory())
                    continue;
                tFileList.add(file.getPath());
            }
        }
        return tFileList;
    }

    public class ImageListAdapter extends BaseAdapter
    {
        private Context context;
        private List<String> imgPic;
        public ImageListAdapter(Context c, List<String> thePic)
        {
            context = c;
            imgPic = thePic;
        }
        public int getCount() {
            if(imgPic != null)
                return imgPic.size();
            else
                return 0;
        }

        //---returns the ID of an item---
        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        //---returns an ImageView view---
        public View getView(int position, View convertView, ViewGroup parent)
        {
            ImageView imageView;
            BitmapFactory.Options bfOptions=new BitmapFactory.Options();
            bfOptions.inDither=false;                     //Disable Dithering mode
            bfOptions.inPurgeable=true;                   //Tell to gc that whether it needs free memory, the Bitmap can be cleared
            bfOptions.inInputShareable=true;              //Which kind of reference will be used to recover the Bitmap data after being clear, when it will be used in the future
            bfOptions.inTempStorage=new byte[32 * 1024];
            if (convertView == null) {
                imageView = new ImageView(context);
                imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                imageView.setPadding(0, 0, 0, 0);
            } else {
                imageView = (ImageView) convertView;
            }
            FileInputStream fs = null;
            Bitmap bm;
            try {
                fs = new FileInputStream(new File(imgPic.get(position).toString()));

                if(fs!=null) {
                    bm=BitmapFactory.decodeFileDescriptor(fs.getFD(), null, bfOptions);
                    imageView.setImageBitmap(bm);
                    imageView.setId(position);
                    imageView.setLayoutParams(new GridView.LayoutParams(200, 160));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally{
                if(fs!=null) {
                    try {
                        fs.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return imageView;
        }

//        AdapterView.OnItemClickListener myOnItemClickListener
//                = new AdapterView.OnItemClickListener(){
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position,
//                                    long id) {
//                String prompt = (String)parent.getItemAtPosition(position);
//                Toast.makeText(getApplicationContext(),
//                        prompt,
//                        Toast.LENGTH_LONG).show();
//
//            }};
    }
}
