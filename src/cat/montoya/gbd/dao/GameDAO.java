package cat.montoya.gbd.dao;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.*;

import cat.montoya.gbd.entity.Chip;
import cat.montoya.gbd.entity.Chip.ChipType;
import cat.montoya.gbd.entity.Dice;
import cat.montoya.gbd.entity.Dice.DiceType;
import cat.montoya.gbd.entity.Game;

public class GameDAO implements IGameDAO {

	private SQLiteDatabase database;
	private GameHelper dbHelper;
	
	public GameDAO(Context context) {
	    dbHelper = new GameHelper(context);
	}
	
	public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	@Override
	public Game getGame(Long id) {
		dbHelper.getReadableDatabase();
		
		String[] projection = {
			GameContract.Game._ID,
			GameContract.Game.COLUMN_NAME_NAME,
			GameContract.Game.COLUMN_NAME_BOARD_URL,
			GameContract.Game.COLUMN_NAME_BOARD_THUMBNAIL_URL
		};
		
		String selection = GameContract.Game._ID + " = ?";
		String[] selectionArgs = { id.toString() };
		String orderBy = GameContract.Game.COLUMN_NAME_NAME + " DESC";
		
		Cursor c = database.query(GameContract.Game.TABLE_NAME, projection, selection, selectionArgs, null, null, orderBy);
		
		//TODO: Queda pendiente obtener las fichas y los dados
		
		if(c != null){
			c.moveToFirst();
			Game g = new Game();
			g.setId(c.getLong(c.getColumnIndexOrThrow(GameContract.Game._ID)));
			g.setName(c.getString(c.getColumnIndexOrThrow(GameContract.Game.COLUMN_NAME_NAME)));
			g.setBoardURL(c.getString(c.getColumnIndexOrThrow(GameContract.Game.COLUMN_NAME_BOARD_URL)));
			g.setBoardThumbnailURL(c.getString(c.getColumnIndexOrThrow(GameContract.Game.COLUMN_NAME_BOARD_THUMBNAIL_URL)));
			return g;
		}
			
		return null;
	}

	@Override
	public Game setGame(Game game) {
		return null;
	}

	@Override
	public List<Game> getGameList() {
		dbHelper.getReadableDatabase();
		List<Game> games = new ArrayList<Game>();
		
		String[] projection = {
			GameContract.Game._ID,
			GameContract.Game.COLUMN_NAME_NAME,
			GameContract.Game.COLUMN_NAME_BOARD_URL,
			GameContract.Game.COLUMN_NAME_BOARD_THUMBNAIL_URL
		};
		
		String orderBy = GameContract.Game.COLUMN_NAME_NAME + " DESC";
		
		Cursor c = database.query(GameContract.Game.TABLE_NAME, projection, null, null, null, null, orderBy);
		
		if(c != null){
			c.moveToFirst();
			while (!c.isAfterLast()) {
		      Game game = CursorToGame(c);
		      games.add(game);
		      c.moveToNext();
		    }
		}
		
		return games;
	}

	@Override
	public void deleteGame(Long id) {
		String selection = GameContract.Game._ID + " = ?";
		String[] selectionArgs = { id.toString() };
		database.delete(GameContract.Game.TABLE_NAME, selection, selectionArgs);
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
	 * Metodo para devolver la entidad a partir de la fila
	 * a la que apunta el cursor.
	 * */
	private Game CursorToGame(Cursor c){
		Game g = new Game();
		g.setId(c.getLong(c.getColumnIndexOrThrow(GameContract.Game._ID)));
		g.setName(c.getString(c.getColumnIndexOrThrow(GameContract.Game.COLUMN_NAME_NAME)));
		g.setBoardURL(c.getString(c.getColumnIndexOrThrow(GameContract.Game.COLUMN_NAME_BOARD_URL)));
		g.setBoardThumbnailURL(c.getString(c.getColumnIndexOrThrow(GameContract.Game.COLUMN_NAME_BOARD_THUMBNAIL_URL)));
		return g;
	}
}
