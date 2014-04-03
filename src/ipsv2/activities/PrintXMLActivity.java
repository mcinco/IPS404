package ipsv2.activities;

import ipsv2.objects.AccessPoint;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.example.ips.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class PrintXMLActivity extends Activity {

	TextView textView;
	String filepath = "";
	ArrayList<AccessPoint> aps = new ArrayList<AccessPoint>();
	private BufferedReader br;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		filepath = intent.getStringExtra(FileChooserActivity.EXTRA_MESSAGE);
		Log.i("Test2", filepath);
		setContentView(R.layout.activity_print_xml);
		loadAPs();
	}

	private void loadAPs() {
		try {
			br = new BufferedReader(new FileReader(filepath));
			AccessPoint a = new AccessPoint();

			while((filepath = br.readLine()) != null) {
				String[] result = filepath.split(",");
				a.setMac(result[0]);
				a.setLevel(result[1]);
				a.setX(result[2]);
				a.setY(result[3]);
				a.setDecription(result[4]);
				aps.add(a);
				Log.i("Test3", a.toString());
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
