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
import com.hetro.FieldConnect.DtoController.Channel3;
import com.hetro.FieldConnect.Util.JSONParser;
import com.hetro.FieldConnect.Util.NetworkConnection;

import java.util.ArrayList;
import java.util.HashMap;


public class Channel3Fragment extends Fragment implements OnRefreshListener {

	SwipeRefreshLayout swipeLayout;

	NetworkConnection networkConnection;

	Spinner c3selOpeeratorSpinner,c3numOperatorSpinner,c3FreqencySpinner,c3technologySpinner;

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

	Button c3SubmitButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.channel3, container, false);


	//	backImageview=(ImageView) rootView.findViewById(R.id.daily_back_imageview);
		c3selOpeeratorSpinner=(Spinner) rootView.findViewById(R.id.c3operator_spinner);
		c3numOperatorSpinner=(Spinner) rootView.findViewById(R.id.c3operatorspinner);
		c3FreqencySpinner  = (Spinner) rootView.findViewById(R.id.c3frequencyspinner);
		c3technologySpinner = (Spinner) rootView.findViewById(R.id.c3technologyspinner);

		backImageview =(ImageView) rootView.findViewById(R.id.c3back);

	//	c3SubmitButton = (Button) rootView.findViewById(R.id.c3submit_button);

		String [] operator =
				{"Select Operator","IDEA","Vodafone","Airtel","Aircel","Reliance","Tata Docomo","Uninor","TTML-G","BSNL"};

		ArrayAdapter<String> operatorArrayAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, operator);
		operatorArrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		c3selOpeeratorSpinner.setAdapter(operatorArrayAdapter);

		String [] values =
				{"Number of Operator","1","2","3","4","5","6","7"};

		ArrayAdapter<String> selArrayAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, values);
		selArrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		c3numOperatorSpinner.setAdapter(selArrayAdapter);


		String [] frequency =
				{"Frequency of Operator","900","800","1800","2100","2000"};

		ArrayAdapter<String> freArrayAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, frequency);
		freArrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		c3FreqencySpinner.setAdapter(freArrayAdapter);

		String [] technology =
				{"Select Technology","2g","3g","4g"};

		ArrayAdapter<String> techArrayAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, technology);
		techArrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		c3technologySpinner.setAdapter(techArrayAdapter);


		networkConnection = new NetworkConnection(getActivity());

		c3selOpeeratorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int pos,
									   long id) {

				if(pos!=0) {

					Channel3.getInstance().setC3Operator(parent.getItemAtPosition(pos).toString());



//					Toast.makeText(parent.getContext(),
//							"On Item Select : \n" + parent.getItemAtPosition(pos).toString(),
//							Toast.LENGTH_LONG).show();
				}
				else{
					Channel3.getInstance().setC3Operator("No Sensor");
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		c3numOperatorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int pos,
									   long id) {

				if (pos != 0) {

					Channel3.getInstance().setC3Operno(parent.getItemAtPosition(pos).toString());
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

		c3FreqencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int pos,
									   long id) {

				if (pos != 0) {

					Channel3.getInstance().setC3Freq(parent.getItemAtPosition(pos).toString());
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

		c3technologySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int pos,
									   long id) {


				if(pos!=0) {

					Channel3.getInstance().setC3Tech(parent.getItemAtPosition(pos).toString());
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
