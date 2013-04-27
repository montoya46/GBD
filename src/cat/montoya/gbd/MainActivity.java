package cat.montoya.gbd;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;
import cat.montoya.gbd.dao.GameDAOMock;
import cat.montoya.gbd.dao.IGameDAO;
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

		File folder = getRootFolder();
		gameDAO = new GameDAOMock(folder);

		ListView lv = (ListView) findViewById(R.id.gameList);
		lv.setAdapter(new GameLazyListAdapter(this, gameDAO.getGameList(),
				folder));
		// lv.setOnItemLongClickListener(this);
		 lv.setOnItemClickListener(this);

		registerForContextMenu(lv);

	}

	private File getRootFolder() {
		File folder = getExternalFilesDir(null);
		if (folder == null)
			folder = getFilesDir();
		return folder;
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

		return false;
	}

	public void createDummyGames(View v) {

		// gameDAO.setGame(game);

	}

	// *******************************************************************
	// *********************** Menu Actions ******************************
	// *******************************************************************

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.action_add_game:
			//Intent i = new Intent(this, MaintenanceActivity.class);
			Intent i = new Intent(this, GameDetail.class);
			startActivity(i);
			return true;
		case R.id.action_settings:
			// Falta la activity amb les opcions
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.onlongclickmenu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.action_edit_game:
			return true;
		case R.id.action_play_game:
			return true;
		case R.id.action_delete_game:
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

}
