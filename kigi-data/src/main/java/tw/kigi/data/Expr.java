package tw.kigi.data;

public enum Expr {
	NO((byte)0x01), GROUP((byte)0x02), OTHER((byte)0x04);
	
	private byte value;
	private Expr(byte value) {
		this.value = value;
	}
	
	public boolean and(byte value) {
		return (this.value & value) == this.value;
	}
	
	public byte byteValue() {
		return value;
	}
}
