package com.github.powerlibraries.io.helper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.github.powerlibraries.io.functions.InputStreamWrapper;
import com.github.powerlibraries.io.functions.OutputStreamWrapper;

/**
 * This class is a central registry which maps common compression extensions to its respective in and ouputstreams.
 * It supports gz and zip extensions and can be easily extended by calling one of the registerWrapper methods.
 * @author Manuel Hegner
 *
 */
public class CompressorRegistry {

	private static CompressorRegistry INSTANCE;
	
	private HashMap<String, InputStreamWrapper> extensionInputMap;
	private HashMap<String, OutputStreamWrapper> extensionOutputMap;
	
	private CompressorRegistry() {
		extensionInputMap=new HashMap<>();
		extensionOutputMap=new HashMap<>();
		registerWrapper("gz", GZIPInputStream::new, GZIPOutputStream::new);
	}
	
	/**
	 * @return the singleton instance of the registry
	 */
	public static CompressorRegistry getInstance() {
		if(INSTANCE==null)
			INSTANCE=new CompressorRegistry();
		return INSTANCE;
	}

	/**
	 * @param fileName the name of the file or an url or something else that ends with an file extension
	 * @return true, if there is any specific {@link InputStreamWrapper} known to this class that could wrap
	 * the specified file
	 */
	public boolean canWrapInput(String fileName) {
		int index;
		if((index=fileName.lastIndexOf('.'))>=0) {
			if(extensionInputMap.containsKey(fileName.substring(index+1)))
				return true;
			else
				return false;
		}
		return false;
	}
	
	/**
	 * @param fileName the name of the file or an url or something else that ends with an file extension
	 * @return true, if there is any specific {@link OutputStreamWrapper} known to this class that could wrap
	 * the specified file
	 */
	public boolean canWrapOutput(String fileName) {
		int index;
		if((index=fileName.lastIndexOf('.'))>=0) {
			if(extensionOutputMap.containsKey(fileName.substring(index+1)))
				return true;
			else
				return false;
		}
		return false;
	}
	
	/**
	 * This method will wrap the provided stream with a decompressing stream if it recognizes the extension of 
	 * the given file
	 * @param fileName the name of the file which is used to choose the wrapper
	 * @param in the stream to wrap
	 * @return a wrapped stream or the given stream itself
	 * @throws IOException if the wrapper throws an IOException
	 */
	public InputStream wrap(String fileName, InputStream in) throws IOException {
		int lastIndex=fileName.length();
		int index=fileName.length();
		while((index=fileName.lastIndexOf('.',index-1))>=0) {
			InputStreamWrapper wrapper=extensionInputMap.get(fileName.substring(index+1,lastIndex));
			if(wrapper==null)
				return in;
			in=wrapper.wrap(in);
			lastIndex=index;
		}
		return in;
	}
	
	/**
	 * This method will wrap the provided stream with a decompressing stream if it recognizes the extension of 
	 * the given file
	 * @param fileName the name of the file which is used to choose the wrapper
	 * @param out the stream to wrap
	 * @return a wrapped stream or the given stream itself
	 * @throws IOException if the wrapper throws an IOException
	 */
	public OutputStream wrap(String fileName, OutputStream out) throws IOException {
		int lastIndex=fileName.length();
		int index=fileName.length();
		while((index=fileName.lastIndexOf('.',index-1))>=0) {
			OutputStreamWrapper wrapper=extensionOutputMap.get(fileName.substring(index+1,lastIndex));
			if(wrapper==null)
				return out;
			out=wrapper.wrap(out);
			lastIndex=index;
		}
		return out;
	}
	
	/**
	 * This method is used to register new wrappers for extensions. Mostly it is enough to provide this method 
	 * with the constructors of the right class, e.g. <pre>registerWrapper("gz", GZIPInputStream::new, GZIPOutputStream::new);</pre>
	 * @param fileExtension the extension that should use the given wrapping streams
	 * @param inWrapper the decompressing InputStream
	 * @param outWrapper the compressing OutputStream
	 * @return true, if no other wrapping stream was overwritten
	 */
	public boolean registerWrapper(String fileExtension, InputStreamWrapper inWrapper, OutputStreamWrapper outWrapper) {
		boolean nonOverwritten=extensionInputMap.put(fileExtension, inWrapper)==null;
		nonOverwritten&=extensionOutputMap.put(fileExtension, outWrapper)==null;
		return nonOverwritten;
	}
	
	/**
	 * This method is used to register new wrappers for extensions. Mostly it is enough to provide this method 
	 * with the constructors of the right class, e.g. <pre>registerWrapper("gz", GZIPInputStream::new);</pre>
	 * @param fileExtension the extension that should use the given wrapping streams
	 * @param inWrapper the decompressing InputStream
	 * @return the wraping stream that was registered for the extension before
	 */
	public InputStreamWrapper registerWrapper(String fileExtension, InputStreamWrapper inWrapper) {
		return extensionInputMap.put(fileExtension, inWrapper);
	}
	
	/**
	 * This method is used to register new wrappers for extensions. Mostly it is enough to provide this method 
	 * with the constructors of the right class, e.g. <pre>registerWrapper("gz", GZIPOutputStream::new);</pre>
	 * @param fileExtension the extension that should use the given wrapping streams
	 * @param outWrapper the compressing OutputStream
	 * @return the wraping stream that was registered for the extension before
	 */
	public OutputStreamWrapper registerWrapper(String fileExtension, OutputStreamWrapper outWrapper) {
		return extensionOutputMap.put(fileExtension, outWrapper);
	}
}
