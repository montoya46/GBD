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
import cat.montoya.gbd.entity.Dice;
import cat.montoya.gbd.utils.ImageSelectorUtils;

public class DiceAdapter extends BaseAdapter {
	private List<Dice.DiceType> dices;
	private static LayoutInflater inflater = null;

	public DiceAdapter(Activity activity, List<Dice.DiceType> dices) {
		this.dices = dices;
		if (DiceAdapter.inflater == null) {
			DiceAdapter.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
	}

	public int getCount() {
		return dices.size();
	}

	public Dice.DiceType getItem(int position) {
		if (position < dices.size()) {
			return dices.get(position);
		} else {
			return dices.get(dices.size() - 1);
		}
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Dice.DiceType dice = getItem(position);
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.dialog_dice_item, null);
		ImageView ibDice = (ImageView) vi.findViewById(R.id.ivDialogDiceItem);
		ibDice.setImageResource(ImageSelectorUtils.SelectImg(dice));
		return vi;
	}
}