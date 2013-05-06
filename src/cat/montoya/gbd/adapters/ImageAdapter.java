package cat.montoya.gbd.adapters;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cat.montoya.gbd.R;
import cat.montoya.gbd.entity.Game;

public class ImageAdapter extends BaseAdapter {
	private Context mContext;

	private List<Game> games;
	private File folder;
	private static LayoutInflater inflater = null;

	public ImageAdapter(Activity activity, List<Game> games, File folder) {
		this.games = games;
		this.folder = folder;

		if (ImageAdapter.inflater == null) {
			ImageAdapter.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

	}

	public int getCount() {
		return games.size();
	}

	public Game getItem(int position) {
		if (position < games.size()) {
			return games.get(position);
		} else {
			return games.get(games.size() - 1);
		}
	}

	public long getItemId(int position) {
		Game g = getItem(position);
		if (g != null && g.getId() != null)
			return g.getId();
		else
			return position;
	}

	// create a new ImageView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		Game game = getItem(position);

		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.game_grid_item, null);

		TextView text = (TextView) vi.findViewById(R.id.editText1);
		ImageView image = (ImageView) vi.findViewById(R.id.imageView1);

		File imgFile = new File(folder, game.getBoardThumbnailURL());
		if (imgFile.exists()) {
			Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
			image.setImageBitmap(myBitmap);
		} else {
			image.setImageResource(R.drawable.parchis);
		}

		text.setText("item " + position + " - " + game.getName());

		return vi;
	}
}