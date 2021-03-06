package cat.montoya.gbd;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.ActionMode;
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
import cat.montoya.gbd.adapters.GameLazyListAdapter;
import cat.montoya.gbd.dao.GameDAO;
import cat.montoya.gbd.dao.IGameDAO;
import cat.montoya.gbd.utils.FileUtils;

/*
 * Game List Activity
 */
public class MainActivity extends Activity implements OnItemLongClickListener, OnItemClickListener {

	private IGameDAO gameDAO;
	private ActionMode mActionMode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_games_listview);

		File folder = FileUtils.getRootFolder(this);
		gameDAO = new GameDAO(this);

		ListView lv = (ListView) findViewById(R.id.gameList);
		lv.setAdapter(new GameLazyListAdapter(this, gameDAO.getGameList(), folder));
		lv.setOnItemLongClickListener(this);
		lv.setOnItemClickListener(this);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Toast.makeText(getApplicationContext(), "Clickon element" + id, Toast.LENGTH_LONG).show();

		Intent i = new Intent(this, GameActivity.class);
		// i.putExtra("game", gameDAO.getGame(id));
		startActivity(i);

	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

		if (mActionMode != null) {
			return false;
		}

		mActionMode = startActionMode(new ActionMode.Callback() {

			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				MenuInflater inflater = mode.getMenuInflater();
				inflater.inflate(R.menu.onlongclickmenu, menu);
				return true;
			}

			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				switch (item.getItemId()) {
				case R.id.action_edit_game:
					openMaintenanceActivity();
					mode.finish();
					return true;
				case R.id.action_play_game:
					mode.finish();
					return true;
				case R.id.action_delete_game:
					mode.finish();
					return true;
				default:
					return false;
				}
			}

			@Override
			public void onDestroyActionMode(ActionMode mode) {
				mActionMode = null;

			}

			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

				return false;
			}
		});

		return true;
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
			// Intent i = new Intent(this, MaintenanceActivity.class);
			Intent i = new Intent(this, GameDetail.class);
			startActivity(i);
			finish();
			return true;
		case R.id.action_settings:
			// Falta la activity amb les opcions
			return true;
		case R.id.action_changeview:
			startActivity(new Intent(this, GameGridViewActivity.class));
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void openMaintenanceActivity() {
		Intent i = new Intent(this, GameDetail.class);
		startActivity(i);
	}

}
