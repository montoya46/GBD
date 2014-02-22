package cat.montoya.gbd.adapters;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cat.montoya.gbd.R;
import cat.montoya.gbd.entity.Chip;
import cat.montoya.gbd.entity.Dice;
import cat.montoya.gbd.entity.Dice.DiceType;

public class DeleteListAdapterDices extends BaseAdapter {
	private List<Dice> dices;
	private static LayoutInflater inflater = null;
	private Activity _activity;
	
	public DeleteListAdapterDices(Activity activity, List<Dice> dices) {
		this._activity = activity;
		this.dices = dices;
		new ArrayList<Chip>();
		
		if (DeleteListAdapterDices.inflater == null) {
			DeleteListAdapterDices.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
	}

	public int getCount() {
		return dices.size();
	}

	public Dice getItem(int position) {
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
		Dice d = getItem(position);
		
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.dialog_delete_list_item, null);

		TextView tvDescripcionItem = (TextView) vi.findViewById(R.id.tvDescripcionItem);
		tvDescripcionItem.setText(getDescription(d.getType()));
		
		return vi;
	}
	
	public String getDescription(DiceType type){
		String typeDescription = "";
		String formaDescription = _activity.getString(R.string.DiceDescription);
				
		switch (type) {
			case STANDARD:
				typeDescription = _activity.getString(R.string.DiceStandardDescription);
				break;
			case TETRAAEDRO:
				typeDescription = _activity.getString(R.string.DiceTetraedroDescription);
				break;
			case HEXAEDRO:
				typeDescription = _activity.getString(R.string.DiceHexaedroDescription);
				break;
			case OCTAEDRO:
				typeDescription = _activity.getString(R.string.DiceOctaedroDescription);
				break;
			default:
				typeDescription = _activity.getString(R.string.DiceStandardDescription);
			break;
		}
		
		return formaDescription + " " + typeDescription;
	}
}