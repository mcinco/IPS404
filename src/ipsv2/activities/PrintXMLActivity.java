package ipsv2.activities;

import ipsv2.objects.AccessPoint;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

import com.example.ips.R;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class PrintXMLActivity extends Activity {

	private ListView lv;
	private String filepath = "";
	private String level = "";
	private String levelNo = "";
	private String APnum = "";

	//ArrayLists
	private ArrayList<AccessPoint> aps = new ArrayList<AccessPoint>();				//stores all loaded access points
	private ArrayList<ScanResult> wlist = new ArrayList<ScanResult>();
	private ArrayList<String> coordinates = new ArrayList<String>();

	//LoadAPs Method
	private BufferedReader br;
	private WifiManager wifi;
	private boolean scan;

	//Location Coordinates
	private String X, Y = "";


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

		if (APnum.equals("0")){
			Context context = getApplicationContext();
			CharSequence text = "No Access Points detected.";
			Toast toast = Toast.makeText(context, text, 30);
			toast.show();
		}

		ArrayAdapter<AccessPoint> adapter = new ArrayAdapter<AccessPoint>(this, R.layout.mytextview, aps);
		lv.setAdapter(adapter);
	}

	private void findLocation() {
		if (aps.size() == 0){
			APnum = Integer.toString(0);
		}
		else if (aps.size() == 1){
			APnum = Integer.toString(1);
			oneAP();
		}
		else if (aps.size() == 2){
			APnum = Integer.toString(2);
			twoAPs();
		}
		else if (aps.size() == 3){
			APnum = Integer.toString(3);
			threeAPs();
		}
		else if (aps.size() > 3){
			APnum = Integer.toString(3);
			Collections.sort(aps);
			ArrayList<AccessPoint> top3 = new ArrayList<AccessPoint>(aps.subList(aps.size() -3, aps.size()));
			aps = top3;
			threeAPs();
		}

	}

	private void threeAPs() {
		//Values from AccessPoint data
		double x1, x2, x3, y1, y2, y3 , r1, r2, r3 = 0;
		DecimalFormat df = new DecimalFormat("#.##");
		x1 = Double.parseDouble(aps.get(0).getX());
		y1 = Double.parseDouble(aps.get(0).getY());
		x2 = Double.parseDouble(aps.get(1).getX());
		y2 = Double.parseDouble(aps.get(1).getY());
		x3 = Double.parseDouble(aps.get(2).getX());
		y3 = Double.parseDouble(aps.get(2).getY());
		r1 = aps.get(0).getDistance();
		r2 = aps.get(1).getDistance();
		r3 = aps.get(2).getDistance();

		// If AP detected is not in current level, give it less weighting.
		if (!aps.get(0).getLevel().equals(levelNo)){ r1 = r1/7.5; }
		if (!aps.get(1).getLevel().equals(levelNo)){ r2 = r2/7.5; }
		if (!aps.get(2).getLevel().equals(levelNo)){ r3 = r3/7.5; }

		boolean oneNtwo = isIntersecting(x1, y1, x2, y2, r1, r2);
		boolean twoNthree = isIntersecting(x2, y2, x3, y3, r2, r3);
		boolean oneNthree = isIntersecting(x1, y1, x3, y3, r1, r3);

		if (oneNtwo && twoNthree && oneNthree){
			double d1 = getDistance(x1, y1, x2, y2);
			double d2 = getDistance(x2, y2, x3, y3);
			double d3 = getDistance(x1, y1, x3, y3);

			//oneNtwo - midpoint of the two possible coordinates
			double dx1 = getMidpointX(x1, x2, r1, r2, d1);
			double dy1 = getMidpointY(y1, y2, r1, r2, d1);
			//twoNthree - midpoint of the two possible coordinates
			double dx2 = getMidpointX(x2, x3, r2, r3, d2);
			double dy2 = getMidpointY(y2, y3, r2, r3, d2);
			//oneNthree - midpoint of the two possible coordinates
			double dx3 = getMidpointX(x1, x3, r1, r3, d3);
			double dy3 = getMidpointY(y1, y3, r1, r3, d3);

			X = df.format((dx1 + dx2 + dx3)/3);
			Y = df.format((dy1 + dy2 + dy3)/3);
			coordinates.add("("+X+", "+Y+")");
		}
		else {
			X = df.format((x1 + x2 + x3)/3);
			Y = df.format((y1 + y2 + y3)/3);
			coordinates.add("("+X+", "+Y+")");
		}

	}

	/**
	 * Calculates the two possible coordinates of intersection and the midpoint between the two.
	 * http://fypandroid.wordpress.com/2011/07/03/how-to-calculate-the-intersection-of-two-circles-java/
	 */
	private void twoAPs() {
		//Values from AccessPoint data
		double x1, x2, y1, y2, r1, r2 = 0;
		DecimalFormat df = new DecimalFormat("#.##");
		x1 = Double.parseDouble(aps.get(0).getX());
		y1 = Double.parseDouble(aps.get(0).getY());
		x2 = Double.parseDouble(aps.get(1).getX());
		y2 = Double.parseDouble(aps.get(1).getY());
		r1 = aps.get(0).getDistance();
		r2 = aps.get(1).getDistance();

		// If AP detected is not in current level, give it less weighting.
		if (!aps.get(0).getLevel().equals(levelNo)){ r1 = r1/5; }
		if (!aps.get(1).getLevel().equals(levelNo)){ r2 = r2/5; }

		boolean intersects = isIntersecting(x1, y1, x2, y2, r1, r2);
		double d = getDistance(x1, y1, x2, y2);

		// If they intersect, take the midpoint between two possible coordinates.
		// If not, take the midpoint between the two access points.

		if (!intersects){
			X = Double.toString(Math.abs(x1+x2)/2);
			Y = Double.toString(Math.abs(y1+y2)/2);
			coordinates.add("("+X+", "+Y+")");
		}
		else if (intersects){

			double d1 = (Math.pow(r1, 2) - Math.pow(r2, 2) + Math.pow(d, 2))/(2*d);
			double h = Math.sqrt(Math.pow(r1, 2) - Math.pow(d1, 2));
			double x3 = x1 + (d1 * (x2 - x1))/d;
			double y3 = y1 + (d1 * (y2 - y1))/d;

			//Get intersecting coordinates
			String dx =  df.format(x3 + (h*(y2-y1))/d);
			String dy = df.format(y3 - (h*(x2-x1))/d);
			String dx2 = df.format(x3 - (h*(y2-y1))/d);
			String dy2 = df.format(y3 + (h*(x2-x1))/d);
			coordinates.add("2 Access Points detected: ");
			coordinates.add("("+dx+", "+dy+")");
			coordinates.add("("+dx2+", "+dy2+")");
			coordinates.add("X, Y: ("+df.format(x3)+", "+df.format(y3)+")");
		}
	}

	/**
	 * Returns six points that are 10m away around the access point.
	 * http://board.flashkit.com/board/showthread.php?773919-trying-to-find-coordinates-for-points-around-a-circle
	 */
	private void oneAP() {

		DecimalFormat df = new DecimalFormat("#.##");
		double angle = Math.PI * 2/6;
		coordinates.add("1 Access Point detected: ");
		coordinates.add("Six possible coordinates");
		for (int i = 0; i < 6; ++i){
			double theta = angle * i;
			double radius = aps.get(0).getDistance();
			if (radius >= 10) radius = 10;
			String X = df.format(Math.cos(theta) * radius + Double.parseDouble(aps.get(0).getX()));
			String Y = df.format(Math.sin(theta) * radius + Double.parseDouble(aps.get(0).getY()));
			coordinates.add("("+X+", "+Y+")");
		}
		coordinates.add(" ");
		coordinates.add("Midpoint: ");
		X = aps.get(0).getX();
		Y = aps.get(0).getY();
		coordinates.add("X, Y: ("+X+", "+Y+")");

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
					}
				}
				if (a.getRssi() != 0)
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

	/**
	 * Check if two "circles" intersect
	 */
	private boolean isIntersecting(double x1, double y1, double x2, double y2, double r1, double r2){
		double a = (r1+r2)*(r1+r2);
		double dx = x1 - x2;
		double dy = y1 - y2;
		if (a > (dx*dx)+(dy*dy))
			return true;
		else
			return false;
	}

	/**
	 * Gets the x coordinate for the midpoint between two possible
	 * coordinates when two circles intersect.
	 */
	private double getMidpointX(double x1, double x2, double r1, double r2, double d){
		double d1 = (Math.pow(r1, 2) - Math.pow(r2, 2) + Math.pow(d, 2))/(2*d);
		return x1 + (d1 * (x2 - x1))/d;
	}

	/**
	 * Gets the y coordinate for the midpoint between two possible
	 * coordinates when two circles intersect.
	 */
	private double getMidpointY(double y1, double y2, double r1, double r2, double d) {
		double d1 = (Math.pow(r1, 2) - Math.pow(r2, 2) + Math.pow(d, 2))/(2*d);
		return y1 + (d1 * (y2 - y1))/d;
	}

	/**
	 * Calculates the distance between two access points.
	 */
	private double getDistance(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow(y2-y1, 2) + Math.pow(x2-x1,  2));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.print_xml, menu);
		return true;
	}

	/**
	 * Called when the user clicks the Send button
	 */
	public void buttonClick(View view) {
		if (APnum.equals("0")){
			Context context = getApplicationContext();
			CharSequence text = "No Access Points detected.";
			Toast toast = Toast.makeText(context, text, 30);
			toast.show();
		}
		else {
			Intent intent = new Intent(this,DisplayCoordinates.class);
			intent.setType("text/plain");
			intent.putExtra("APnum", APnum);
			intent.putStringArrayListExtra("coordinates", coordinates);
			startActivity(intent);
		}
	}

}
