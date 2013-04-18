package tw.kigi.data;

import java.text.ParseException;

public interface EnumValue<T> {

	public T toValue();
	public Enum<? extends EnumValue<T>> toEnum(Object value) throws ParseException;
	public Primitive toPrimitive();
}
