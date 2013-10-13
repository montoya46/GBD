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
import cat.montoya.gbd.entity.Dice;
import cat.montoya.gbd.entity.Game;

public class GameDAO implements IGameDAO {

	private SQLiteDatabase db;
	private GameHelper dbHelper;

	String[] PROJECTIONGAME = { "id", "name", "help", "boardURL" };
	String[] PROJECTIONDICE = { "id", "idgame", "type" };
	String[] PROJECTIONCHIP = { "id", "idgame", "type", "color", "size" };

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
		// TODO moure aixo a una constant
		String[] projectionGame = { "id", "name", "help", "boardURL" };
		String selectionGame = "id = ?";
		String[] selectionGameArgs = { String.valueOf(id) };

		Cursor cGame = db.query("game", projectionGame, selectionGame,
				selectionGameArgs, null, null, null);

		if (cGame != null) {
			cGame.moveToFirst();
			g = cursorToGame(cGame);
			cGame.close();
		}

		if (g != null) {
			List<Dice> dices = new ArrayList<Dice>();

			String[] projectionDice = { "id", "idgame", "type" };
			String selectionDice = "idgame = ?";
			String[] selectionDiceArgs = { String.valueOf(id) };

			Cursor cDices = db.query("dice", projectionDice, selectionDice,
					selectionDiceArgs, null, null, null);

			if (cDices != null) {
				cDices.moveToFirst();
				while (!cDices.isAfterLast()) {
					Dice d = cursorToDice(cDices);
					dices.add(d);
					cDices.moveToNext();
				}
				g.setDices(dices);
				cDices.close();
			}

			List<Chip> chips = new ArrayList<Chip>();

			String[] projectionChip = { "id", "idgame", "type", "color", "size" };
			String selectionChip = "idgame = ?";
			String[] selectionChioArgs = { String.valueOf(id) };

			Cursor cChips = db.query("chip", projectionChip, selectionChip,
					selectionChioArgs, null, null, null);

			if (cChips != null) {
				cChips.moveToFirst();
				while (!cChips.isAfterLast()) {
					Chip c = cursorToChip(cChips);
					chips.add(c);
					cChips.moveToNext();
				}
				g.setChips(chips);
				cChips.close();
			}

		}

		return g;
	}

	@Override
	public Game setGame(Game game) {
		open();
		dbHelper.getWritableDatabase();

		ContentValues gameValues = new ContentValues();
		gameValues.put(PROJECTIONGAME[1], game.getName());
		gameValues.put(PROJECTIONGAME[2], game.getHelp());
		gameValues.put(PROJECTIONGAME[3], game.getBoardURL());

		long gameId;
		if (game.getId() != null) {
			gameId = game.getId();
			gameValues.put(PROJECTIONGAME[1], game.getId());
			db.update("game", gameValues, "id = ?",
					new String[] { String.valueOf(game.getId()) });

		} else {
			gameId = db.insert("game", null, gameValues);
			game.setId(gameId);
		}

		// Eliminem abans de insertar els nous. Sempre podem fer update enlloc
		// de insert en cas que ja tinguin id.
		db.delete("dice", "idgame = ?",
				new String[] { String.valueOf(game.getId()) });
		db.delete("chip", "idgame = ?",
				new String[] { String.valueOf(game.getId()) });

		// Insertamos los dados
		ContentValues diceValues = new ContentValues();
		long newDiceId;

		if (game.getDices() != null)
			for (Dice d : game.getDices()) {
				diceValues.clear();
				diceValues.put(PROJECTIONDICE[1], gameId);
				diceValues.put(PROJECTIONDICE[2], String.valueOf(d.getType()));
				newDiceId = db.insert("dice", null, diceValues);
				d.setId(newDiceId);
			}

		// Insertamos las fichas
		ContentValues chipValues = new ContentValues();
		long newChipId;

		if (game.getChips() != null)
			for (Chip c : game.getChips()) {
				chipValues.clear();
				chipValues.put(PROJECTIONCHIP[1], gameId);
				chipValues.put(PROJECTIONCHIP[2], String.valueOf(c.getType()));
				chipValues.put(PROJECTIONCHIP[3], String.valueOf(c.getColor()));
				chipValues.put(PROJECTIONCHIP[4], String.valueOf(c.getSize()));
				newChipId = db.insert("chip", null, chipValues);
				c.setId(newChipId);
			}
		close();
		return game;
	}

	@Override
	public List<Game> getGameList() {
		open();
		List<Game> games = new ArrayList<Game>();

		String orderBy = PROJECTIONGAME[1] + " DESC";

		Cursor c = db.query("game", PROJECTIONGAME, null, null, null, null,
				orderBy);

		if (c != null) {
			c.moveToFirst();
			while (!c.isAfterLast()) {
				Game game = cursorToGame(c);
				games.add(game);
				c.moveToNext();
			}
		}
		close();
		return games;
	}

	@Override
	public void deleteGame(Long id) {
		db.delete("dice", "idgame = ?", new String[] { String.valueOf(id) });
		db.delete("chip", "idgame = ?", new String[] { String.valueOf(id) });
		db.delete("game", "id = ?", new String[] { String.valueOf(id) });
	}

	@Override
	public void saveGame(Long id, Date d, String description) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Game loadGame(Long id) {
		throw new UnsupportedOperationException();
	}

	/*
	 * Metodo para devolver la entidad a partir de la fila a la que apunta el
	 * cursor.
	 */
	private Game cursorToGame(Cursor cGame) {
		Game g = new Game();
		g.setId(cGame.getLong(cGame.getColumnIndexOrThrow(PROJECTIONGAME[0])));
		g.setName(cGame.getString(cGame
				.getColumnIndexOrThrow(PROJECTIONGAME[1])));
		g.setBoardURL(cGame.getString(cGame
				.getColumnIndexOrThrow(PROJECTIONGAME[2])));
		g.setBoardThumbnailURL(g.getBoardURL() + ".tmb");
		return g;
	}

	private Dice cursorToDice(Cursor cDices) {
		Dice d = new Dice();
		d.setId(cDices.getLong(cDices.getColumnIndexOrThrow(PROJECTIONDICE[0])));
		Long diceType = cDices.getLong(cDices
				.getColumnIndexOrThrow(PROJECTIONDICE[2]));

		if (diceType == null || diceType.longValue() == 0)
			d.setType(Dice.DiceType.STANDARD);
		else
			d.setType(Dice.DiceType.POKER);
		return d;
	}

	private Chip cursorToChip(Cursor cChips) {
		Chip chip = new Chip();
		chip.setId(cChips.getLong(cChips
				.getColumnIndexOrThrow(PROJECTIONDICE[0])));
		chip.setType(cChips.getInt(cChips
				.getColumnIndexOrThrow(PROJECTIONDICE[2])));
		chip.setColor(cChips.getInt(cChips
				.getColumnIndexOrThrow(PROJECTIONDICE[3])));
		chip.setSize(cChips.getInt(cChips
				.getColumnIndexOrThrow(PROJECTIONDICE[4])));
		return chip;
	}
}
