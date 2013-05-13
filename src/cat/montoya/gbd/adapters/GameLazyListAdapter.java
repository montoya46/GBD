package cat.montoya.gbd.adapters;

import java.io.File;
import java.util.List;

import cat.montoya.gbd.R;
import cat.montoya.gbd.entity.Game;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GameLazyListAdapter extends BaseAdapter {

	private List<Game> games;
	private File folder;

	private static LayoutInflater inflater = null;

	// public ImageLoader imageLoader;

	public GameLazyListAdapter(Activity activity, List<Game> games, File folder) {
		this.games = games;
		this.folder = folder;

		if (GameLazyListAdapter.inflater == null) {
			GameLazyListAdapter.inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
	}

	@Override
	public int getCount() {
		return games.size();
	}

	@Override
	public Game getItem(int position) {
		if (position < games.size()) {
			return games.get(position);
		} else {
			return games.get(games.size() - 1);
		}
	}

	@Override
	public long getItemId(int position) {
		Game g = getItem(position);
		if (g != null && g.getId() != null)
			return g.getId();
		else
			return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Game game = getItem(position);

		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.activity_games_listview_item, null);

		TextView text = (TextView) vi.findViewById(R.id.text);
		ImageView image = (ImageView) vi.findViewById(R.id.image);

		File imgFile = new File(folder, game.getBoardThumbnailURL());
		if (imgFile.exists()) {
			Bitmap myBitmap = BitmapFactory.decodeFile(imgFile
					.getAbsolutePath());
			image.setImageBitmap(myBitmap);
		} else {
			image.setImageResource(R.drawable.ic_launcher);
		}

		text.setText("item " + position + " - " + game.getName());

		return vi;
	}

}
