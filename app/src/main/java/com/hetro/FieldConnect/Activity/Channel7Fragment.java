package com.hetro.FieldConnect.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Toast;

import com.hetro.FieldConnect.DtoController.Channel1;
import com.hetro.FieldConnect.DtoController.Channel2;
import com.hetro.FieldConnect.DtoController.Channel3;
import com.hetro.FieldConnect.DtoController.Channel4;
import com.hetro.FieldConnect.DtoController.Channel5;
import com.hetro.FieldConnect.DtoController.Channel6;
import com.hetro.FieldConnect.DtoController.Channel7;
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

import java.nio.channels.Channel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Channel7Fragment extends Fragment implements OnRefreshListener {

	SwipeRefreshLayout swipeLayout;

	NetworkConnection networkConnection;

	Spinner c7selOpeeratorSpinner,c7numOperatorSpinner,c7FreqencySpinner,c7technologySpinner;

	Button ciSubmitButton;

	ListView dailyReportListview;
	ImageView calenderViewButton,backImageview;
	View rootView;
	private PopupWindow pwindo;

	String dateString;
	private ProgressDialog pDialog;

	JSONParser jsonParser = new JSONParser();

	//JSONArray daily_report = null;

	String pssword,userId,userName,preStatusName,postStatusName,scheduleDate,visitDate,towerSiteId,teamId,reMarks,
			towerLatiTude,towerLogniTude,approvedBy,completeStatus,visitIndex;

	int preStatus,postStatus;

	String prestatus;

	//ArrayList<HashMap<String, String>> reportList =null;

	ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();

	HashMap<String, String> map = new HashMap<String, String>();

	String selectedDate=null;
//	CalendarViewActivity calendarView;
	//int day,mon,yer;

	PopupMenu popupMenu;

	Button c7SubmitButton;

	GPSTracker1 gps;

	double latitude,longitude;

	String groupIndex,userCircle;
	JSONArray tower = null;

	public static final String MyPREFERENCES = "MyPrefs" ;

	public static final String TowerId = "towerId";

	SharedPreferences sharedpreferences;

	String towerId;

	String c1Oper,c1SelOperNo,c1Freq,c1Tech,c2Oper,c2SelOperNo,c2Freq,c2Tech,c3Oper,c3SelOperNo,
			c3Freq,c3Tech,c4Oper,c4SelOperNo,c4Freq,c4Tech,c5Oper,c5SelOperNo,c5Freq,c5Tech,
			c6Oper,c6SelOperNo,c6Freq,c6Tech,c7Oper,c7SelOperNo,c7Freq,c7Tech;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.channel7, container, false);

	//	backImageview=(ImageView) rootView.findViewById(R.id.daily_back_imageview);
		c7selOpeeratorSpinner=(Spinner) rootView.findViewById(R.id.c7operator_spinner);
		c7numOperatorSpinner=(Spinner) rootView.findViewById(R.id.c7operatorspinner);
		c7FreqencySpinner  = (Spinner) rootView.findViewById(R.id.c7frequencyspinner);
		c7technologySpinner = (Spinner) rootView.findViewById(R.id.c7technologyspinner);

		backImageview =(ImageView) rootView.findViewById(R.id.c7back);

		c7SubmitButton = (Button) rootView.findViewById(R.id.c7submit_button);

		sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

		towerId = sharedpreferences.getString(TowerId, "");

		String [] operator =
				{"Select Operator","IDEA","Vodafone","Airtel","Aircel","Reliance","Tata Docomo","Uninor","TTML-G","BSNL"};

		ArrayAdapter<String> operatorArrayAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, operator);
		operatorArrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		c7selOpeeratorSpinner.setAdapter(operatorArrayAdapter);

		String [] values =
				{"Number of Operator","1","2","3","4","5","6","7"};

		ArrayAdapter<String> selArrayAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, values);
		selArrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		c7numOperatorSpinner.setAdapter(selArrayAdapter);

		String [] frequency =
				{"Frequency of Operator","900","800","1800","2100","2000"};

		ArrayAdapter<String> freArrayAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, frequency);
		freArrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		c7FreqencySpinner.setAdapter(freArrayAdapter);

		String [] technology =
				{"Select Technology","2g","3g","4g"};

		ArrayAdapter<String> techArrayAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, technology);
		techArrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		c7technologySpinner.setAdapter(techArrayAdapter);

		networkConnection = new NetworkConnection(getActivity());

		c7selOpeeratorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int pos,
									   long id) {

				if(pos!=0) {

					Channel7.getInstance().setC7Operator(parent.getItemAtPosition(pos).toString());

//					Toast.makeText(parent.getContext(),
//							"On Item Select : \n" + parent.getItemAtPosition(pos).toString(),
//							Toast.LENGTH_LONG).show();
				}
				else{
					Channel7.getInstance().setC7Operator("No Sensor");
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		c7numOperatorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int pos,
									   long id) {

				if (pos != 0) {

					Channel7.getInstance().setC7Operno(parent.getItemAtPosition(pos).toString());
//					Toast.makeText(parent.getContext(),
//							"On Item Select : \n" + parent.getItemAtPosition(pos).toString(),
//							Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});

		c7FreqencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int pos,
									   long id) {

				if (pos != 0) {
					Channel7.getInstance().setC7Freq(parent.getItemAtPosition(pos).toString());
//					Toast.makeText(parent.getContext(),
//							"On Item Select : \n" + parent.getItemAtPosition(pos).toString(),
//							Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});

		c7technologySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int pos,
									   long id) {

				if(pos!=0) {

					Channel7.getInstance().setC7Tech(parent.getItemAtPosition(pos).toString());
//					Toast.makeText(parent.getContext(),
//							"On Item Select : \n" + parent.getItemAtPosition(pos).toString(),
//							Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});

		c7SubmitButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				gps = new GPSTracker1(getActivity());
				if (gps.canGetLocation()) {

					preStatus = TowerDetails.getInstance().getPreStatus();
					postStatus = TowerDetails.getInstance().getPostStatus();
					groupIndex = UserDetails.getInstance().getGroupIndex();
					userCircle = UserDetails.getInstance().getCircle();
					latitude = gps.getLatitude();
					longitude = gps.getLongitude();
					c1Oper = Channel1.getInstance().getC1Operator();
					if(c1Oper.equals("")){
						c1Oper="No Sensor";
					}
					c1SelOperNo = Channel1.getInstance().getC1Operno();
					c1Freq = Channel1.getInstance().getC1Freq();
					c1Tech = Channel1.getInstance().getC1Tech();
					c2Oper = Channel2.getInstance().getC2Operator();
					if(c2Oper.equals("")){
						c2Oper="No Sensor";
					}
					c2SelOperNo = Channel2.getInstance().getC2Operno();
					c2Freq = Channel2.getInstance().getC2Freq();
					c2Tech = Channel2.getInstance().getC2Tech();
					c3Oper = Channel3.getInstance().getC3Operator();
					if(c3Oper.equals("")){
						c3Oper="No Sensor";
					}
					c3SelOperNo = Channel3.getInstance().getC3Operno();
					c3Freq = Channel3.getInstance().getC3Freq();
					c3Tech = Channel3.getInstance().getC3Tech();
					c4Oper = Channel4.getInstance().getC4Operator();
					if(c4Oper.equals("")){
						c4Oper="No Sensor";
					}
					c4SelOperNo = Channel4.getInstance().getC4Operno();
					c4Freq = Channel4.getInstance().getC4Freq();
					c4Tech = Channel4.getInstance().getC4Tech();
					c5Oper = Channel5.getInstance().getC5Operator();
					if(c5Oper.equals("")){
						c5Oper="No Sensor";
					}
					c5SelOperNo = Channel5.getInstance().getC5Operno();
					c5Freq = Channel5.getInstance().getC5Freq();
					c5Tech = Channel5.getInstance().getC5Tech();
					c6Oper = Channel6.getInstance().getC6Operator();
					if(c6Oper.equals("")){
						c6Oper="No Sensor";
					}
					c6SelOperNo = Channel6.getInstance().getC6Operno();
					c6Freq = Channel6.getInstance().getC6Freq();
					c6Tech = Channel6.getInstance().getC6Tech();
					c7Oper = Channel7.getInstance().getC7Operator();
					if(c7Oper.equals("")){
						c7Oper="No Sensor";
					}
					c7SelOperNo = Channel7.getInstance().getC7Operno();
					c7Freq = Channel7.getInstance().getC7Freq();
					c7Tech = Channel7.getInstance().getC7Tech();

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

					Toast.makeText(getActivity(),
							"towerSiteId-- " + towerSiteId,
							Toast.LENGTH_SHORT).show();

					prestatus = "8";

					new updateStatus().execute();


					// \n is for new line
					// Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
				} else {
					// can't get location
					// GPS or Network is not enabled
					// Ask user to enable GPS/network in settings
					gps.showSettingsAlert();
				}


			}
		});

		backImageview.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(getActivity(), DashBoardActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(intent);
				getActivity().finish();

			}
		});



		return rootView;
	}

	@Override
	public void onRefresh() {
	//	new LoadReport().execute();
	}

	class updateTenantDetails extends AsyncTask<String, String, JSONObject> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("update Tenant Details...");
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

				String userId = UserDetails.getInstance().getId();

