package ipsv2.activities;

import com.example.ips.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class MenuActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	/** Called when the user clicks the Browse button */
	public void fileChooser(View view) {
		Intent intent = new Intent(getApplicationContext(),FileChooserActivity.class);
        startActivity(intent);
	}

}
