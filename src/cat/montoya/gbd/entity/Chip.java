package cat.montoya.gbd.entity;

import android.graphics.Color;

public class Chip {

	public static enum ChipType {
		CIRCLE, RECTANGLE;
	}
	
	private int Id;
	private ChipType type = ChipType.CIRCLE;
	private int color = Color.RED;
	private String Descripcion;

	public ChipType getType() {
		return type;
	}

	public void setType(ChipType type) {
		this.type = type;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
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
