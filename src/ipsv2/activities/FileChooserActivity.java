package ipsv2.activities;

import ipsv2.filechooser.FileArrayAdapter;
import ipsv2.filechooser.Option;
import ipsv2.filechooser.OptionComparator;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.ips.R;

import android.os.Bundle;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class FileChooserActivity extends ListActivity {

	//FileChooser
	private File currentDir;
	private FileArrayAdapter adapter;
	public final static String EXTRA_MESSAGE = "com.example.studytime";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		currentDir = new File("/sdcard/");
		fill(currentDir);
	}

	private void fill(File f)
	{
		File[]dirs = f.listFiles();
		this.setTitle("Current Directory: "+f.getName());
		List<Option>dir = new ArrayList<Option>();
		List<Option>fls = new ArrayList<Option>();
		try{
			for(File ff: dirs) {
				if(ff.isDirectory())
					dir.add(new Option(ff.getName(),"Folder",ff.getAbsolutePath()));
				else
					fls.add(new Option(ff.getName(),"File Size: "+ff.length(),ff.getAbsolutePath()));
			}
		} catch(Exception e){

		}
		Collections.sort(dir, new OptionComparator());
		Collections.sort(fls, new OptionComparator());
		dir.addAll(fls);
		if(!f.getName().equalsIgnoreCase("sdcard"))
			dir.add(0,new Option("..","Parent Directory",f.getParent()));
		adapter = new FileArrayAdapter(FileChooserActivity.this,R.layout.file_view,dir);
		this.setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Option o = adapter.getItem(position);
		if(o.getData().equalsIgnoreCase("folder")||o.getData().equalsIgnoreCase("parent directory")){
			currentDir = new File(o.getPath());
			fill(currentDir);
		}
		else
			checkFile(o);
	}

	private void checkFile(Option o){
		String substr = o.getPath().substring(o.getPath().length()-3);

		if (!substr.equalsIgnoreCase("CSV")){
			Context context = getApplicationContext();
			CharSequence text = "Must load CSV file only";
			Toast toast = Toast.makeText(context, text, 15);
			toast.show();
		}
		else {
			Intent intent = new Intent(this,PrintXMLActivity.class);
			intent.setType("text/plain");
			intent.putExtra(EXTRA_MESSAGE, o.getPath());
			startActivity(intent);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.send, menu);
		return true;
	}

}
