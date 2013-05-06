package cat.montoya.gbd;

import java.io.File;

import cat.montoya.gbd.R.id;
import cat.montoya.gbd.adapters.ImageAdapter;
import cat.montoya.gbd.dao.GameDAOMock;
import cat.montoya.gbd.dao.IGameDAO;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

public class GameGridViewActivity extends Activity {

	private IGameDAO gameDAO;
	private ActionMode mActionMode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gridview);

		File folder = getRootFolder();
		gameDAO = new GameDAOMock(folder);

		GridView gridview = (GridView) findViewById(R.id.gamegridview);
		gridview.setAdapter(new ImageAdapter(this, gameDAO.getGameList(), folder));

		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				Toast.makeText(GameGridViewActivity.this, "" + position, Toast.LENGTH_SHORT).show();
			}
		});

	}

	private File getRootFolder() {
		File folder = getExternalFilesDir(null);
		if (folder == null)
			folder = getFilesDir();
		return folder;
	}

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
			return true;
		case R.id.action_settings:
			// Falta la activity amb les opcions
			return true;
		case R.id.action_changeview:
			startActivity(new Intent(this, MainActivity.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
