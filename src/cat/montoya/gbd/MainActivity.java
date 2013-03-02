package cat.montoya.gbd;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import cat.montoya.gbd.dao.GameDAO;
import cat.montoya.gbd.dao.IGameDAO;

/*
 * Game List Activity
 */
public class MainActivity extends Activity {
	
	private IGameDAO gameDAO;

	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
				
		setContentView(R.layout.activity_main);
		
		Context context = getApplicationContext();
		CharSequence text = "Hello toast!";
		int duration = Toast.LENGTH_SHORT;

		if (checkExternalStorageAvailableP()){
			text = "Access to external storage";
		}else {
			text = "No Access to external storage";
		}
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
		
		File folder = getExternalFilesDir(null);
		if (folder == null)
		folder = getFilesDir();
		
		gameDAO = new GameDAO(folder);
		
		TextView tv = (TextView) findViewById(R.id.textView1);
		tv.setText(folder.getAbsolutePath());

	}

	private boolean checkExternalStorageAvailableP() {
		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			// We can read and write the media
			mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			// We can only read the media
			mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;
		} else {
			// Something else is wrong. It may be one of many other states, but
			// all we need
			// to know is we can neither read nor write
			mExternalStorageAvailable = mExternalStorageWriteable = false;
		}
		return mExternalStorageAvailable && mExternalStorageWriteable;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void editGame(View v){
		Intent i = new Intent(this, MaintenanceActivity.class);
		startActivity(i);
	}
	
	public void playGame(View v){
		Intent i = new Intent(this, GameActivity.class);
		startActivity(i);
	}
	
	
}
