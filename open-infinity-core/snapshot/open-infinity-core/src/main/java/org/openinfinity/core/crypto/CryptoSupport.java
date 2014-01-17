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
package org.openinfinity.core.crypto;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;

import org.apache.commons.codec.binary.Base64;
import org.keyczar.Crypter;
import org.keyczar.exceptions.KeyczarException;
import org.openinfinity.core.util.ExceptionUtil;
import org.openinfinity.core.util.IOUtil;

/**
 * Object for supporting encryption and decryption of the entity fields. Handles Base64 encoding with <code>java.util.String</code> fields.
 * 
 * @author Ilkka Leinonen
 * @version 1.1.0 - Added support for symmetric and asymmetric cryptography extensions.
 * @Since 1.3.0
 *
 */
public class CryptoSupport {

	/**
	 * Default character encoding set to 'ISO-8859-1'.
	 */
	private static final String DEFAULT_CHARACTER_ENCODING = "ISO-8859-1";
	
	/**
	 * Encrypter for managing encryption functions. 
	 */
	
	/**
	 * Crypter for managing decryption functions. 
	 */
	private Crypter crypter;
	
	/**
	 * Defines the symmetric public key path.
	 */
	private String asymmetricPublicKeyPath;
	
	/**
	 * Defines the symmetric private key path.
	 */
	private String asymmetricPrivateKeyPath;
	
	/**
	 * Defines the symmetric key path.
	 */
	private String symmetricKeyPath;
	
	/**
	 * Setter for public key path.
	 * 
	 * @param asymmetricPublicKeyPath Represents the actual key path.
	 */
	public void setAsymmetricPublicKeyPath(String asymmetricPublicKeyPath) {
		this.asymmetricPublicKeyPath = asymmetricPublicKeyPath;
	}
	
	/**
	 * Setter for private key path.
	 * 
	 * @param asymmetricPrivateKeyPath Represents the actual key path.
	 */
	public void setAsymmetricPrivateKeyPath(String asymmetricPrivateKeyPath) {
		this.asymmetricPrivateKeyPath = asymmetricPrivateKeyPath;
	}
		

	
	public void setSymmetricKeyPath(String symmetricKeyPath) {
		this.symmetricKeyPath = symmetricKeyPath;
	}

	/**
	 * Constructor with public and private key paths.
	 * 
	 * @param rsaPublicKeyPath Represents the actual public key path.
	 * @param rsaPrivateKeyPath Represents the actual private key path.
	 */
	public CryptoSupport(String asymmetricPublicKeyPath, String asymmetricPrivateKeyPath) {
		try {
			this.asymmetricPublicKeyPath  = asymmetricPublicKeyPath;
			this.asymmetricPrivateKeyPath = asymmetricPrivateKeyPath;
			crypter = new Crypter(asymmetricPrivateKeyPath);
		} catch (KeyczarException keyczarException) {
			ExceptionUtil.throwSystemException("CryptoSupport initialization failed: " + keyczarException.getMessage(), keyczarException);
		}
	}
	
	/**
	 * Constructor with public key path.
	 * 
	 * @param symmetricKeyPath Represents the actual symmetric key path.
	 */
	public CryptoSupport(String symmetricKeyPath) {
		try {
			crypter = new Crypter(symmetricKeyPath);
		} catch (KeyczarException keyczarException) {
			ExceptionUtil.throwSystemException("CryptoSupport initialization failed: " + keyczarException.getMessage(), keyczarException);
		}
	}
	
	/**
	 * Encrypts byte buffers from inbound to outbound.
	 * 
	 * @param inboundBuffer Represents the actual plain inbound buffer.
	 * @param outboundBuffer Represents the actual encrypted outbound buffer.
	 */
	public void encrypt(ByteBuffer inboundBuffer, ByteBuffer outboundBuffer) {
		try {
			crypter.encrypt(inboundBuffer, outboundBuffer);
		} catch (KeyczarException keyczarException) {
			ExceptionUtil.throwSystemException("Encryption failed.", keyczarException);
		}
	}
	
	/**
	 * Decrypts byte buffers from inbound to outbound.
	 * 
	 * @param inboundBuffer Represents the actual encrypted inbound buffer.
	 * @param outboundBuffer Represents the actual decrypted outbound buffer.
	 */
	public void decrypt(ByteBuffer inboundBuffer, ByteBuffer outboundBuffer) {
		try {
			crypter.decrypt(inboundBuffer, outboundBuffer);
		} catch (KeyczarException keyczarException) {
			ExceptionUtil.throwSystemException("Decryption failed.", keyczarException);
		}
	}
	
