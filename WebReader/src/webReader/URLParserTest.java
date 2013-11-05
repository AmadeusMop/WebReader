package webReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("deprecation")
public class URLParserTest {
	URLParser parser;
	
	@Before
	public void setUp() {
		System.out.println("hello");
		parser = new URLParser();
	}
	
	@Test
	public void HTMLGetTest() throws IOException {
		String s = "abcdefghijkl\nmnop{}";
		StringBufferInputStream stream = new StringBufferInputStream(s);
		assertEquals(s + "\0", (parser.getHTMLTest(stream)));
	}

	@Test
	public void URLParseTest() throws IllegalArgumentException, IOException {
		boolean b = false;
		InputStream stream = (new URL("http://www.something.com")).openStream();
		
		try {
			parser.setURL("hello");
			parser.parseURLTest();
		} catch(IllegalArgumentException e) {
			assertEquals("hello", e.getMessage());
			b = true;
		}
		
		assertTrue(b);
		
		parser.setURL("http://www.something.com");
		assertEquals(parser.getHTMLTest(stream), parser.parseURLTest());
	}
	
	@Test
	public void HTMLParseTest() {
		String html = "abscd asdf <body class='blueberry'>\nthis \n<div class='main'> hello </div>\nfoo <script> baz </script><script type='something'>spam</script>\n</body> bar";
		String[] strArray = new String[] {"this", "hello", "foo"};
		List<String> expected = Arrays.asList(strArray);
		
		assertEquals(expected, parser.parseHTMLTest(html));
	}
}
