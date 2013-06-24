package cat.montoya.gbd.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GameHelper extends SQLiteOpenHelper {
	public static final int DATABASE_VERSION = 2;
	public static final String DATABASE_NAME = "GBD.db";
	private static final String INTEGER_TYPE = " INTEGER";
	private static final String TEXT_TYPE = " TEXT";
	private static final String COMMA_SEP = ",";
	private static final String NOT_NULL = "not null";
	private static final String FOREIGN_KEY = "FOREIGN KEY";
	private static final String REFERENCES = "REFERENCES";

	private static final String SQL_CREATE_GAME = "CREATE TABLE "
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
			+ GameContract.Game_Saved._ID + " )" + REFERENCES
			+ GameContract.Game.TABLE_NAME + " (" + GameContract.Game._ID
			+ " )" + " );";

	private static final String SQL_CREATE_GAME_HELP = "CREATE TABLE "
			+ GameContract.Game_Help.TABLE_NAME + " ("
			+ GameContract.Game_Help._ID + " INTEGER PRIMARY KEY,"
			+ GameContract.Game_Help.COLUMN_NAME_HELP + TEXT_TYPE + NOT_NULL
			+ COMMA_SEP + FOREIGN_KEY + " (" + GameContract.Game_Help._ID
			+ " )" + REFERENCES + GameContract.Game.TABLE_NAME + " ("
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
			+ GameContract.Game_Chips.COLUMN_NAME_ID_GAME + " )" + REFERENCES
			+ GameContract.Game.TABLE_NAME + " (" + GameContract.Game._ID
			+ " )" + FOREIGN_KEY + " ("
			+ GameContract.Game_Chips.COLUMN_NAME_ID_CHIP_TYPE + " )"
			+ REFERENCES + GameContract.Master_Chip.TABLE_NAME + " ("
			+ GameContract.Master_Chip._ID + " )" + " );";

	private static final String SQL_CREATE_GAME_DICES = "CREATE TABLE "
			+ GameContract.Game_Dices.TABLE_NAME + " ("
			+ GameContract.Game_Dices._ID
			+ " INTEGER PRIMARY KEY autoincrement,"
			+ GameContract.Game_Dices.COLUMN_NAME_ID_GAME + INTEGER_TYPE
			+ NOT_NULL + COMMA_SEP
			+ GameContract.Game_Dices.COLUMN_NAME_ID_DICE_TYPE + INTEGER_TYPE
			+ NOT_NULL + COMMA_SEP + FOREIGN_KEY + " ("
			+ GameContract.Game_Dices.COLUMN_NAME_ID_GAME + " )" + REFERENCES
			+ GameContract.Game.TABLE_NAME + " (" + GameContract.Game._ID
			+ " )" + FOREIGN_KEY + " ("
			+ GameContract.Game_Dices.COLUMN_NAME_ID_DICE_TYPE + " )"
			+ REFERENCES + GameContract.Master_Dice.TABLE_NAME + " ("
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

	public GameHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// Creamos las tablas en el orden adecuado, ojo: que hay foreign keys
		dropAll(db); // De moment borrem tot al arrancar fins que funcionin les sentencies create
		db.execSQL(SQL_CREATE_GAME);
		db.execSQL(SQL_CREATE_MASTER_CHIP);
		db.execSQL(SQL_CREATE_MASTER_DICE);
//		db.execSQL(SQL_CREATE_GAME_SAVED);
//		db.execSQL(SQL_CREATE_GAME_HELP);
//		db.execSQL(SQL_CREATE_GAME_CHIPS);
//		db.execSQL(SQL_CREATE_GAME_DICES);

		// Insertamos los datos maestros para MASTER_CHIP y MASTER_DICE
		// En las tablas maestras el campo estado es 1 si esta activo y 0 si
		// esta inactivo (no existe el tipo boolean en sql lite)
		db.execSQL("INSERT INTO " + GameContract.Master_Chip.TABLE_NAME
				+ " VALUES(1, 'CIRCLE', 1);");
		db.execSQL("INSERT INTO " + GameContract.Master_Chip.TABLE_NAME
				+ " VALUES(2, 'RECTANGLE', 1);");
		db.execSQL("INSERT INTO " + GameContract.Master_Dice.TABLE_NAME
				+ " VALUES(1, 'STANDARD', 1);");
		db.execSQL("INSERT INTO " + GameContract.Master_Dice.TABLE_NAME
				+ " VALUES(2, 'POKER', 1);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// This database is only a cache for online data, so its upgrade policy
		// is
		// to simply to discard the data and start over
		db.execSQL(SQL_DELETE_MASTER_CHIP);
		db.execSQL(SQL_DELETE_MASTER_DICE);
		db.execSQL(SQL_DELETE_GAME_HELP);
		db.execSQL(SQL_DELETE_GAME_CHIPS);
		db.execSQL(SQL_DELETE_GAME_DICES);
		db.execSQL(SQL_DELETE_GAME_SAVED);
		db.execSQL(SQL_DELETE_GAME);
		onCreate(db);
	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}

	private void dropAll(SQLiteDatabase db) {
		try {
			db.execSQL(SQL_DELETE_MASTER_CHIP);
			db.execSQL(SQL_DELETE_MASTER_DICE);
			db.execSQL(SQL_DELETE_GAME_HELP);
			db.execSQL(SQL_DELETE_GAME_CHIPS);
			db.execSQL(SQL_DELETE_GAME_DICES);
			db.execSQL(SQL_DELETE_GAME_SAVED);
			db.execSQL(SQL_DELETE_GAME);
		} catch (Exception e) {

		}
	}
}