	/**
	 * Encrypts the given bytes.
	 * 
	 * @param input Represents plain bytes.
	 * @return byte[] Represents the encrypted bytes.
	 */
	public byte[] encrypt(byte[] input) {
		try {
			return crypter.encrypt(input);
		} catch (KeyczarException keyczarException) {
			ExceptionUtil.throwSystemException("Encryption failed.", keyczarException);
		}
		return null;
	}
	
	/**
	 * Decrypts the given bytes.
	 * 
	 * @param input Represents the crypted bytes.
	 * @return byte[] Represents the decrypted bytes.
	 */
	public byte[] decrypt(byte[] input) {
		try {
			return crypter.decrypt(input);
		} catch (KeyczarException keyczarException) {
			ExceptionUtil.throwSystemException("Decryption failed.", keyczarException);
		}
		return null;
	}
	
	/**
	 * Encrypts input bytes and encodes Base64 presentation of the encrypted bytes.
	 * 
	 * @param input Represents the plain input byte array.
	 * @param encoding Represents the character encoding for byte array.
	 * @return String Represents the Base64 encoded String.
	 */
	public String encryptAndReturnBase64Presentation(byte[] input, String encoding) {
		ObjectInputStream objectInputStream = null;
		ByteArrayInputStream byteArrayInputStream = null;
		try {
			byteArrayInputStream = new ByteArrayInputStream(input);
			objectInputStream = new ObjectInputStream(byteArrayInputStream);
			Object plainObject = objectInputStream.readObject();
			if (plainObject instanceof String) {
				String stringObject = (String) plainObject;
				byte[] encodedBytes = stringObject.getBytes(encoding);
				byte[] encryptedBytes = crypter.encrypt(encodedBytes);
				return Base64.encodeBase64URLSafeString(encryptedBytes);
			}
		} catch (Throwable throwable) {
			ExceptionUtil.throwSystemException("Encryption failed: " + throwable.getMessage(), throwable);
		} finally {
			IOUtil.closeStream(byteArrayInputStream);
			IOUtil.closeStream(objectInputStream);
		}
		return null;
	}
	
	/**
	 * Base64 decodes given input bytes and decrypts encrypted bytes.
	 * 
	 * @param input Represents the Base64 presentation of the encrypted bytes.
	 * @param encoding Represents the character encoding for byte array.
	 * @return String Represents the plain decoded and decrypted <code>java.lang.String</code>.
	 */
	public String decryptAndReturnBase64Presentation(byte[] input, String encoding) {
		ObjectInputStream objectInputStream = null;
		ByteArrayInputStream byteArrayInputStream = null;
		try {
			byteArrayInputStream = new ByteArrayInputStream(input);
			objectInputStream = new ObjectInputStream(byteArrayInputStream);
			Object plainObject = objectInputStream.readObject();
			if (plainObject instanceof String) {
				String stringObject = (String) plainObject;
				byte[] encodedBytes = stringObject.getBytes(encoding);
				byte[] base64Decoded = Base64.decodeBase64(new String(encodedBytes));
				byte[] decryptedBytes = crypter.decrypt(base64Decoded);
				return new String(decryptedBytes);
			}
		} catch (Throwable throwable) {
			ExceptionUtil.throwSystemException("Decryption failed: " + throwable.getMessage(), throwable);
		} finally {
			IOUtil.closeStream(byteArrayInputStream);
			IOUtil.closeStream(objectInputStream);
		}
		return null;
	}
	
	/**
	 * Encrypts input bytes and encodes Base64 presentation of the encrypted bytes. Uses default character encoding for character set.
	 * 
	 * @param input Represents the plain input byte array.
	 * @return String Represents the Base64 encoded String.
	 */
	public String encryptAndReturnBase64Presentation(byte[] input) {
		return encryptAndReturnBase64Presentation(input, DEFAULT_CHARACTER_ENCODING);
	}
	
	/**
	 * Base64 decodes given input bytes and decrypts encrypted bytes.
	 * 
	 * @param input Represents the Base64 presentation of the encrypted bytes.
	 * @return String Represents the plain decoded and decrypted <code>java.lang.String</code>.
	 */
	public String decryptAndReturnBase64Presentation(byte[] input) {
		return decryptAndReturnBase64Presentation(input, DEFAULT_CHARACTER_ENCODING);
	}
	
}