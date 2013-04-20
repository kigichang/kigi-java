package tw.kigi.data;

public enum Direction {
	ASC, DESC;
	
	public static Direction toDirection(String value) {
		if (DESC.name().equalsIgnoreCase(value)) {
			return DESC;
		}
		
		return ASC;
	}
}
