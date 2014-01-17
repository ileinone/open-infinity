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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openinfinity.core.util.IOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Unit test for crypto support object.
 * 
 * @author Ilkka Leinonen
 * @version 1.1.0 - Added support for symmetric and asymmetric cryptography extensions.
 * @since 1.3.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:META-INF/spring/t-core-integration-test-context.xml")
public class CryptoSupportUnitTest {
	
	private static final String ENCODING = "ISO-8859-1";
	private static final String EXPECTED_PLAIN_TEXT = "Open Infinity Rocks";
	
	@Autowired
	@Qualifier("asymmetricCryptoSupport")
	private CryptoSupport asymmetricCryptoSupport;
	
	@Autowired
	@Qualifier("symmetricCryptoSupport")
	private CryptoSupport symmetricCryptoSupport;
	
	@Test
	public void givenPublicRSAKeyWhenEncryptingContentThenResultMustNotBePlainText() {
		byte[] actual = asymmetricCryptoSupport.encrypt(EXPECTED_PLAIN_TEXT.getBytes());
		assertNotSame(EXPECTED_PLAIN_TEXT, actual.toString());
	}
	
	@Test
	public void givenPublicAndPrivateRSAKeyWhenEncryptingContentAndDecryptingItThenResultMustBePlainText() {
		byte[] encryptedBytes = asymmetricCryptoSupport.encrypt(EXPECTED_PLAIN_TEXT.getBytes());
		byte[] actual = asymmetricCryptoSupport.decrypt(encryptedBytes);
		String actualPlainText = new String(actual);
		System.out.println(actualPlainText);
		assertEquals(EXPECTED_PLAIN_TEXT, actualPlainText);
	}
	
	@Test
	public void givenPublicRSAKeyWhenEncryptingContentWithPlainBytesThenResultMustNotBePlainText() {
		byte[] plainBytes = IOUtil.getBytes(EXPECTED_PLAIN_TEXT);
		String actual = asymmetricCryptoSupport.encryptAndReturnBase64Presentation(plainBytes, ENCODING);
		assertNotSame(EXPECTED_PLAIN_TEXT, actual);
	}
	
	@Test
	public void givenPublicAndPrivateRSAKeyWhenEncryptingContentWithPlainBytesAndDecryptingItThenResultMustBePlainText() {
		byte[] plainBytes = IOUtil.getBytes(EXPECTED_PLAIN_TEXT);
		String base64PresentationOfEncryptedBytes = asymmetricCryptoSupport.encryptAndReturnBase64Presentation(plainBytes, ENCODING);
		byte[] encryptedBase64Bytes = IOUtil.getBytes(base64PresentationOfEncryptedBytes);
		String actual = asymmetricCryptoSupport.decryptAndReturnBase64Presentation(encryptedBase64Bytes, ENCODING);
		String actualPlainText = new String(actual);
		System.out.println(actualPlainText);
		assertEquals(EXPECTED_PLAIN_TEXT, actualPlainText);
	}
	
	@Test
	public void givenSymmetricAESKeyWhenEncryptingContentThenResultMustNotBePlainText() {
		byte[] actual = symmetricCryptoSupport.encrypt(EXPECTED_PLAIN_TEXT.getBytes());
		assertNotSame(EXPECTED_PLAIN_TEXT, actual.toString());
	}
	
	@Test
	public void givenSymmetricAESKeyWhenEncryptingContentAndDecryptingItThenResultMustBePlainText() {
		byte[] encryptedBytes = symmetricCryptoSupport.encrypt(EXPECTED_PLAIN_TEXT.getBytes());
		byte[] actual = symmetricCryptoSupport.decrypt(encryptedBytes);
		String actualPlainText = new String(actual);
		System.out.println(actualPlainText);
		assertEquals(EXPECTED_PLAIN_TEXT, actualPlainText);
	}
	
	@Test
	public void givenSymmetricAESKeyWhenEncryptingContentWithPlainBytesThenResultMustNotBePlainText() {
		byte[] plainBytes = IOUtil.getBytes(EXPECTED_PLAIN_TEXT);
		String actual = symmetricCryptoSupport.encryptAndReturnBase64Presentation(plainBytes, ENCODING);
		assertNotSame(EXPECTED_PLAIN_TEXT, actual);
	}
	
	@Test
	public void givenSymmetricAESKeyWhenEncryptingContentWithPlainBytesAndDecryptingItThenResultMustBePlainText() {
		byte[] plainBytes = IOUtil.getBytes(EXPECTED_PLAIN_TEXT);
		String base64PresentationOfEncryptedBytes = symmetricCryptoSupport.encryptAndReturnBase64Presentation(plainBytes, ENCODING);
		byte[] encryptedBase64Bytes = IOUtil.getBytes(base64PresentationOfEncryptedBytes);
		String actual = symmetricCryptoSupport.decryptAndReturnBase64Presentation(encryptedBase64Bytes, ENCODING);
		String actualPlainText = new String(actual);
		System.out.println(actualPlainText);
		assertEquals(EXPECTED_PLAIN_TEXT, actualPlainText);
	}

}
