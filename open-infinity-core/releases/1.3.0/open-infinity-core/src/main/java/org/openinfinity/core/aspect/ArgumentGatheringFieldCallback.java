package org.openinfinity.core.aspect;

/**
 * Field callback interface.
 * 
 * @author Ilkka Leinonen
 *
 * @param <Field>
 * @param <V>
 * 
 * @version 1.0.0
 * @since 1.3.0
 */
public interface ArgumentGatheringFieldCallback<Field, V> {
	
	/**
	 * Called on each found field of an entity.
	 * 
	 * @param field Represent the actual field of an object.
	 * @param object Represents the actual object where the field is found.
	 */
	public void onField(Field field, V object);

}
