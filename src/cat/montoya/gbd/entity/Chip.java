package cat.montoya.gbd.entity;


public class Chip {

	private Long id;
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

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
