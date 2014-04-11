package ipsv2.activities;

import ipsv2.objects.AccessPoint;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.example.ips.R;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PrintXMLActivity extends Activity {

	private ListView lv;
	private String filepath = "";
	private String level = "";
	private String levelNo = "";

	//ArrayLists
	private ArrayList<AccessPoint> aps = new ArrayList<AccessPoint>();				//stores all loaded access points
	private ArrayList<ScanResult> wlist = new ArrayList<ScanResult>();
	private ArrayList<AccessPoint> top3 = new ArrayList<AccessPoint>();

	//LoadAPs Method
	private BufferedReader br;
	private WifiManager wifi;
	private boolean scan;

	//Location Coordinates
	private String X, Y = "";
	private HashMap<String, Integer> vals = new HashMap<String, Integer>();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_print_xml);
		lv = (ListView) findViewById(R.id.listView1);

		Intent intent = getIntent();
		level = intent.getExtras().getString("level");
		levelNo = level.substring((level.length())-1);
		filepath = intent.getExtras().getString("path");
		//		Log.i("Level", level);
		//		Log.i("LevelNumber", levelNo);
		//		Log.i("Path", filepath);
		scanAPs();
		loadAPs();
		findLocation();

		if (aps.size() == 0){
			Context context = getApplicationContext();
			CharSequence text = "No Access Points detected.";
			Toast toast = Toast.makeText(context, text, 30);
			toast.show();
		}

		Context context = getApplicationContext();
		CharSequence text = "X: "+X+", Y: "+Y;
		Toast toast = Toast.makeText(context, text, 60);
		toast.show();

		ArrayAdapter<AccessPoint> adapter = new ArrayAdapter<AccessPoint>(this, R.layout.mytextview, aps);
		lv.setAdapter(adapter);
	}

	private void findLocation() {
		if (aps.size() == 0){
			X = "Not found";
			Y = "Not found";
		}
		else if (aps.size() == 1){
			oneAP();
		}
		else if (aps.size() == 2){
			twoAPs();
		}
		else if (aps.size() == 3){
			threeAPs();
		}
		else if (aps.size() > 3){

			threeAPs();
		}

	}

	private void threeAPs() {
		// TODO Auto-generated method stub

	}

	/*
	 * http://e-blog-java.blogspot.co.nz/2013/03/how-to-find-point-coordinates-between.html
	 */
	private void twoAPs() {
		double x1, x2, y1, y2, d = 0;
		x1 = Double.parseDouble(aps.get(0).getX());
		y1 = Double.parseDouble(aps.get(0).getY());
		x2 = Double.parseDouble(aps.get(1).getX());
		y2 = Double.parseDouble(aps.get(1).getY());

//		// calculate distance between the two points
//        double DT = Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
//        if (aps.get(0).getDistance() < aps.get(1).getDistance())
//        	d = aps.get(0).getDistance();
//        else if (aps.get(0).getDistance() > aps.get(1).getDistance())
//        	d = aps.get(1).getDistance();
//
//        double T = d / DT;
//        this.X = Double.toString((1-T)*x1 + T * y1);
//        this.Y = Double.toString((1-T)*x2 + T * y2);
		this.X = Double.toString(Math.abs(x1+x2)/2);
		this.Y = Double.toString((y1+y2)/2);
	}

	private void oneAP() {
		// TODO Auto-generated method stub

	}

	/**
	 * Uses Android's WifiManager to scan the surrounding access points in the area
	 * then calls the loadAPs to check that the detected access points are in the CSV
	 * file loaded by the user.
	 */
	private void scanAPs(){
		wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		scan = wifi.startScan();
		wlist = (ArrayList<ScanResult>) wifi.getScanResults();
	}

	/**
	 * Loads all access points in the CSV file loaded from filechooser,
	 * then saves it in an arraylist of AccessPoints with the mac address,
	 * floor level, X and Y coordinates, description, level and frequency
	 * IFF loaded level is the user's chosen level and the access point
	 * was detected at the time.
	 */
	private void loadAPs() {
		try {
			br = new BufferedReader(new FileReader(filepath));

			while(((filepath = br.readLine()) != null)) {
				AccessPoint a = new AccessPoint();
				String[] result = filepath.split(",");
				a.setMac(result[0]);
				a.setLevel(result[1]);
				a.setX(result[2]);
				a.setY(result[3]);
				a.setDecription(result[4]);
				for (ScanResult sr : wlist){
					if (sr.BSSID.equalsIgnoreCase(a.getMac())){
						a.setRssi(sr.level);
						a.setFreq(sr.frequency);
						//Log.i("freq", Integer.toString(sr.frequency));
						//Log.i("dBm", Integer.toString(sr.level));
					}
				}
				if (a.getLevel().equals(levelNo) && a.getRssi() != 0)
					aps.add(a);
			}
			br.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.print_xml, menu);
		return true;
	}

}
