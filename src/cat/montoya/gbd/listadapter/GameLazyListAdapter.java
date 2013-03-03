package cat.montoya.gbd.listadapter;

import java.util.List;

import cat.montoya.gbd.R;
import cat.montoya.gbd.entity.Game;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GameLazyListAdapter extends BaseAdapter {

	private List<Game> games;
	private static LayoutInflater inflater = null;

	// public ImageLoader imageLoader;

	public GameLazyListAdapter(Activity activity, List<Game> games) {
		this.games = games;

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
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.game_list_item, null);

		TextView text = (TextView) vi.findViewById(R.id.text);
		ImageView image = (ImageView) vi.findViewById(R.id.image);
		text.setText("item " + position);
		image.setImageResource(R.drawable.ic_launcher);

		Game game = getItem(position);
		text.setText("item " + position + " - " + game.getName());

		return vi;
	}

}
