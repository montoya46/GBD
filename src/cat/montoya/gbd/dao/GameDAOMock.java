package cat.montoya.gbd.dao;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.*;

import cat.montoya.gbd.entity.Chip;
import cat.montoya.gbd.entity.Chip.ChipType;
import cat.montoya.gbd.entity.Dice;
import cat.montoya.gbd.entity.Dice.DiceType;
import cat.montoya.gbd.entity.Game;

public class GameDAOMock implements IGameDAO {

	private File rootFolder;

	public GameDAOMock(File folder) {
		this.rootFolder = folder;
	}

	@Override
	public Game getGame(Long id) {
		// TODO Auto-generated method stub
		Game game = new Game();
		game.setBoardThumbnailURL("");
		game.setBoardURL("");
		List<Chip> chips = new ArrayList<Chip>();
		Chip chip = new Chip();
		chip.setColor(Color.BLUE);
		chip.setType(ChipType.CIRCLE);
		chips.add(chip);
		game.setChips(chips);
		List<Dice> dices = new ArrayList<Dice>();
		Dice dice = new Dice();
		dice.setType(DiceType.POKER);
		dices.add(dice);
		game.setDices(dices);
		game.setHelp(new String[] { "Manual del Joc Dummy" });
		game.setId(1l);
		game.setName("Dummy Game");

		return game;
	}

	@Override
	public Game setGame(Game game) {

		if (game.getId() == null) {

		}

		try {
			File f = new File(rootFolder, String.valueOf(game.getId()));
			if (f.exists())
				f.delete();

			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return game;
	}

	@Override
	public List<Game> getGameList() {
		// TODO Auto-generated method stub
		return getMockGames();
	}

	/**
	 * Delete this
	 * 
	 * @return
	 */
	private List<Game> getMockGames() {
		List<Game> games = new ArrayList<Game>();
		for (int i = 0; i < 20; i++) {
			games.add(new Game());
		}
		return games;
	}

	@Override
	public void deleteGame(Long id) {
		// TODO Auto-generated method stub
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

}
