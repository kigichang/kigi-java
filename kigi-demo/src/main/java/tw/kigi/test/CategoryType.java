package tw.kigi.test;

import java.text.ParseException;

import tw.kigi.data.EnumValue;
import tw.kigi.data.Primitive;

public enum CategoryType implements EnumValue<Short>{
	IN((short)1), OUT((short)-1);
	
	private Short value;
	private CategoryType(short value) {
		this.value = value;
	}
	
	@Override
	public Short toValue() {
		return value;
	}
	
	@Override
	public Enum<? extends EnumValue<Short>> toEnum(Object value)
			throws ParseException {
		for (CategoryType e : CategoryType.values()) {
			if (e.value.equals(value)) {
				return e;
			}
		}
		throw new ParseException(value + " Not in CategoryType", 1);
	}
	
	@Override
	public Primitive toPrimitive() {
		return Primitive.SHORT;
	}
	
	
}
