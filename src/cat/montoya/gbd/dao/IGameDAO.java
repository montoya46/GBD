package cat.montoya.gbd.dao;

import java.sql.Date;
import java.util.List;

import cat.montoya.gbd.entity.Game;

public interface IGameDAO {
	
	/*
	 * Obtenemos el listado de juegos disponibles
	 * */
	List<Game> getGameList();
	
	/*
	 * Obtiene el juego por Id
	 * */
	Game getGame(Long id);
	
	/*
	 * Elimina el juego
	 * */
	void deleteGame(Long id);
		
	/*
	 * Guarda el juego
	 * */
	Game setGame(Game game);
		
	/*
	 * Grabamos la partida
	 * TODO: Guardar las coordenadas de donde estan las fichas
	 * */
	void saveGame(Long id, Date d, String description);
	
	/*
	 * Cargamos la partida
	 * TODO: Guardar las coordenadas de donde estan las fichas
	 * */
	Game loadGame(Long id);
}
