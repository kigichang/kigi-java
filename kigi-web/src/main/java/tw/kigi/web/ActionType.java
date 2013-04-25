package tw.kigi.web;

public enum ActionType {
	FORWARD,
	INCLUDE,
	REDIRECT,
	ACTION;
	
	public String elementString() {
		return name().toLowerCase();
	}
}
