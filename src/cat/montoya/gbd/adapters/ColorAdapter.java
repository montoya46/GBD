package cat.montoya.gbd.adapters;

import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import cat.montoya.gbd.R;

public class ColorAdapter extends BaseAdapter {
	private List<Integer> colors;
	private static LayoutInflater inflater = null;

	public ColorAdapter(Activity activity, List<Integer> colors) {
		this.colors = colors;

		if (ColorAdapter.inflater == null) {
			ColorAdapter.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
	}

	public int getCount() {
		return colors.size();
	}

	public Integer getItem(int position) {
		if (position < colors.size()) {
			return colors.get(position);
		} else {
			return colors.get(colors.size() - 1);
		}
	}

	public long getItemId(int position) {
		return 0;
	}

	// create a new ImageView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		int color = getItem(position);

		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.dialog_color_item, null);

		RelativeLayout rlColor = (RelativeLayout) vi.findViewById(R.id.rlDialogColorItem);
		
		rlColor.setBackgroundColor(color);
				
		return vi;
	}
}