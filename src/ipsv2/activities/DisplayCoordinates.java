package ipsv2.activities;

import ipsv2.objects.AccessPoint;

import java.util.ArrayList;

import com.example.ips.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DisplayCoordinates extends Activity {

	private ListView lv;
	private ArrayList<String> coordinates = new ArrayList<String>();
	private String APnum = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_coordinates);
		lv = (ListView) findViewById(R.id.listView1);

		Intent intent = getIntent();
		APnum = intent.getExtras().getString("APnum");
		coordinates = intent.getStringArrayListExtra("coordinates");

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.mytextview, coordinates);
		lv.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_coordinates, menu);
		return true;
	}

}
