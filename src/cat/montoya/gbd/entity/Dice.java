package cat.montoya.gbd.entity;

public class Dice {

	public static enum DiceType {
		STANDARD, POKER, NUMERIC;
	}

	private DiceType type;

	public DiceType getType() {
		return type;
	}

	public void setType(DiceType type) {
		this.type = type;
	}

}
