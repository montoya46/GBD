package cat.montoya.gbd.adapters;

import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import cat.montoya.gbd.R;

public class ShapeAdapter extends BaseAdapter {
	private List<Integer> shapes;
	private static LayoutInflater inflater = null;

	public ShapeAdapter(Activity activity, List<Integer> shapes) {
		this.shapes = shapes;

		if (ShapeAdapter.inflater == null) {
			ShapeAdapter.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

	}

	public int getCount() {
		return shapes.size();
	}

	public Integer getItem(int position) {
		if (position < shapes.size()) {
			return shapes.get(position);
		} else {
			return shapes.get(shapes.size() - 1);
		}
	}

	public long getItemId(int position) {
		return 0;
	}

	// create a new ImageView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		int shape = getItem(position);

		View vi = convertView;
		
		if (convertView == null)
			vi = inflater.inflate(R.layout.dialog_shape_item, null);

		ImageView ivShape = (ImageView) vi.findViewById(R.id.rlDialogShapeItem);
		
		ivShape.setImageResource(shape);
				
		return vi;
	}
}