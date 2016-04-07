package com.hetro.FieldConnect.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
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
import com.hetro.FieldConnect.DtoController.Channel4;
import com.hetro.FieldConnect.Util.JSONParser;
import com.hetro.FieldConnect.Util.NetworkConnection;

import java.util.ArrayList;
import java.util.HashMap;


public class Channel4Fragment extends Fragment implements OnRefreshListener {

	SwipeRefreshLayout swipeLayout;

	NetworkConnection networkConnection;

	Spinner c4selOpeeratorSpinner,c4numOperatorSpinner,c4FreqencySpinner,c4technologySpinner;

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

	Button c4SubmitButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.channel4, container, false);


	//	backImageview=(ImageView) rootView.findViewById(R.id.daily_back_imageview);
		c4selOpeeratorSpinner=(Spinner) rootView.findViewById(R.id.c4operator_spinner);
		c4numOperatorSpinner=(Spinner) rootView.findViewById(R.id.c4operatorspinner);
		c4FreqencySpinner  = (Spinner) rootView.findViewById(R.id.c4frequencyspinner);
		c4technologySpinner = (Spinner) rootView.findViewById(R.id.c4technologyspinner);

		backImageview =(ImageView) rootView.findViewById(R.id.c4back);

	//	c4SubmitButton = (Button) rootView.findViewById(R.id.c4submit_button);

		String [] operator =
				{"Select Operator","IDEA","Vodafone","Airtel","Aircel","Reliance","Tata Docomo","Uninor","TTML-G","BSNL"};

		ArrayAdapter<String> operatorArrayAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, operator);
		operatorArrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		c4selOpeeratorSpinner.setAdapter(operatorArrayAdapter);

		String [] values =
				{"Number of Operator","1","2","3","4","5","6","7"};

		ArrayAdapter<String> selArrayAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, values);
		selArrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		c4numOperatorSpinner.setAdapter(selArrayAdapter);


		String [] frequency =
				{"Frequency of Operator","900","800","1800","2100","2000"};

		ArrayAdapter<String> freArrayAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, frequency);
		freArrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		c4FreqencySpinner.setAdapter(freArrayAdapter);

		String [] technology =
				{"Select Technology","2g","3g","4g"};

		ArrayAdapter<String> techArrayAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, technology);
		techArrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		c4technologySpinner.setAdapter(techArrayAdapter);


		networkConnection = new NetworkConnection(getActivity());

		c4selOpeeratorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int pos,
									   long id) {

				if(pos!=0) {


					Channel4.getInstance().setC4Operator(parent.getItemAtPosition(pos).toString());
//					Toast.makeText(parent.getContext(),
//							"On Item Select : \n" + parent.getItemAtPosition(pos).toString(),
//							Toast.LENGTH_LONG).show();
				}
				else{
					Channel4.getInstance().setC4Operator("No Sensor");
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		c4numOperatorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int pos,
									   long id) {

				if (pos != 0) {

					Channel4.getInstance().setC4Operno(parent.getItemAtPosition(pos).toString());
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

		c4FreqencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int pos,
									   long id) {

				if (pos != 0) {

					Channel4.getInstance().setC4Freq(parent.getItemAtPosition(pos).toString());
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

		c4technologySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int pos,
									   long id) {


				if(pos!=0) {

					Channel4.getInstance().setC4Tech(parent.getItemAtPosition(pos).toString());
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

	private void populateScheduleVenueDetails(String selectedDate){

	}
}
