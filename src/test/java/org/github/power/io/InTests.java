package org.github.power.io;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.zip.ZipInputStream;

import org.junit.Assert;
import org.junit.Test;

import com.github.powerlibraries.io.In;

public class InTests {

	@Test
	public void testFileUTF8() throws IOException {
		ArrayList<String> expected=loadExpected(StandardCharsets.UTF_8);
		
		//test
		try(BufferedReader in=In.file("target/test-classes/utf8test.txt").withUTF8().asReader()) {
			String l;
			Iterator<String> expectedIt=expected.iterator();
			while((l=in.readLine())!=null)
				Assert.assertEquals(expectedIt.next(), l);
			Assert.assertFalse(expectedIt.hasNext());
		}
	}
	
	@Test
	public void testUncompressing() throws IOException {
		ArrayList<String> expected=loadExpected(StandardCharsets.UTF_8);
		
		//test
		try(BufferedReader in=In.file("target/test-classes/utf8test.txt.gz").decompress().withUTF8().asReader()) {
			String l;
			Iterator<String> expectedIt=expected.iterator();
			while((l=in.readLine())!=null)
				Assert.assertEquals(expectedIt.next(), l);
			Assert.assertFalse(expectedIt.hasNext());
		}
	}
	
	@Test
	public void testUncompressingZip() throws IOException {
		ArrayList<String> expected=loadExpected(StandardCharsets.UTF_8);
		
		//test
		try(ZipInputStream in=In.file("target/test-classes/utf8test.zip").asZip()) {
			in.getNextEntry();
			try(BufferedReader in2=In.stream(in).withUTF8().asReader()) {
				String l;
				Iterator<String> expectedIt=expected.iterator();
				while((l=in2.readLine())!=null)
					Assert.assertEquals(expectedIt.next(), l);
				Assert.assertFalse(expectedIt.hasNext());
			}
		}
	}
	
	private ArrayList<String> loadExpected(Charset charset) throws IOException {
		ArrayList<String> expected=new ArrayList<>();
		try(BufferedReader in=new BufferedReader(new InputStreamReader(new FileInputStream("target/test-classes/utf8test.txt"), charset))) {
			String l;
			while((l=in.readLine()) != null)
				expected.add(l);
		}
		return expected;
	}

	@Test
	public void testFileDefault() throws IOException {
		ArrayList<String> expected=loadExpected(Charset.defaultCharset());
		
		//test
		try(BufferedReader in=In.file("target/test-classes/utf8test.txt").asReader()) {
			String l;
			Iterator<String> expectedIt=expected.iterator();
			while((l=in.readLine())!=null)
				Assert.assertEquals(expectedIt.next(), l);
			Assert.assertFalse(expectedIt.hasNext());
		}
	}
	
	@Test
	public void testBase64() throws IOException {
		ArrayList<String> expected=loadExpected(StandardCharsets.UTF_8);
		for(String l:expected) {
			String base64=Base64.getEncoder().encodeToString(l.getBytes(StandardCharsets.UTF_8));
			Assert.assertEquals(l, In.string(base64).decodeBase64().withUTF8().readAll());
		}
	}

	@Test
	public void testResource() throws IOException {
		ArrayList<String> expected=loadExpected(StandardCharsets.UTF_8);
		//test
		try(BufferedReader in=In.resource(InTests.class, "/utf8test.txt").withUTF8().asReader()) {
			String l;
			Iterator<String> expectedIt=expected.iterator();
			while((l=in.readLine())!=null)
				Assert.assertEquals(expectedIt.next(), l);
			Assert.assertFalse(expectedIt.hasNext());
		}
	}
}
