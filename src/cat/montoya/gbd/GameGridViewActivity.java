package cat.montoya.gbd;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import cat.montoya.gbd.adapters.ImageAdapter;
import cat.montoya.gbd.dao.GameDAO;
import cat.montoya.gbd.dao.IGameDAO;
import cat.montoya.gbd.entity.Game;
import cat.montoya.gbd.utils.FileUtils;

public class GameGridViewActivity extends Activity implements OnItemLongClickListener, OnItemClickListener {

	private IGameDAO gameDAO;
	private ActionMode mActionMode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_games_gridview);

		File folder = FileUtils.getRootFolder(this);
		gameDAO = new GameDAO(this);

		GridView gridview = (GridView) findViewById(R.id.gamegridview);
		gridview.setAdapter(new ImageAdapter(this, gameDAO.getGameList(), folder));

		gridview.setOnItemLongClickListener(this);
		gridview.setOnItemClickListener(this);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {
		openGameActivity(id);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, final long id) {
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
					openMaintenanceActivity(id);
//					mode.finish();
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

	public void openMaintenanceActivity(Long id) {
		Intent i = new Intent(this, GameDetail.class);
		if (id != null)
			i.putExtra("id", id);
		
		startActivity(i);
	}
	
	public void openGameActivity(Long id) {
		Intent i = new Intent(this, GameActivity.class);
		Game game = gameDAO.getGame(id);
		if (game != null)
			i.putExtra("game", game);
		
		startActivity(i);
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
			startActivity(new Intent(this, MainActivity.class));
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
