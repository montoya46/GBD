package cat.montoya.gbd.entity;

public class Dice {

	public static enum DiceType {
		STANDARD, 
		POKER;
	}

	private Long id;
	private DiceType type;
	
	public DiceType getType() {
		return type;
	}

	public void setType(DiceType type) {
		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
