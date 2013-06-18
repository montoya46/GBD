package cat.montoya.gbd;

import cat.montoya.gbd.entity.Game;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

public class MaintenanceActivity extends Activity {
	
	private static final int CAMERA_REQUEST = 1888; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maintenance);
		Game g = (Game) getIntent().getSerializableExtra("game");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void onCaptureCamera(View v){
		// water
		final PackageManager packageManager = this.getPackageManager();
		boolean cameraAvailable = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA);
		if (cameraAvailable){
			Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		    startActivityForResult(takePictureIntent, CAMERA_REQUEST);
			
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {  
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            ImageView imageView = (ImageView) findViewById(R.id.gameBoard);
            imageView.setImageBitmap(photo);
        }  
    } 

}
