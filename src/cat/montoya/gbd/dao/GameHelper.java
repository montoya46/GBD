package cat.montoya.gbd.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GameHelper extends SQLiteOpenHelper {
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "GBD.db";

	public GameHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// Creamos las tablas en el orden adecuado, ojo: que hay foreign keys
		dropAll(db); // De moment borrem tot al arrancar fins que funcionin les sentencies create		
		String tGame = "create table game (id integer primary key autoincrement, name text not null, help text, boardURL text not null);";
		String tChip = "create table chip (id integer primary key autoincrement, idgame integer not null, type integer, color integer, size integer);";
		String tDice = "create table dice (id integer primary key autoincrement, idgame integer not null, type integer);";
		
		db.execSQL(tGame);
		db.execSQL(tChip);
		db.execSQL(tDice);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onCreate(db);
	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}

	private void dropAll(SQLiteDatabase db) {
		try {
			String dropDice = "DROP TABLE IF EXISTS dice;";
			String dropChip = "DROP TABLE IF EXISTS chip;";
			String dropGame = "DROP TABLE IF EXISTS game;";

			db.execSQL(dropDice);
			db.execSQL(dropChip);
			db.execSQL(dropGame);
		} catch (Exception e) {

		}
	}
	
	
	
	/*
	 * private static final String SQL_CREATE_GAME = "CREATE TABLE "
			+ GameContract.Game.TABLE_NAME + " (" + GameContract.Game._ID
			+ " INTEGER PRIMARY KEY autoincrement,"
			+ GameContract.Game.COLUMN_NAME_NAME + TEXT_TYPE + NOT_NULL
			+ COMMA_SEP + GameContract.Game.COLUMN_NAME_BOARD_URL + TEXT_TYPE
			+ NOT_NULL + COMMA_SEP
			+ GameContract.Game.COLUMN_NAME_BOARD_THUMBNAIL_URL + TEXT_TYPE
			+ NOT_NULL + " );";

	private static final String SQL_CREATE_MASTER_CHIP = "CREATE TABLE "
			+ GameContract.Master_Chip.TABLE_NAME + " ("
			+ GameContract.Master_Chip._ID + " INTEGER PRIMARY KEY,"
			+ GameContract.Master_Chip.COLUMN_NAME_DESCRIPTION + TEXT_TYPE
			+ NOT_NULL + COMMA_SEP
			+ GameContract.Master_Chip.COLUMN_NAME_STATUS + INTEGER_TYPE
			+ NOT_NULL + " );";

	private static final String SQL_CREATE_MASTER_DICE = "CREATE TABLE "
			+ GameContract.Master_Dice.TABLE_NAME + " ("
			+ GameContract.Master_Dice._ID + " INTEGER PRIMARY KEY,"
			+ GameContract.Master_Dice.COLUMN_NAME_DESCRIPTION + TEXT_TYPE
			+ NOT_NULL + COMMA_SEP
			+ GameContract.Master_Dice.COLUMN_NAME_STATUS + INTEGER_TYPE
			+ NOT_NULL + " );";

	private static final String SQL_CREATE_GAME_SAVED = "CREATE TABLE "
			+ GameContract.Game_Saved.TABLE_NAME + " ("
			+ GameContract.Game_Saved._ID + " INTEGER PRIMARY KEY,"
			+ GameContract.Game_Saved.COLUMN_NAME_SAVED_DATE + TEXT_TYPE
			+ NOT_NULL + COMMA_SEP
			+ GameContract.Game_Saved.COLUMN_NAME_SHORT_DESCRIPTION + TEXT_TYPE
			+ NOT_NULL + COMMA_SEP + FOREIGN_KEY + " ("
			+ GameContract.Game_Saved._ID + " )" + REFERENCES + " "
			+ GameContract.Game.TABLE_NAME + " (" + GameContract.Game._ID
			+ " )" + " );";

	private static final String SQL_CREATE_GAME_HELP = "CREATE TABLE "
			+ GameContract.Game_Help.TABLE_NAME + " ("
			+ GameContract.Game_Help._ID + " INTEGER PRIMARY KEY,"
			+ GameContract.Game_Help.COLUMN_NAME_HELP + TEXT_TYPE + NOT_NULL
			+ COMMA_SEP + FOREIGN_KEY + " (" + GameContract.Game_Help._ID
			+ " )" + REFERENCES + " " + GameContract.Game.TABLE_NAME + " ("
			+ GameContract.Game._ID + " )" + " );";

	private static final String SQL_CREATE_GAME_CHIPS = "CREATE TABLE "
			+ GameContract.Game_Chips.TABLE_NAME + " ("
			+ GameContract.Game_Chips._ID
			+ " INTEGER PRIMARY KEY autoincrement,"
			+ GameContract.Game_Chips.COLUMN_NAME_ID_GAME + INTEGER_TYPE
			+ NOT_NULL + COMMA_SEP
			+ GameContract.Game_Chips.COLUMN_NAME_ID_CHIP_TYPE + INTEGER_TYPE
			+ NOT_NULL + COMMA_SEP + GameContract.Game_Chips.COLUMN_NAME_COLOR
			+ INTEGER_TYPE + NOT_NULL + COMMA_SEP + FOREIGN_KEY + " ("
			+ GameContract.Game_Chips.COLUMN_NAME_ID_GAME + " )" + REFERENCES + " "
			+ GameContract.Game.TABLE_NAME + " (" + GameContract.Game._ID
			+ " )" + FOREIGN_KEY + " ("
			+ GameContract.Game_Chips.COLUMN_NAME_ID_CHIP_TYPE + " )"
			+ REFERENCES + " " + GameContract.Master_Chip.TABLE_NAME + " ("
			+ GameContract.Master_Chip._ID + " )" + " );";

	private static final String SQL_CREATE_GAME_DICES = "CREATE TABLE "
			+ GameContract.Game_Dices.TABLE_NAME + " ("
			+ GameContract.Game_Dices._ID
			+ " INTEGER PRIMARY KEY autoincrement,"
			+ GameContract.Game_Dices.COLUMN_NAME_ID_GAME + INTEGER_TYPE
			+ NOT_NULL + COMMA_SEP
			+ GameContract.Game_Dices.COLUMN_NAME_ID_DICE_TYPE + INTEGER_TYPE
			+ NOT_NULL + COMMA_SEP + FOREIGN_KEY + " ("
			+ GameContract.Game_Dices.COLUMN_NAME_ID_GAME + " )" + REFERENCES + " "
			+ GameContract.Game.TABLE_NAME + " (" + GameContract.Game._ID
			+ " )" + FOREIGN_KEY + " ("
			+ GameContract.Game_Dices.COLUMN_NAME_ID_DICE_TYPE + " )"
			+ REFERENCES + " " + GameContract.Master_Dice.TABLE_NAME + " ("
			+ GameContract.Master_Dice._ID + " )" + " );";

	private static final String SQL_DELETE_GAME = "DROP TABLE IF EXISTS "
			+ GameContract.Game.TABLE_NAME + " ;";
	private static final String SQL_DELETE_MASTER_CHIP = "DROP TABLE IF EXISTS "
			+ GameContract.Master_Chip.TABLE_NAME + " ;";
	private static final String SQL_DELETE_MASTER_DICE = "DROP TABLE IF EXISTS "
			+ GameContract.Master_Dice.TABLE_NAME + " ;";
	private static final String SQL_DELETE_GAME_SAVED = "DROP TABLE IF EXISTS "
			+ GameContract.Game_Saved.TABLE_NAME + " ;";
	private static final String SQL_DELETE_GAME_HELP = "DROP TABLE IF EXISTS "
			+ GameContract.Game_Help.TABLE_NAME + " ;";
	private static final String SQL_DELETE_GAME_CHIPS = "DROP TABLE IF EXISTS "
			+ GameContract.Game_Chips.TABLE_NAME + " ;";
	private static final String SQL_DELETE_GAME_DICES = "DROP TABLE IF EXISTS "
			+ GameContract.Game_Dices.TABLE_NAME + " ;";
	 * 
	 * */
}
