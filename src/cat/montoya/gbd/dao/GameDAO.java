package cat.montoya.gbd.dao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import cat.montoya.gbd.entity.Chip;
import cat.montoya.gbd.entity.Chip.ChipType;
import cat.montoya.gbd.entity.Dice;
import cat.montoya.gbd.entity.Dice.DiceType;
import cat.montoya.gbd.entity.Game;

public class GameDAO implements IGameDAO {

	private SQLiteDatabase db;
	private GameHelper dbHelper;

	public GameDAO(Context context) {
		dbHelper = new GameHelper(context);
	}

	public void open() throws SQLException {
		db = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	@Override
	public Game getGame(Long id) {
		dbHelper.getReadableDatabase();
		Game g = null;
		List<Dice> dices = new ArrayList<Dice>();
		List<Chip> chips = new ArrayList<Chip>();

		String[] projectionGame = { GameContract.Game._ID, GameContract.Game.COLUMN_NAME_NAME, GameContract.Game.COLUMN_NAME_BOARD_URL,
				GameContract.Game.COLUMN_NAME_BOARD_THUMBNAIL_URL };

		String selectionGame = GameContract.Game._ID + " = ?";
		String[] selectionGameArgs = { String.valueOf(id) };
		// String orderByGame = GameContract.Game.COLUMN_NAME_NAME + " DESC";

		Cursor c = db.query(GameContract.Game.TABLE_NAME, projectionGame, selectionGame, selectionGameArgs, null, null, null);

		if (c != null) {
			c.moveToFirst();
			g = new Game();
			g.setId(c.getLong(c.getColumnIndexOrThrow(GameContract.Game._ID)));
			g.setName(c.getString(c.getColumnIndexOrThrow(GameContract.Game.COLUMN_NAME_NAME)));
			g.setBoardURL(c.getString(c.getColumnIndexOrThrow(GameContract.Game.COLUMN_NAME_BOARD_URL)));
			g.setBoardThumbnailURL(c.getString(c.getColumnIndexOrThrow(GameContract.Game.COLUMN_NAME_BOARD_THUMBNAIL_URL)));
		}

		c.close();

		if (g != null) {

			// obtenemos los dados
			String dicesQuery = "SELECT gd.Id, gd.IdGame, gd.IdDiceType, md.Description  FROM " + GameContract.Game_Dices.TABLE_NAME + " gd INNER JOIN "
					+ GameContract.Master_Dice.TABLE_NAME + " md ON gd.IdDiceType = md.Id WHERE gd.IdGame = ?";

			c = db.rawQuery(dicesQuery, selectionGameArgs);

			if (c != null) {
				c.moveToFirst();
				while (!c.isAfterLast()) {
					dices.add(CursorToDice(c));
					c.moveToNext();
				}
			}

			g.setDices(dices);
			c.close();

			// Obtenemos las fichas
			String chipsQuery = "SELECT gc.Id, gc.IdGame, gc.IdChipType, gc.RGB_Color, mc.Description  FROM " + GameContract.Game_Chips.TABLE_NAME
					+ " gc INNER JOIN " + GameContract.Master_Chip.TABLE_NAME + " mc ON gc.IdChipType = mc.Id WHERE gc.IdGame = ?";

			c = db.rawQuery(chipsQuery, selectionGameArgs);

			if (c != null) {
				c.moveToFirst();
				while (!c.isAfterLast()) {
					chips.add(CursorToChip(c));
					c.moveToNext();
				}
			}

			g.setChips(chips);
			c.close();
		}

		return g;
	}

	@Override
	public Game setGame(Game game) {
		open();
		dbHelper.getWritableDatabase();

		ContentValues gameValues = new ContentValues();
		gameValues.put(GameContract.Game.COLUMN_NAME_NAME, game.getName());
		gameValues.put(GameContract.Game.COLUMN_NAME_BOARD_URL, game.getBoardURL());
		gameValues.put(GameContract.Game.COLUMN_NAME_BOARD_THUMBNAIL_URL, game.getBoardThumbnailURL());

		long newGameId;
		newGameId = db.insert(GameContract.Game.TABLE_NAME, null, gameValues);
		game.setId(newGameId);

		// Insertamos los dados
		ContentValues diceValues = new ContentValues();
		long newDiceId;

		if (game.getDices() != null)
			for (Dice d : game.getDices()) {
				diceValues.clear();
				diceValues.put(GameContract.Game_Dices.COLUMN_NAME_ID_GAME, newGameId);
				diceValues.put(GameContract.Game_Dices.COLUMN_NAME_ID_DICE_TYPE, String.valueOf(d.getType()));

				newDiceId = db.insert(GameContract.Game_Dices.TABLE_NAME, null, diceValues);
				d.setId((int) newDiceId);
			}

		// Insertamos las fichas
		ContentValues chipValues = new ContentValues();
		long newChipId;

		if (game.getChips() != null)
			for (Chip c : game.getChips()) {
				chipValues.clear();
				chipValues.put(GameContract.Game_Chips.COLUMN_NAME_ID_GAME, newGameId);
				chipValues.put(GameContract.Game_Chips.COLUMN_NAME_ID_CHIP_TYPE, String.valueOf(c.getType()));

				newChipId = db.insert(GameContract.Game_Dices.TABLE_NAME, null, chipValues);
				c.setId((int) newChipId);
			}
		close();
		return game;
	}

	@Override
	public List<Game> getGameList() {
		open();
		List<Game> games = new ArrayList<Game>();

		String[] projection = { GameContract.Game._ID, GameContract.Game.COLUMN_NAME_NAME, GameContract.Game.COLUMN_NAME_BOARD_URL,
				GameContract.Game.COLUMN_NAME_BOARD_THUMBNAIL_URL };

		String orderBy = GameContract.Game.COLUMN_NAME_NAME + " DESC";

		Cursor c = db.query(GameContract.Game.TABLE_NAME, projection, null, null, null, null, orderBy);

		if (c != null) {
			c.moveToFirst();
			while (!c.isAfterLast()) {
				Game game = CursorToGame(c);
				games.add(game);
				c.moveToNext();
			}
		}
		close();
		return games;
	}

	@Override
	public void deleteGame(Long id) {
		String selection = GameContract.Game._ID + " = ?";
		String[] selectionArgs = { id.toString() };
		db.delete(GameContract.Game.TABLE_NAME, selection, selectionArgs);
	}

	@Override
	public void saveGame(Long id, Date d, String description) {
		// TODO Auto-generated method stub
	}

	@Override
	public Game loadGame(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * M�todo para devolver la entidad a partir de la fila a la que apunta el
	 * cursor.
	 */
	private Game CursorToGame(Cursor c) {
		Game g = new Game();
		g.setId(c.getLong(c.getColumnIndexOrThrow(GameContract.Game._ID)));
		g.setName(c.getString(c.getColumnIndexOrThrow(GameContract.Game.COLUMN_NAME_NAME)));
		g.setBoardURL(c.getString(c.getColumnIndexOrThrow(GameContract.Game.COLUMN_NAME_BOARD_URL)));
		g.setBoardThumbnailURL(c.getString(c.getColumnIndexOrThrow(GameContract.Game.COLUMN_NAME_BOARD_THUMBNAIL_URL)));
		return g;
	}

	private Dice CursorToDice(Cursor c) {
		Dice d = new Dice();
		d.setId(c.getInt(c.getColumnIndexOrThrow(GameContract.Game_Dices._ID)));
		d.setDescripcion(c.getString(c.getColumnIndexOrThrow(GameContract.Master_Dice.COLUMN_NAME_DESCRIPTION)));
		d.setType(DiceType.values()[c.getInt(c.getColumnIndexOrThrow(GameContract.Game_Dices.COLUMN_NAME_ID_DICE_TYPE))]);
		return d;
	}

	private Chip CursorToChip(Cursor c) {
		Chip chip = new Chip();
		chip.setId(c.getInt(c.getColumnIndexOrThrow(GameContract.Game_Chips._ID)));
		chip.setDescripcion(c.getString(c.getColumnIndexOrThrow(GameContract.Master_Chip.COLUMN_NAME_DESCRIPTION)));
		chip.setColor(c.getInt(c.getColumnIndexOrThrow(GameContract.Game_Chips.COLUMN_NAME_COLOR)));
		chip.setType(ChipType.values()[c.getInt(c.getColumnIndexOrThrow(GameContract.Game_Chips.COLUMN_NAME_ID_CHIP_TYPE))]);
		return chip;
	}
}
