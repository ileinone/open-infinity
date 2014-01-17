/*
 * Copyright (c) 2011-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openinfinity.core.aspect;

import java.lang.reflect.Field;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.openinfinity.core.annotation.Decrypt;
import org.openinfinity.core.annotation.Encrypt;
import org.openinfinity.core.crypto.CryptoSupport;
import org.openinfinity.core.util.ExceptionUtil;
import org.openinfinity.core.util.IOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
/**
 * Aspect for handling cryptography on method level with entities and their attributes.
 * 
 * @author Ilkka Leinonen
 * @version 1.3.0
 * @since 1.0.0
 */
@Aspect
public class CryptoAspect implements Ordered {
	
	/**
	 * Logger for this class.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CryptoAspect.class);

	/**
	 * Default character encoding is set to 'ISO-8859-1'.
	 */
	private static final String DEFAULT_CHARACTER_ENCODING = "ISO-8859-1";
	
	/**
	 * Default character encoding is set to 'ISO-8859-1'.
	 */
	private String encoding = DEFAULT_CHARACTER_ENCODING;
	
	/**
	 * Support tool for handling the actual cryptography.
	 */
	private CryptoSupport cryptoSupport;
	
	/**
	 * Setter for <code>org.openinfinity.core.crypto.CryptoSupport</code> object.
	 */
	public void setCryptoSupport(CryptoSupport cryptoSupport) {
		this.cryptoSupport = cryptoSupport;
	}
	
	/**
	 * Setter for encoding.
	 * 
	 * @param encoding Represents the actual encoding.
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * Represents the execution order of the aspect.
	 */
	private int order;
	
	/**
	 * Setter for the order.
	 * 
	 * @param order Represents the execution order of the aspect.
	 */
	public void setOrder(int order) {
		this.order = order;
	}
	
	/**
	 * Pointcut for each <code>org.openinfinity.core.annotation.Crypto</code> found from methods. 
	 */
	@Pointcut("@annotation(org.openinfinity.core.annotation.Encrypt)")
	public void encryptObjectContentAfterOperation(){}

	/**
	 * Pointcut for each <code>org.openinfinity.core.annotation.Decrypt</code> found from methods. 
	 */
	@Pointcut("@annotation(org.openinfinity.core.annotation.Decrypt)")
	public void decryptObjectContentBeforeOperation(){}
	
	/**
	 * Method for managing encryption of the entity fields. 
	 * 
	 * @param joinPoint Represent the actual binded joinpoint providing method arguments.
	 * @param encrypt Represents the annotation found from the joinpoint.
	 * @return The original arguments.
	 */
	@Around(value="encryptObjectContentAfterOperation() && @annotation(encrypt)")
	public Object encryptObjectContentAfterMethod(ProceedingJoinPoint joinPoint, final Encrypt encrypt) {
		LOGGER.debug("Encryption for the object has started.");
		try {
			ArgumentBuilder argumentBuilder = new ArgumentBuilder();
			StringBuilder builder = new StringBuilder();
			ArgumentStrategy argumentStrategy = encrypt.argumentStrategy();
			switch (argumentStrategy) {
				case ALL    : executeGatheringOfAllArguments (joinPoint.getArgs(), encrypt, argumentBuilder, builder); LOGGER.debug("Encryption of the all object fields started."); return joinPoint.proceed(joinPoint.getArgs());
				case CUSTOM : executeGatheringOfDefinedArguments(joinPoint.getArgs(), encrypt, argumentBuilder, builder); LOGGER.debug("Encryption of defined object fields started."); return joinPoint.proceed(joinPoint.getArgs());
				case NONE   : return joinPoint.proceed(joinPoint.getArgs());
				default: return joinPoint.proceed(joinPoint.getArgs());
			}
		} catch(Throwable throwable) {
			ExceptionUtil.throwSystemException(throwable.getMessage(), throwable);
		} finally {
			LOGGER.debug("Encryption object fields ended.");
		}
		return joinPoint.getArgs();
	}
	
