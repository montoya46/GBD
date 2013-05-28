package cat.montoya.gbd;

import java.io.File;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import cat.montoya.gbd.dao.GameDAOMock;
import cat.montoya.gbd.dao.IGameDAO;
import cat.montoya.gbd.entity.Game;

public class GameDetail extends Activity {
	
	private IGameDAO gameDAO;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_game_detail);
		gameDAO = new GameDAOMock(getRootFolder());
		
		Long id = getIntent().getLongExtra("id", -1);
		
		if (id != -1){//Carregar Joc de la base de dades i emplenar fitxa
			Toast.makeText(this,"Carregar joc amb id: "+ id, Toast.LENGTH_LONG).show();
			Game game = gameDAO.getGame(id);
			loadGame(game);
		} else {
			//Nou joc
			Toast.makeText(this,"Nou joc", Toast.LENGTH_LONG).show();
		}
			
		
		Spinner spinner = (Spinner) findViewById(R.id.numer_dices);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.number_dices_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		
		Spinner spinnerChips = (Spinner) findViewById(R.id.numer_chips);
		spinnerChips.setAdapter(adapter);
		
		Spinner spinnerSize = (Spinner) findViewById(R.id.number_size);
		spinnerSize.setAdapter(adapter);
	}

	private void loadGame(Game game) {
		// TODO Implementació carrega del joc
	}
	
	//Metode duplicat
	private File getRootFolder() {
		File folder = getExternalFilesDir(null);
		if (folder == null)
			folder = getFilesDir();
		return folder;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_detail, menu);
		return true;
	}

}
