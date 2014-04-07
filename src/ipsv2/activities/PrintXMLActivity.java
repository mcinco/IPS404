package ipsv2.activities;

import ipsv2.objects.AccessPoint;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

public class PrintXMLActivity extends Activity {

	private ListView lv;
	TextView textView;
	String filepath = "";
	String level = "";
	String levelNo = "";
	private ArrayList<AccessPoint> aps = new ArrayList<AccessPoint>();
	private BufferedReader br;
	private WifiManager wifi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_print_xml);
		lv = (ListView) findViewById(R.id.listView1);

		Intent intent = getIntent();
		level = intent.getExtras().getString("level");
		levelNo = level.substring((level.length())-1);
		filepath = intent.getExtras().getString("path");
		Log.i("Level", level);
		Log.i("LevelNumber", levelNo);
		Log.i("Path", filepath);


		loadAPs();

//		for (AccessPoint a: aps){
//			Log.i("t2_final", a.toString());
//		}
		ArrayAdapter<AccessPoint> adapter = new ArrayAdapter<AccessPoint>(this, android.R.layout.simple_list_item_1, aps);
		lv.setAdapter(adapter);
	}

	private void loadAPs() {
		try {
			br = new BufferedReader(new FileReader(filepath));
			wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
			List<ScanResult> wlist = wifi.getScanResults();
//			for (ScanResult r:wlist)
//				Log.i("SSID", r.toString());

			while(((filepath = br.readLine()) != null)) {
				AccessPoint a = new AccessPoint();
				String[] result = filepath.split(",");
				a.setMac(result[0]);
				a.setLevel(result[1]);
				a.setX(result[2]);
				a.setY(result[3]);
				a.setDecription(result[4]);
				for (ScanResult sr : wlist){
					if (sr.BSSID.equalsIgnoreCase(a.getMac()))
						a.setRssi(sr.level);
				}
				if (a.getLevel().equals(levelNo)){
					aps.add(a);
				}

			}
			br.close();
			for (AccessPoint b: aps){
				Log.i("t2_final", b.toString());
			}
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
