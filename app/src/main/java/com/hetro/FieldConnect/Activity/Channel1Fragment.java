package com.hetro.FieldConnect.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hetro.FieldConnect.DtoController.Channel1;
import com.hetro.FieldConnect.Util.JSONParser;
import com.hetro.FieldConnect.Util.NetworkConnection;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Channel1Fragment extends Fragment implements OnRefreshListener {

	SwipeRefreshLayout swipeLayout;

	NetworkConnection networkConnection;

	Spinner c1selOpeeratorSpinner,c1numOperatorSpinner,c1FreqencySpinner,c1technologySpinner;

	Button ciSubmitButton;


	ListView dailyReportListview;
	ImageView calenderViewButton,backImageview;
	View rootView;
	private PopupWindow pwindo;

	String dateString;
	private ProgressDialog pDialog;

	JSONParser jsonParser = new JSONParser();

	//JSONArray daily_report = null;



	//ArrayList<HashMap<String, String>> reportList =null;

	ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();


	HashMap<String, String> map = new HashMap<String, String>();

	String selectedDate=null;
//	CalendarViewActivity calendarView;
	//int day,mon,yer;

	PopupMenu popupMenu;

	Button c1SubmitButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.channel1, container, false);


	//	backImageview=(ImageView) rootView.findViewById(R.id.daily_back_imageview);
		c1selOpeeratorSpinner=(Spinner) rootView.findViewById(R.id.c1operator_spinner);
		c1numOperatorSpinner=(Spinner) rootView.findViewById(R.id.c1operatorspinner);
		c1FreqencySpinner  = (Spinner) rootView.findViewById(R.id.c1frequencyspinner);
		c1technologySpinner = (Spinner) rootView.findViewById(R.id.c1technologyspinner);

		backImageview =(ImageView) rootView.findViewById(R.id.c1back);

	//	c1SubmitButton = (Button) rootView.findViewById(R.id.c1submit_button);

		String [] operator =
				{"Select Operator","IDEA","Vodafone","Airtel","Aircel","Reliance","Tata Docomo","Uninor","TTML-G","BSNL"};

		ArrayAdapter<String> operatorArrayAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, operator);
		operatorArrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		c1selOpeeratorSpinner.setAdapter(operatorArrayAdapter);

		String [] values =
				{"Number of Operator","1","2","3","4","5","6","7"};

		ArrayAdapter<String> selArrayAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, values);
		selArrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		c1numOperatorSpinner.setAdapter(selArrayAdapter);


		String [] frequency =
				{"Frequency of Operator","900","800","1800","2100","2000"};

		ArrayAdapter<String> freArrayAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, frequency);
		freArrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		c1FreqencySpinner.setAdapter(freArrayAdapter);

		String [] technology =
				{"Select Technology","2g","3g","4g"};

		ArrayAdapter<String> techArrayAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, technology);
		techArrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		c1technologySpinner.setAdapter(techArrayAdapter);


		networkConnection = new NetworkConnection(getActivity());

		c1selOpeeratorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int pos,
									   long id) {

				if(pos!=0) {

					Channel1.getInstance().setC1Operator(parent.getItemAtPosition(pos).toString());

//					Toast.makeText(parent.getContext(),
//							"On Item Select : \n" + parent.getItemAtPosition(pos).toString(),
//							Toast.LENGTH_LONG).show();
				}
				else{
					Channel1.getInstance().setC1Operator("No Sensor");
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		c1numOperatorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int pos,
									   long id) {

				if(pos!=0) {

					Channel1.getInstance().setC1Operno(parent.getItemAtPosition(pos).toString());
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

		c1FreqencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int pos,
									   long id) {

				if(pos!=0) {

					Channel1.getInstance().setC1Freq(parent.getItemAtPosition(pos).toString());
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

		c1technologySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int pos,
									   long id) {


				if(pos!=0) {

					Channel1.getInstance().setC1Tech(parent.getItemAtPosition(pos).toString());
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

		backImageview.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(getActivity(),DashBoardActivity.class);
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



	private void populateScheduleVenueDetails(String selectedDate){

	}
}
