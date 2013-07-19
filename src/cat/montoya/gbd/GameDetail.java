package cat.montoya.gbd;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import cat.montoya.gbd.dao.GameDAO;
import cat.montoya.gbd.dao.IGameDAO;
import cat.montoya.gbd.entity.Chip;
import cat.montoya.gbd.entity.Dice;
import cat.montoya.gbd.entity.Game;
import cat.montoya.gbd.utils.MD5Utils;

public class GameDetail extends Activity {

	private IGameDAO gameDAO;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_game_detail);

		gameDAO = new GameDAO(this);

		Long id = getIntent().getLongExtra("id", -1);

		if (id != -1) {// Carregar Joc de la base de dades i emplenar fitxa
			Toast.makeText(this, "Carregar joc amb id: " + id, Toast.LENGTH_LONG).show();
			Game game = gameDAO.getGame(id);
			loadGame(game);
		} else {
			// Nou joc
			Toast.makeText(this, "Nou joc", Toast.LENGTH_LONG).show();
		}

		Spinner spinner = (Spinner) findViewById(R.id.numer_dices);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.number_dices_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);

		Spinner spinnerChips = (Spinner) findViewById(R.id.numer_chips);
		spinnerChips.setAdapter(adapter);

		Spinner spinnerSize = (Spinner) findViewById(R.id.number_size);
		spinnerSize.setAdapter(adapter);
	}

	public void SelectDice(View v) {
		//TODO: Mostrar PopUp con los dado
	}

	private void loadGame(Game g) {

		// ID
		g.getId();
		// NAME
		TextView name = (TextView) findViewById(R.id.edTitulo);
		name.setText(g.getName());
		// HELP
		g.setHelp(new String[] { getStringFromTextView(R.id.edDescripcion) });
		// BOARD && BOARDTHUMBNAIL
//		String file = writeBoardToFile();
//		g.setBoardURL(file);
//		g.setBoardThumbnailURL("tmb_" + file);
//		// Chips
//		g.setChips(viewToEntityChips());
//		// Dices
//		g.setDices(viewToEntityDices());
//		// Save the game
//		g = gameDAO.setGame(g);
	}

	// Metode duplicat
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

	/**
	 * Called onClick save button
	 * 
	 * @param v
	 */
	public void saveGame(View v) {

		Game g = new Game();
		// ID
		g.setId(1l);
		// NAME
		g.setName(getStringFromTextView(R.id.edTitulo));
		// HELP
		g.setHelp(new String[] { getStringFromTextView(R.id.edDescripcion) });
		// BOARD && BOARDTHUMBNAIL
		String[] file = writeBoardToFile();
		g.setBoardURL(file[0]);
		g.setBoardThumbnailURL(file[1]);
		// Chips
		g.setChips(viewToEntityChips());
		// Dices
		g.setDices(viewToEntityDices());
		// Save the game
		g = gameDAO.setGame(g);

		// TODO Load id into field

	}

	private List<Chip> viewToEntityChips() {
		// TODO Auto-generated method stub
		return null;
	}

	private List<Dice> viewToEntityDices() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * This method writes the image to aplication folder 
	 * 
	 * @return string array with the name of the board
	 */
	private String[] writeBoardToFile() {
		// TODO esta posat a saco

		ImageView preview = (ImageView) findViewById(R.id.ibPreview);
		if (preview != null) {
			if (preview.getDrawable() != null && preview.getDrawable() instanceof BitmapDrawable) {
				BitmapDrawable drawable = (BitmapDrawable) preview.getDrawable();
				Bitmap bitmap = drawable.getBitmap();

				if (bitmap != null) {
					// String extStorageDirectory =
					// Environment.getExternalStorageDirectory().toString();
					File rootFolder = getRootFolder();
					OutputStream outStream = null;
					OutputStream outStreamThm = null;
					File fBoard = new File(rootFolder, "board.PNG");
					File fBoardThumbnail = new File(rootFolder, "tmb_board.PNG");
					try {
						outStream = new FileOutputStream(fBoard);
						outStreamThm = new FileOutputStream(fBoardThumbnail);
						bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
						bitmap.compress(Bitmap.CompressFormat.PNG, 25, outStreamThm);
						outStream.flush();
						outStream.close();
						
					} catch (Exception e) {
						Log.e("Error", "Error writing images to disc", e);
					} finally {
						try {
							outStreamThm.flush();
							outStreamThm.close();
						} catch (IOException e) {
						}
					}
					String fBoardName = MD5Utils.md5String(fBoard);
					fBoard.renameTo(new File(rootFolder,fBoardName));
					
					String fBoardNameThumbnail = MD5Utils.md5String(fBoardThumbnail);
					fBoardThumbnail.renameTo(new File(rootFolder,fBoardNameThumbnail));
					return new String[]{fBoardName,fBoardNameThumbnail};
				}
			}
		}

		return null;

	}

	private String getStringFromTextView(int res) {
		String ret = null;
		TextView tvName = (TextView) findViewById(res);
		if (tvName.getText() != null) {
			ret = tvName.getText().toString();
		}
		return ret;
	}

}
