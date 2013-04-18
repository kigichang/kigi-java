package tw.kigi.data;

public class Sort {

	private Column column = null;
	private Direction direction = null;
	
	public Sort(Column column, Direction direction) {
		this.column = column;
		this.direction = direction;
	}

	public Column getColumn() {
		return column;
	}

	public Direction getDirection() {
		return direction;
	}
}
