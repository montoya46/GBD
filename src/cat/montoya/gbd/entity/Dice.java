package cat.montoya.gbd.entity;

public class Dice {

	public static enum DiceType {
		STANDARD, 
		POKER;
	}

	private int Id;
	private String Descripcion;
	private DiceType type;
	
	public DiceType getType() {
		return type;
	}

	public void setType(DiceType type) {
		this.type = type;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getDescripcion() {
		return Descripcion;
	}

	public void setDescripcion(String descripcion) {
		Descripcion = descripcion;
	}

}
