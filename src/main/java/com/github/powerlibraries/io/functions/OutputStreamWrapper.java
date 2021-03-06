package com.github.powerlibraries.io.functions;

import java.io.IOException;
import java.io.OutputStream;

/**This class is a simple functional interface used to wrap an OutputStream with another. Instead of implementing this 
 * interface it is often enough to use a constructor, e.g. BufferedOutputStream::new.
 * 
 * @author Manuel Hegner
 */
@FunctionalInterface
public interface OutputStreamWrapper {
	
	/**
	 * This method should wrap the given OutputStream out with another OutputStream (e.g. compression) and return it.
	 * @param out the stream given
	 * @return a wrapped OutputStream
	 * @throws IOException thrown by some OutputStream constructors
	 */
	public OutputStream wrap(OutputStream out) throws IOException;
}
