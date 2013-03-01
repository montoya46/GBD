package cat.montoya.gbd.entity;

import android.graphics.Color;

public class Chip {

	public static enum ChipType {
		CIRCLE, RECT;
	}

	private ChipType type = ChipType.CIRCLE;
	private int size = 0;
	private int color = Color.RED;

	public ChipType getType() {
		return type;
	}

	public void setType(ChipType type) {
		this.type = type;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

}
