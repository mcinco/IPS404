package ipsv2.activities;

import ipsv2.objects.AccessPoint;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.example.ips.R;

import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class PrintXMLActivity extends ListActivity {

	TextView textView;
	String filepath = "";
	ArrayList<AccessPoint> aps = new ArrayList<AccessPoint>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		filepath = intent.getStringExtra(FileChooserActivity.EXTRA_MESSAGE);
		//Log.i("Test2", filepath);
		setContentView(R.layout.activity_print_xml);

		textView = (TextView) findViewById(R.id.textView3);
		loadAPs();
	}

	private void loadAPs() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(filepath));
			String strLine = null;
			StringTokenizer st = null;
			int lineNumber = 0, tokenNumber = 0;

			while((filepath = br.readLine()) != null) {
				lineNumber++;
				String[] result = filepath.split(",");
				for (int x=0; x<result.length; x++) {
					System.out.println(result[x]);
				}
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