//				 c1Oper,c1SelOperNo,c1Freq,c1Tech,c2Oper,c2SelOperNo,c2Freq,c2Tech,c3Oper,c3SelOperNo,
//						c3Freq,c3Tech,c4Oper,c4SelOperNo,c4Freq,c4Tech,c5Oper,c5SelOperNo,c5Freq,c5Tech,
//						c6Oper,c6SelOperNo,c6Freq,c6Tech,c7Oper,c7SelOperNo,c7Freq,c7Tech;

				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("towersiteid", towerSiteId));
				params.add(new BasicNameValuePair("c1oper", c1Oper));
//				params.add(new BasicNameValuePair("c1seloperno", c1SelOperNo));
//				params.add(new BasicNameValuePair("c1freq", c1Freq));
//				params.add(new BasicNameValuePair("c1tech", c1Tech));

				params.add(new BasicNameValuePair("c2oper", c2Oper));
//				params.add(new BasicNameValuePair("c2seloperno", c2SelOperNo));
//				params.add(new BasicNameValuePair("c2freq", c2Freq));
//				params.add(new BasicNameValuePair("c2tech", c2Tech));

				params.add(new BasicNameValuePair("c3oper", c3Oper));
//				params.add(new BasicNameValuePair("c3seloperno", c3SelOperNo));
//				params.add(new BasicNameValuePair("c3freq", c3Freq));
//				params.add(new BasicNameValuePair("c3tech", c3Tech));

				params.add(new BasicNameValuePair("c4oper", c4Oper));
