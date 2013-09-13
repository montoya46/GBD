package cat.montoya.gbd.entity;

import android.graphics.Color;

public class Chip {

	private int Id;
	private int type;
	private int color;
	private int size;

	public int getType() {
		return type;
	}

	public void setType(int type) {
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

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
}
