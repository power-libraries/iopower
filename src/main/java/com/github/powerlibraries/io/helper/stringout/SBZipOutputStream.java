package com.github.powerlibraries.io.helper.stringout;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.util.zip.ZipOutputStream;

/**
 * This class extends a {@link ZipOutputStream} with the ability to directly access an underlying 
 * {@link ByteArrayOutputStream}.
 * 
 * @see DataOutputStream
 * @see SBWrapper
 * @author Manuel Hegner
 */
public class SBZipOutputStream extends ZipOutputStream implements SBWrapper {
	
	private StringBuilderOutputStream underlyingOutput;

	public SBZipOutputStream(OutputStream outputStream, StringBuilderOutputStream underlyingOutput) {
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
	public StringBuilderOutputStream getUnderlyingOutput() {
		return underlyingOutput;
	}
}