	/**
	 * Method for managing decryption of the entity fields. 
	 * 
	 * @param joinPoint Represent the actual binded joinpoint providing method arguments.
	 * @param encrypt Represents the annotation found from the joinpoint.
	 * @return The original arguments.
	 */
	@Around(value="decryptObjectContentBeforeOperation() && @annotation(decrypt)")
	public Object decryptObjectContentAfterMethod(ProceedingJoinPoint joinPoint, final Decrypt decrypt) {
		LOGGER.debug("Decryption for the object has started.");
		try {
			ArgumentBuilder argumentBuilder = new ArgumentBuilder();
			StringBuilder builder = new StringBuilder();
			ArgumentStrategy argumentStrategy = decrypt.argumentStrategy();
			switch(argumentStrategy) {
				case ALL    : executeGatheringOfAllArguments (joinPoint.getArgs(), decrypt, argumentBuilder, builder); return joinPoint.proceed(joinPoint.getArgs());
				case CUSTOM : executeGatheringOfDefinedArguments(joinPoint.getArgs(), decrypt, argumentBuilder, builder); return joinPoint.proceed(joinPoint.getArgs());
				case NONE   : return joinPoint.proceed(joinPoint.getArgs());
				default: return joinPoint.proceed(joinPoint.getArgs());
			}
		} catch(Throwable throwable) {
			ExceptionUtil.throwSystemException(throwable.getMessage(), throwable);
		} finally {
			LOGGER.debug("Encryption object fields ended.");
		}
		return joinPoint.getArgs();
	}
	
	private void executeGatheringOfDefinedArguments(Object[] arguments, final Decrypt decrypt, ArgumentBuilder argumentBuilder, StringBuilder builder) {
		argumentBuilder.executeArgumentGatheringCallbackBasedOnDefinedFields(new ArgumentGatheringFieldCallback<Field, Object>() {
			@Override
			public void onField(Field field, Object object) {
				decryptField(field, object);

			}
		}, arguments, decrypt.value());
	}

	private void executeGatheringOfAllArguments(Object[] arguments, final Decrypt decrypt, ArgumentBuilder argumentBuilder, StringBuilder builder) {
		argumentBuilder.executeArgumentGatheringCallbackOnAllFields(new ArgumentGatheringFieldCallback<Field, Object>() {
			@Override
			public void onField(Field field, final Object object) {
				decryptField(field, object);
			}
		}, arguments);
	}

	private void executeGatheringOfAllArguments(Object[] arguments, final Encrypt encrypt, ArgumentBuilder argumentBuilder, StringBuilder builder) {
		argumentBuilder.executeArgumentGatheringCallbackOnAllFields(new ArgumentGatheringFieldCallback<Field, Object>() {
			@Override
			public void onField(Field field, Object object) {
				encryptField(field, object);
			}
		}, arguments);
	}
	
	private void executeGatheringOfDefinedArguments(Object[] arguments, final Encrypt encrypt, ArgumentBuilder argumentBuilder, StringBuilder builder) {
		argumentBuilder.executeArgumentGatheringCallbackBasedOnDefinedFields(new ArgumentGatheringFieldCallback<Field, Object>() {
			@Override
			public void onField(Field field, Object object) {
				encryptField(field, object);
			}
		}, arguments, encrypt.value());
	}
	
	private void encryptField(Field field, final Object object) {
		byte[] plainBytes;
		try {
			plainBytes = IOUtil.getBytes(field.get(object));
			LOGGER.debug("Encrypting field [" + field.getName() + "] with value [" + new String(plainBytes, encoding) + "]");
			String encryptedBase64Presentation = cryptoSupport.encryptAndReturnBase64Presentation(plainBytes, encoding);
			if (encryptedBase64Presentation != null) {
				LOGGER.debug("Encypted field [" + field.getName() + "] with value [" + encryptedBase64Presentation + "]");
				injectField(field, object, encryptedBase64Presentation);
			}
		} catch (Throwable throwable) {
			ExceptionUtil.throwSystemException(throwable.getMessage(), throwable);
		}
	}
	
	private void decryptField(Field field, final Object object) {
		byte[] encryptedBytes;
		try {
			encryptedBytes = IOUtil.getBytes(field.get(object));
			LOGGER.debug("Decrypting field [" + field.getName() + "] with value [" + new String(encryptedBytes, encoding) + "]");
			String decryptedBase64Presentation = cryptoSupport.decryptAndReturnBase64Presentation(encryptedBytes, encoding);
			if (decryptedBase64Presentation != null) {
				LOGGER.debug("Decrypted field [" + field.getName() + "] with value [" + decryptedBase64Presentation + "]");
				injectField(field, object, decryptedBase64Presentation);
			}
		} catch (Throwable throwable) {
			ExceptionUtil.throwSystemException(throwable.getMessage(), throwable);
		}
	}
	
	private void injectField(Field field, Object object, String textValue) {
		try {
			if (!field.isAccessible()) {
				field.setAccessible(Boolean.TRUE);
			}
			LOGGER.debug("Setting encrypted field [" + field.getName() + "], bytes [" + textValue + "]" );
			field.set(object, textValue);	
		} catch (Throwable throwable) {
			LOGGER.error("Error occurred while setting bytes to field: " + throwable.getMessage(), throwable);
		}
	}

	@Override
	public int getOrder() {
		return order;
	}
	
}