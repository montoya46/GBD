package cat.montoya.gbd;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import cat.montoya.gbd.dao.GameDAOMock;
import cat.montoya.gbd.dao.IGameDAO;
import cat.montoya.gbd.entity.Chip;
import cat.montoya.gbd.entity.Dice;
import cat.montoya.gbd.entity.Game;

public class GameDetail extends Activity {

	private IGameDAO gameDAO;
	private static final int SELECT_PICTURE = 1;
	private static final int CAMERA_REQUEST = 1888;
	private Bitmap _photo;
	
	//private Uri selectedImageUri;
	//private String selectedImagePath;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_game_detail);
		
		gameDAO = new GameDAOMock(getRootFolder());

		Long id = getIntent().getLongExtra("id", -1);

		if (id != -1) {// Carregar Joc de la base de dades i emplenar fitxa
			Toast.makeText(this, "Carregar joc amb id: " + id, Toast.LENGTH_LONG).show();
			Game game = gameDAO.getGame(id);
			loadGame(game);
		} else {
			// Nou joc
			Toast.makeText(this, "Nou joc", Toast.LENGTH_LONG).show();
		}

		//TODO: Las soluciones que la gente es que hay que utilizar fragments (revisarlo)
		final Object data = getLastNonConfigurationInstance();
		
		if(data != null){
			ImageView preview = (ImageView) findViewById(R.id.ibPreview);
			preview.setImageBitmap((Bitmap)data);	
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

	@Override
	public Object onRetainNonConfigurationInstance() {
	   return _photo;
	}
	
	private void loadGame(Game game) {
		// TODO Implementació carrega del joc
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

	/*
	 * Metodo para recuperar desde la galeria
	 */
	public void addBoardFromGallery(View v) {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
	}

	/*
	 * Metodo para recuperar la imagen desde la camara
	 */
	public void addBoardFromCamera(View v) {
		final PackageManager packageManager = this.getPackageManager();
		boolean cameraAvailable = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA);
		if (cameraAvailable) {
			Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(takePictureIntent, CAMERA_REQUEST);

		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == SELECT_PICTURE) {
				Uri selectedImageUri = data.getData();
				String selectedImagePath = getPath(selectedImageUri);
				_photo = BitmapFactory.decodeFile(selectedImagePath);
			} else if (requestCode == CAMERA_REQUEST) {
				_photo = (Bitmap) data.getExtras().get("data");
			}
			
			ImageView preview = (ImageView) findViewById(R.id.ibPreview);
			preview.setImageBitmap(_photo);
		}
	}

	/*
	 * En principio esto me pilla la imagen del bitmap y la reescalo Por
	 * provar!!!!
	 */
	protected void GetBitmapFromPreview() {
		ImageView preview = (ImageView) findViewById(R.id.ibPreview);
		BitmapDrawable drawable = (BitmapDrawable) preview.getDrawable();
		Bitmap bitmap = drawable.getBitmap();
		Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 160, 160, true);
	}

	protected String getPath(Uri uri) {
		String res = null;
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
		if (cursor.moveToFirst()) {
			;
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			res = cursor.getString(column_index);
		}
		cursor.close();
		return res;
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
		String file = writeBoardToFile();
		g.setBoardURL(file);
		g.setBoardThumbnailURL("tmb_"+file);
		//Chips
		g.setChips(viewToEntityChips());
		// Dices
		g.setDices(viewToEntityDices());
		//Save the game
		g = gameDAO.setGame(g);
		
		//TODO Load id into field

	}
	
	private List<Chip> viewToEntityChips(){
		// TODO Auto-generated method stub
		return null;
	}
	
	private List<Dice> viewToEntityDices() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * This method writes the image to aplication folder (not yet implemented)
	 * @return
	 */
	private String writeBoardToFile() {
		//TODO esta posat a saco

		ImageView preview = (ImageView) findViewById(R.id.ibPreview);
		BitmapDrawable drawable = (BitmapDrawable) preview.getDrawable();
		Bitmap bitmap = drawable.getBitmap();

//		String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
		File rootFolder = getRootFolder();
		OutputStream outStream = null;
		OutputStream outStreamThm = null;
		File fBoard = new File(rootFolder, "board1.PNG");
		File fBoardThumbnail = new File(rootFolder, "tmb_board1.PNG");
		try {
			outStream = new FileOutputStream(fBoard);
			outStreamThm = new FileOutputStream(fBoardThumbnail);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
			bitmap.compress(Bitmap.CompressFormat.PNG, 25, outStreamThm);
			outStream.flush();
			outStream.close();
			outStreamThm.flush();
			outStreamThm.close();
		} catch (Exception e) {
			Log.e("Error", "Error writing images to disc",e);
		}
		
		
		return fBoard.getName();

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
