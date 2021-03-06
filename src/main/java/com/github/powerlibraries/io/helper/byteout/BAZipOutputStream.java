package com.github.powerlibraries.io.helper.byteout;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.util.zip.ZipOutputStream;

/**
 * This class extends a {@link ZipOutputStream} with the ability to directly access an underlying 
 * {@link ByteArrayOutputStream}.
 * 
 * @see DataOutputStream
 * @see ByteArrayWrapper
 * @author Manuel Hegner
 */
public class BAZipOutputStream extends ZipOutputStream implements ByteArrayWrapper {
	
	private ByteArrayOutputStream underlyingOutput;

	public BAZipOutputStream(OutputStream outputStream, ByteArrayOutputStream underlyingOutput) {
		super(outputStream);
		this.underlyingOutput=underlyingOutput;
	}
	
	/**
     * Converts the buffer's contents into a string decoding bytes using the
     * platform's default character set. The length of the new <tt>String</tt>
     * is a function of the character set, and hence may not be equal to the
     * size of the buffer.
     *
     * <p> This method always replaces malformed-input and unmappable-character
     * sequences with the default replacement string for the platform's
     * default character set. The {@linkplain java.nio.charset.CharsetDecoder}
     * class should be used when more control over the decoding process is
     * required.
     *
     * @return String decoded from the buffer's contents.
     */
	@Override
	public String toString() {
		return getUnderlyingOutput().toString();
	}

	@Override
	public ByteArrayOutputStream getUnderlyingOutput() {
		return underlyingOutput;
	}
}
