package cat.montoya.gbd.dao;

import java.util.List;

import cat.montoya.gbd.entity.Game;

public interface IGameDAO {
	
	Game getGame(Long id);
	Game setGame(Game game);
	List<Game> getGameList();

}
