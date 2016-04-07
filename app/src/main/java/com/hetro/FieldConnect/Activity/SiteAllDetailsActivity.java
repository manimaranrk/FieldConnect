package com.hetro.FieldConnect.Activity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.hetro.FieldConnect.DtoController.TowerDetails;
import com.hetro.FieldConnect.DtoController.UserDetails;
import com.hetro.FieldConnect.Util.ApplicationConstants;
import com.hetro.FieldConnect.Util.JSONParser;
import com.hetro.FieldConnect.Util.NetworkConnection;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SiteAllDetailsActivity extends Activity {

    Button startInstaillButton,siteDetailButton;

    TextView siteIdTextview,siteNameTextview,addressTextview,engineerTextview,engineerPhoneTextview,idodTextview;


    ImageView backButton;

    PopupMenu popupMenu;
    JSONParser jsonParser = new JSONParser();
    NetworkConnection networkConnection;
    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.site_all_details);

        siteIdTextview = (TextView)findViewById(R.id.tsiteid_textview);
        siteNameTextview  = (TextView)findViewById(R.id.tsitename_textview);
        addressTextview = (TextView)findViewById(R.id.ttoweraddress);
        engineerTextview = (TextView)findViewById(R.id.tengeename);
        engineerPhoneTextview = (TextView)findViewById(R.id.tengeephone);
        idodTextview = (TextView)findViewById(R.id.tidod);

//        toweTypetextview = (TextView)findViewById(R.id.ttowertype);
//        techNameTextview = (TextView)findViewById(R.id.ttechname);
//        techphoneTextview = (TextView)findViewById(R.id.ttechphone);
//        techEmailTextview = (TextView)findViewById(R.id.ttechemail);
//        engineerNameTextview = (TextView)findViewById(R.id.tengeneername);
//        engineerPhoneTextview = (TextView)findViewById(R.id.tengeneerphone);
//        engineerAddressTextview = (TextView)findViewById(R.id.tengeneeraddress);

        backButton = (ImageView)findViewById(R.id.tback);

        networkConnection = new NetworkConnection(this);

        siteIdTextview.setText(TowerDetails.getInstance().getTowerSiteId());
        siteNameTextview.setText(TowerDetails.getInstance().getTowerSiteName());
        addressTextview.setText(TowerDetails.getInstance().getTowerAddress());
        engineerTextview.setText(TowerDetails.getInstance().getTowerEngineerName());
        engineerPhoneTextview.setText(TowerDetails.getInstance().getTowerEngineerPhone());
        idodTextview.setText(TowerDetails.getInstance().getTowerIdOd());

//        String siteName =  TowerDetails.getInstance().getSiteName();
//
//        String siteId =  TowerDetails.getInstance().getSiteId();
//
//        siteNameTextview.setText(siteName);
//
//        siteIdTextview.setText(siteId);
//
//        toweTypetextview.setText(TowerDetails.getInstance().getTowerType());
//        techNameTextview.setText(TowerDetails.getInstance().getSiteTechName());
//        techphoneTextview.setText(TowerDetails.getInstance().getSiteTechPhone());
//        techEmailTextview.setText(TowerDetails.getInstance().getSiteTechEmail());
//        engineerNameTextview.setText(TowerDetails.getInstance().getSiteEngineerName());
//        engineerPhoneTextview.setText(TowerDetails.getInstance().getSiteEngineerPhone());
//        engineerAddressTextview.setText(TowerDetails.getInstance().getSiteAddr1());

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(SiteAllDetailsActivity.this,SiteDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                finish();

            }
        });

    }

    public void fmenuButtonClickEvent(View view){

        popupMenu = new PopupMenu(SiteAllDetailsActivity.this, view);
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

    class CheckValidUser extends AsyncTask<String, String, JSONObject> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SiteAllDetailsActivity.this);
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
                Toast.makeText(SiteAllDetailsActivity.this,
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

                    Intent intent=new Intent(SiteAllDetailsActivity.this, MainActivity.class);
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


}
