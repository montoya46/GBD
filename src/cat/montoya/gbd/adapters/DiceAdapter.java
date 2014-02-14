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

public class DiceAdapter extends BaseAdapter {
	private List<Integer> dices;
	private static LayoutInflater inflater = null;

	public DiceAdapter(Activity activity, List<Integer> dices) {
		this.dices = dices;

		if (DiceAdapter.inflater == null) {
			DiceAdapter.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

	}

	public int getCount() {
		return dices.size();
	}

	public Integer getItem(int position) {
		if (position < dices.size()) {
			return dices.get(position);
		} else {
			return dices.get(dices.size() - 1);
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
			vi = inflater.inflate(R.layout.dialog_dice_item, null);

		ImageView ivShape = (ImageView) vi.findViewById(R.id.ivDialogDiceItem);
		
		ivShape.setImageResource(shape);
				
		return vi;
	}
}