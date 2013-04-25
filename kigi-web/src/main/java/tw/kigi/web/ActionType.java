package tw.kigi.web;

public enum ActionType {
	FORWARD,
	INCLUDE,
	REDIRECT;
	
	public String elementString() {
		return name().toLowerCase();
	}
}
