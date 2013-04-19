package tw.kigi.test;

import java.text.ParseException;

import tw.kigi.data.EnumValue;
import tw.kigi.data.Primitive;

public enum ItemDeleted implements EnumValue<Character> {
	ALIVE('N'), DELETED('D');
	
	private Character value;
	private ItemDeleted(Character value) {
		this.value = value;
	}
	@Override
	public Character toValue() {
		return value;
	}
	
	@Override
	public Enum<? extends EnumValue<Character>> toEnum(Object value)
			throws ParseException {
		for (ItemDeleted e : ItemDeleted.values()) {
			if (e.value.equals(value)) {
				return e;
			}
		}
		throw new ParseException(value + " Not in ItemDeleted", 1);
	}
	
	@Override
	public Primitive toPrimitive() {
		return Primitive.CHAR;
	}
	
	
}
