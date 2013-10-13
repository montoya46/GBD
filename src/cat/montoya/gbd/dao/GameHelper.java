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
		dropAll(db); // De moment borrem tot al arrancar fins que funcionin les
						// sentencies create
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
}
