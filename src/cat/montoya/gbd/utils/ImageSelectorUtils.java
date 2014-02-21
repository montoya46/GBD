package cat.montoya.gbd.utils;

import cat.montoya.gbd.R;
import cat.montoya.gbd.entity.Dice;

public class ImageSelectorUtils {
	
	public static int SelectImg(Dice.DiceType dice){
		int id = R.drawable.dice1;
		
		switch (dice) {
			case STANDARD:
				id = R.drawable.dice1;
				break;
			case TETRAAEDRO:
				id = R.drawable.dice3;
				break;
			case HEXAEDRO:
				id = R.drawable.dice4;
				break;
			case OCTAEDRO:
				id = R.drawable.dice2;
				break;
			default:
				id = R.drawable.dice1;
				break;
		}
		
		return id;
	}

}
