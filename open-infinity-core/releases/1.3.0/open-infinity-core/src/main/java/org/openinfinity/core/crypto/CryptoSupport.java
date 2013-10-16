package org.openinfinity.core.crypto;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;

import org.apache.commons.codec.binary.Base64;
import org.keyczar.Crypter;
import org.keyczar.Encrypter;
import org.keyczar.exceptions.KeyczarException;
import org.openinfinity.core.util.ExceptionUtil;
import org.openinfinity.core.util.IOUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Object for supporting encryption and decryption of the entity fields. Handles Base64 encoding with <code>java.util.String</code> fields.
 * 
 * @author Ilkka Leinonen
 * @Version 1.0.0 - Initial version
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
	private Encrypter encrypter;
	
	/**
	 * Decrypter for managing decryption functions. 
	 */
	private Crypter crypter;
	
	/**
	 * Defines the public key path.
	 */
	@Value("${rsa.public.key.path}")
	private String rsaPublicKeyPath;
	
	/**
	 * Defines the private key path.
	 */
	@Value("${rsa.private.key.path}")
	private String rsaPrivateKeyPath;
	
	/**
	 * Setter for public key path.
	 * 
	 * @param rsaPublicKeyPath Represents the actual key path.
	 */
	public void setRsaPublicKeyPath(String rsaPublicKeyPath) {
		this.rsaPublicKeyPath = rsaPublicKeyPath;
	}
	
	/**
	 * Setter for private key path.
	 * 
	 * @param rsaPrivateKeyPath Represents the actual key path.
	 */
	public void setRsaPrivateKeyPath(String rsaPrivateKeyPath) {
		this.rsaPrivateKeyPath = rsaPrivateKeyPath;
	}

	/**
	 * Default constructor for the class.
	 */
	public CryptoSupport() {
		try {
			encrypter = new Encrypter(rsaPublicKeyPath);
			crypter = new Crypter(rsaPrivateKeyPath);
		} catch (KeyczarException keyczarException) {
			ExceptionUtil.throwSystemException("CryptoSupport initialization failed: " + keyczarException.getMessage(), keyczarException);
		}
	}
	
	/**
	 * Constructor with public and private key paths.
	 * 
	 * @param rsaPublicKeyPath Represents the actual public key path.
	 * @param rsaPrivateKeyPath Represents the actual private key path.
	 */
	public CryptoSupport(@Value("${rsa.public.key.path}") String rsaPublicKeyPath, @Value("${rsa.private.key.path}") String rsaPrivateKeyPath) {
		try {
			this.rsaPublicKeyPath  = rsaPublicKeyPath;
			this.rsaPrivateKeyPath = rsaPrivateKeyPath;
			encrypter = new Encrypter(rsaPublicKeyPath);
			crypter = new Crypter(rsaPrivateKeyPath);
		} catch (KeyczarException keyczarException) {
			ExceptionUtil.throwSystemException("CryptoSupport initialization failed: " + keyczarException.getMessage(), keyczarException);
		}
	}
	
	/**
	 * Constructor with public key path.
	 * 
	 * @param rsaPublicKeyPath Represents the actual public key path.
	 */
	public CryptoSupport(@Value("${rsa.public.key.path}") String rsaPublicKeyPath) {
		try {
			encrypter = new Encrypter(rsaPublicKeyPath);
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
			encrypter.encrypt(inboundBuffer, outboundBuffer);
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
			return encrypter.encrypt(input);
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