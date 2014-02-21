package cat.montoya.gbd.entity;

public class Dice {
	private Long id;
	private DiceType type;
	
	public enum DiceType {
		STANDARD,
		TETRAAEDRO,
		HEXAEDRO,
		OCTAEDRO
	}
	
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
