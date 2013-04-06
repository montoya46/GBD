package cat.montoya.gbd.dao;

import android.provider.BaseColumns;

public class GameContract {
	
	private GameContract(){ }
	
	public static abstract class Game implements BaseColumns {
		public static final String TABLE_NAME = "Game";
		public static final String COLUMN_NAME_NAME = "Name";
		public static final String COLUMN_NAME_BOARD_URL = "BoardUrl";
		public static final String COLUMN_NAME_BOARD_THUMBNAIL_URL = "BoardThumbnailURL";
	}
	
	public static abstract class Game_Saved implements BaseColumns {
		public static final String TABLE_NAME = "Game_Saved";
		public static final String COLUMN_NAME_SAVED_DATE = "Saved_Date";
		public static final String COLUMN_NAME_SHORT_DESCRIPTION = "ShortDescription";
	}
	
	public static abstract class Game_Dices implements BaseColumns {
		public static final String TABLE_NAME = "Game_Dices";
		public static final String COLUMN_NAME_ID_GAME = "IdGame";
		public static final String COLUMN_NAME_ID_DICE_TYPE = "IdDiceType";
	}
	
	public static abstract class Game_Help implements BaseColumns {
		public static final String TABLE_NAME = "Game_Help";
		public static final String COLUMN_NAME_HELP = "Help";
	}
	
	public static abstract class Game_Chips implements BaseColumns {
		public static final String TABLE_NAME = "Game_Chips";
		public static final String COLUMN_NAME_ID_GAME = "IdGame";
		public static final String COLUMN_NAME_ID_CHIP_TYPE = "IdChipType";
		public static final String COLUMN_NAME_COLOR = "Color";
	}
	
	public static abstract class Master_Dice implements BaseColumns {
		public static final String TABLE_NAME = "Master_Dice";
		public static final String COLUMN_NAME_DESCRIPTION = "Description";
		public static final String COLUMN_NAME_STATUS = "Status";
	}
	
	public static abstract class Master_Chip implements BaseColumns {
		public static final String TABLE_NAME = "Master_Chip";
		public static final String COLUMN_NAME_DESCRIPTION = "Description";
		public static final String COLUMN_NAME_STATUS = "Status";
	}
}
