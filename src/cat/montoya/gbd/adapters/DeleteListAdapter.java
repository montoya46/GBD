package cat.montoya.gbd.adapters;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import cat.montoya.gbd.R;
import cat.montoya.gbd.entity.Chip;

public class DeleteListAdapter extends BaseAdapter {
	private List<Chip> chips;
	private static LayoutInflater inflater = null;
	private Activity _activity;
	private List<Chip> chipsToRemove;

	public DeleteListAdapter(Activity activity, List<Chip> chips) {
		this._activity = activity;
		this.chips = chips;
		chipsToRemove = new ArrayList<Chip>();
		
		if (DeleteListAdapter.inflater == null) {
			DeleteListAdapter.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
	}

	public int getCount() {
		return chips.size();
	}

	public Chip getItem(int position) {
		if (position < chips.size()) {
			return chips.get(position);
		} else {
			return chips.get(chips.size() - 1);
		}
	}

	public long getItemId(int position) {
		return 0;
	}

	// create a new ImageView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		Chip c = getItem(position);

		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.dialog_delete_list_item, null);

		TextView tvDescripcionItem = (TextView) vi.findViewById(R.id.tvDescripcionItem);
//		ImageButton imgItem = (ImageButton) vi.findViewById(R.id.imgItemDelete);

		tvDescripcionItem.setText(getDescription(c.getColor(), c.getType(), c.getSize()));
//		imgItem.setTag(c);
		
		return vi;
	}
	
	public String getDescription(int color, int type, int size){
		String colorDescription = "";
		String formaDescription = "";
				
		switch (color) {
			case Color.BLACK:
				colorDescription = _activity.getString(R.string.ColorBLACKDescription);
				break;
			case Color.DKGRAY:
				colorDescription = _activity.getString(R.string.ColorDKGRAYDescription);
				break;
			case Color.GRAY:
				colorDescription = _activity.getString(R.string.ColorGRAYDescription);
				break;
			case Color.LTGRAY:
				colorDescription = _activity.getString(R.string.ColorGRAYDescription);
				break;
			case Color.BLUE:
				colorDescription = _activity.getString(R.string.ColorBLUEDescription);
				break;
			case Color.CYAN:
				colorDescription = _activity.getString(R.string.ColorCYANDescription);
				break;
			case Color.GREEN:
				colorDescription = _activity.getString(R.string.ColorGREENDescription);
				break;
			case Color.RED:
				colorDescription = _activity.getString(R.string.ColorREDDescription);
				break;
			case Color.MAGENTA:
				colorDescription = _activity.getString(R.string.ColorMAGENTADescription);
				break;
			case Color.YELLOW:
				colorDescription = _activity.getString(R.string.ColorYELLOWDescription);
				break;
			default:
			
			break;
		}
		
		switch (type) {
		case R.drawable.circle128:
			formaDescription = _activity.getString(R.string.ShapeCircleDescription);
			break;

		default:
			formaDescription = _activity.getString(R.string.ShapeSquareDescription);
			break;
		}
		
		return formaDescription + "-" + colorDescription + "-" + _activity.getString(R.string.SizeDescription) + size;
	}
}