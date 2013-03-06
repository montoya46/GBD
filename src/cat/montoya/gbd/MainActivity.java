package cat.montoya.gbd;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;
import cat.montoya.gbd.dao.GameDAO;
import cat.montoya.gbd.dao.IGameDAO;
import cat.montoya.gbd.entity.Game;
import cat.montoya.gbd.listadapter.GameLazyListAdapter;

/*
 * Game List Activity
 */
public class MainActivity extends Activity implements OnItemLongClickListener,
		OnItemClickListener {

	private IGameDAO gameDAO;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		gameDAO = new GameDAO(getRootFolder());

		ListView lv = (ListView) findViewById(R.id.gameList);
		lv.setAdapter(new GameLazyListAdapter(this, gameDAO.getGameList()));
		lv.setOnItemLongClickListener(this);
		lv.setOnItemClickListener(this);

	}

	private File getRootFolder() {
		File folder = getExternalFilesDir(null);
		if (folder == null)
			folder = getFilesDir();
		return folder;
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Toast.makeText(getApplicationContext(), "Clickon element" + id,
				Toast.LENGTH_LONG).show();

		Intent i = new Intent(this, GameActivity.class);
		// i.putExtra("game", gameDAO.getGame(id));
		startActivity(i);

	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {

		Toast.makeText(getApplicationContext(), "LongPress element" + id,
				Toast.LENGTH_LONG).show();
		Intent i = new Intent(this, MaintenanceActivity.class);
		// i.putExtra("game", gameDAO.getGame(id));
		startActivity(i);

		return false;
	}

	public void createDummyGames(View v) {

		// gameDAO.setGame(game);

	}

}
