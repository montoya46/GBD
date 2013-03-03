package cat.montoya.gbd.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Game implements Serializable {

	private static final long serialVersionUID = 4201325637415361928L;
	
	private Long id;
	private String name = "Game name";
	private String[] help = { "How to play" };
	private String boardURL = "default_board.jpg";
	private String boardThumbnailURL = "default_board_thumbnail.jpg";
	private List<Chip> chips = new ArrayList<Chip>();
	private List<Dice> dices = new ArrayList<Dice>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getHelp() {
		return help;
	}

	public void setHelp(String[] help) {
		this.help = help;
	}

	public String getBoardURL() {
		return boardURL;
	}

	public void setBoardURL(String boardURL) {
		this.boardURL = boardURL;
	}

	public String getBoardThumbnailURL() {
		return boardThumbnailURL;
	}

	public void setBoardThumbnailURL(String boardThumbnailURL) {
		this.boardThumbnailURL = boardThumbnailURL;
	}

	public List<Chip> getChips() {
		return chips;
	}

	public void setChips(List<Chip> chips) {
		this.chips = chips;
	}

	public List<Dice> getDices() {
		return dices;
	}

	public void setDices(List<Dice> dices) {
		this.dices = dices;
	}

}
