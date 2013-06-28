package cat.montoya.gbd;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.app.Activity;
import android.app.Fragment;

public class FragmentTablero extends Fragment {

	private static final int SELECT_PICTURE = 1;
	private static final int CAMERA_REQUEST = 1888;
	private Bitmap _photo;
	
	@Override
	 public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  setRetainInstance(true);
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.fragment_tablero, container, false);

		if(_photo != null){
  			ImageView preview = (ImageView) v.findViewById(R.id.ibPreview);
			preview.setImageBitmap(_photo);
  		}
		
		RelativeLayout rlGal = (RelativeLayout) v.findViewById(R.id.rlGaleria);
		rlGal.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
            	addBoardFromGallery(v);
            }
        });
		
		RelativeLayout rlCam = (RelativeLayout) v.findViewById(R.id.rlCamera);
		rlCam.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
            	addBoardFromCamera(v);
            }
        });
		
        return v;
    }
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
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
		final PackageManager packageManager = getView().getContext().getPackageManager();
		boolean cameraAvailable = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA);
		if (cameraAvailable) {
			Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(takePictureIntent, CAMERA_REQUEST);

		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == SELECT_PICTURE) {
				Uri selectedImageUri = data.getData();
				String selectedImagePath = getPath(selectedImageUri);
				_photo = BitmapFactory.decodeFile(selectedImagePath);
			} else if (requestCode == CAMERA_REQUEST) {
				_photo = (Bitmap) data.getExtras().get("data");
			}
			
			ImageView preview = (ImageView) getView().findViewById(R.id.ibPreview);
			preview.setImageBitmap(_photo);
		}
	}

	/*
	 * En principio esto me pilla la imagen del bitmap y la reescalo Por
	 * provar!!!!
	 */
	protected Bitmap GetBitmapFromPreview() {
		ImageView preview = (ImageView) getView().findViewById(R.id.ibPreview);
		BitmapDrawable drawable = (BitmapDrawable) preview.getDrawable();
		Bitmap bitmap = drawable.getBitmap();
		Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 160, 160, true);
		return scaledBitmap;
	}

	protected String getPath(Uri uri) {
		String res = null;
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = getView().getContext().getContentResolver().query(uri, proj, null, null, null);
		if (cursor.moveToFirst()) {
			;
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			res = cursor.getString(column_index);
		}
		cursor.close();
		return res;
	}
}
