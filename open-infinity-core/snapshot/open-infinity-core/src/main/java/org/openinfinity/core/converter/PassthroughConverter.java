package org.openinfinity.core.converter;

public class PassthroughConverter implements TypeConverter<Object, Object> {

	@Override
	public Object convert(Object object) {
		return object;
	}

}
