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
package org.openinfinity.core.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

import org.openinfinity.core.exception.SystemException;

/**
 * Utility for handling stream, reader and writer objects.
 * 
 * @author Ilkka Leinonen
 * @version 1.0.0 Initial version
 * @since 1.0.0
 */
public class IOUtil {

	private static final String
	ERROR_COPYING_STREAM = "Error copying stream: ",
	ERROR_CLOSING_STREAM = "Error closing stream: ",
	ERROR_CLOSING_READER = "Error closing reader: ",
	ERROR_CLOSING_WRITER = "Error closing writer: ",
	ERROR_IS_NULL = " is null";
	
	/**
	 * Null safe operator for stream copying from inpustream to outputstream. 
	 * 
	 * @param inputStream - represents the inputstream of the <code>serializable</code> object.
	 * @param outputStream - represents the outputstream of the <code>serializable</code> object.
	 * @throws SystemException - when exceptional behaviour happens during IO-operation.
	 */
	public static void copyStream(InputStream inputStream, OutputStream outputStream) throws SystemException {
		try {
			if(inputStream == null) {
				ExceptionUtil.throwSystemException("InputStream" + ERROR_IS_NULL, new NullPointerException("Inpustream is null"));
			}
			if(outputStream == null) {	
				ExceptionUtil.throwSystemException("Outputstream" + ERROR_IS_NULL, new NullPointerException("Outpustream is null"));
			}
			int data;
			while((data=inputStream.read())!=-1) {
				outputStream.write(data);
			}
		} catch (IOException ioException) {
			ExceptionUtil.throwSystemException(ERROR_COPYING_STREAM + ioException.toString(), ioException);
		}
	}

	/**
	 * Closes the <code>OutputStream</code> object (NULL safe).
	 * 
	 * @param outputStream - represents the <code>OutputStream</code> object.
	 * @throws SystemException - when exceptional behaviour happens during IO-operation.
	 */
	public static void closeStream(OutputStream outputStream) throws SystemException {
		if(outputStream!=null) {
			try {
				outputStream.close();
				outputStream = null;
			} catch (IOException ioException) {
				ExceptionUtil.throwSystemException(ERROR_CLOSING_STREAM + ioException.toString(), ioException);
			}
		}	
	}

	/**
	 * Closes the <code>InputStream</code> object (NULL safe).
	 * 
	 * @param outputStream - represents the <code>InputStream</code> object.
	 * @throws ServiceSystemException - when exceptional behaviour happens during IO-operation.
	 */
	public static void closeStream(InputStream inputStream) throws SystemException {
		if(inputStream!=null) {
			try {
				inputStream.close();
				inputStream = null;
			} catch (IOException ioException) {
				ExceptionUtil.throwSystemException(ERROR_CLOSING_STREAM + ioException.toString(), ioException);
			}
		}
	}
	
	/**
	 * Closes the <code>Reader</code> object (NULL safe).
	 * 
	 * @param reader - represents the <code>Reader</code> object.
	 * @throws SystemException - when exceptional behaviour happens during IO-operation.
	 */
	public static void closeReader(Reader reader) throws SystemException {
		if(reader!=null) {
			try {
				reader.close();
				reader = null;
			} catch (IOException ioException) {
				ExceptionUtil.throwSystemException(ERROR_CLOSING_READER + ioException.toString(), ioException);	
			}
		}
	}
	
	/**
	 * Closes the <code>Writer</code> object (NULL safe).
	 * 
	 * @param Writer - represents the <code>Writer</code> object.
	 * @throws SystemException - when exceptional behaviour happens during IO-operation.
	 */
	public static void closeWriter(Writer writer) throws SystemException {
		if(writer!=null) {
			try {
				writer.close();
				writer = null;
			} catch (IOException ioException) {
				ExceptionUtil.throwSystemException(ERROR_CLOSING_WRITER + ioException.toString(), ioException);	
			}
		}
	}

	/**
	 * Returns bytes of the serialized object;
	 * 
	 * @param serilizableObject - Object to be serialized to bytes.
	 * @return byte[] - Array of object bytes.
	 */
	public static byte[] getBytes(Object serilizableObject) {
    	byte[] bytes = null;
    	ByteArrayOutputStream byteArrayOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
	    	byteArrayOutputStream = new ByteArrayOutputStream();
	        objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
	        objectOutputStream.writeObject(serilizableObject);
	        objectOutputStream.flush();
	        bytes = byteArrayOutputStream.toByteArray();
	        return bytes;
        } catch(Throwable throwable) {
        	ExceptionUtil.throwSystemException("IO exception occurred while reading stream: " + throwable.toString(), throwable);
        } finally {
        	IOUtil.closeStream(objectOutputStream);
        	IOUtil.closeStream(byteArrayOutputStream);
	    }
        return bytes;
    }
	
}