//				params.add(new BasicNameValuePair("c4seloperno", c4SelOperNo));
//				params.add(new BasicNameValuePair("c4freq", c4Freq));
//				params.add(new BasicNameValuePair("c4tech", c4Tech));

				params.add(new BasicNameValuePair("c5oper", c5Oper));
//				params.add(new BasicNameValuePair("c5seloperno", c5SelOperNo));
//				params.add(new BasicNameValuePair("c5freq", c5Freq));
//				params.add(new BasicNameValuePair("c5tech", c5Tech));

				params.add(new BasicNameValuePair("c6oper", c6Oper));
//				params.add(new BasicNameValuePair("c6seloperno", c6SelOperNo));
//				params.add(new BasicNameValuePair("c6freq", c6Freq));
//				params.add(new BasicNameValuePair("c6tech", c6Tech));

				params.add(new BasicNameValuePair("c7oper", c7Oper));
//				params.add(new BasicNameValuePair("c7seloperno", c7SelOperNo));
//				params.add(new BasicNameValuePair("c7freq", c7Freq));
//				params.add(new BasicNameValuePair("c7tech", c7Tech));
				params.add(new BasicNameValuePair("username", userName));


				json1 = jsonParser.makeHttpRequest(ApplicationConstants.url_tenant_details1,
						"POST", params);

				Log.e("Update Status", json1.toString());
			}
			else {
				pDialog.dismiss();
				Looper.prepare();
				Toast.makeText(getActivity(),
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
					//new CheckValidUser1().execute();
					Log.e("survey","survey");
				}
				if (success == 3) {
					new CheckValidUser1().execute();
					//new CheckValidUser1().execute();
					Log.e("survey","survey");
				}
//				else if(success == 3){
//
//					Log.e("survey", "dataaaa");
//				//	new CheckValidUser1().execute();
//				}

			} catch (JSONException e) {
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
			pDialog = new ProgressDialog(getActivity());
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
				Toast.makeText(getActivity(),
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

					new updateTenantDetails().execute();

				//	new CheckValidUser1().execute();
					Log.e("survey","survey");
				}
				else if(success == 3){

					Log.e("survey", "dataaaa");
					new updateTenantDetails().execute();
				//	new CheckValidUser1().execute();
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
			pDialog = new ProgressDialog(getActivity());
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


							if(approvedBy.equals("1")) {

								Intent intent = new Intent(getActivity(), ApprovalActivity.class);
								startActivity(intent);
								getActivity().finish();


							}else {

//                                Intent intent = new Intent(SurveyDataActivity.this, SurveyStatus.class);
//                                startActivity(intent);
//                                finish();
							}

//							Intent intent= new Intent(SurveyStatus.this, SurveyDataActivity.class);
//							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//							startActivity(intent);
//							finish();



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










	private void populateScheduleVenueDetails(String selectedDate){

	}
}
