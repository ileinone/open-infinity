package org.openinfinity.core.converter;

public interface TypeConverter<K,V> {
	
	public V convert(K object);

}
