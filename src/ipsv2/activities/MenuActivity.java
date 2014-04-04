package ipsv2.activities;

import com.example.ips.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MenuActivity extends Activity {

	private RadioGroup radioGroup;
	private RadioButton radioButton;
	private Button button;
	private String level="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);

		chooseLevel();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	public void chooseLevel(){
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
		button = (Button) findViewById(R.id.button1);

		button.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				int selectedID = radioGroup.getCheckedRadioButtonId();

				radioButton = (RadioButton) findViewById(selectedID);
				level = radioButton.getText().toString();
				Log.i("radiobutton", level);
				Intent intent = new Intent(getApplicationContext(),FileChooserActivity.class);
				intent.putExtra("levelChosen",  level);
				startActivity(intent);


			}
		});
	}
}
