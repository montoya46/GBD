package cat.montoya.gbd;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import cat.montoya.gbd.dao.GameDAOMock;
import cat.montoya.gbd.dao.IGameDAO;
import cat.montoya.gbd.entity.Game;

public class GameDetail extends Activity {
	
	private IGameDAO gameDAO;
	private static final int SELECT_PICTURE = 1;
	private static final int CAMERA_REQUEST = 1888; 
	private Uri selectedImageUri;
	private String selectedImagePath;
	
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
		// TODO Implementaci√≥ carrega del joc
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
	
	/*
	 * Metodo para recuperar desde la galeria
	 * */
	public void addBoardFromGallery(View v){
		Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
	}
	
	/*
	 * MÈtodo para recuperar la imagen desde la camara
	 * */
	public void addBoardFromCamera(View v){
		final PackageManager packageManager = this.getPackageManager();
		boolean cameraAvailable = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA);
		if (cameraAvailable){
			Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		    startActivityForResult(takePictureIntent, CAMERA_REQUEST);
			
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
            	selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                ImageView preview = (ImageView)findViewById(R.id.ibPreview);
                preview.setImageURI(selectedImageUri);
            }
            else if(requestCode == CAMERA_REQUEST){
            	Bitmap photo = (Bitmap) data.getExtras().get("data");
            	ImageView preview = (ImageView)findViewById(R.id.ibPreview);
            	preview.setImageBitmap(photo);
            }
        }
    }
	
	/*
	 * En principio esto me pilla la imagen del bitmap y la reescalo
	 * Por provar!!!!
	 * */
	protected void GetBitmapFromPreview(){
		ImageView preview = (ImageView)findViewById(R.id.ibPreview);
		BitmapDrawable drawable = (BitmapDrawable) preview.getDrawable();
		Bitmap bitmap = drawable.getBitmap();
		Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 160, 160, true);
	}
	
	protected String getPath(Uri uri) {
		String res = null;
	    String[] proj = { MediaStore.Images.Media.DATA };
	    Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
	    if(cursor.moveToFirst()){;
	       int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	       res = cursor.getString(column_index);
	    }
	    cursor.close();
	    return res;
    }
}
