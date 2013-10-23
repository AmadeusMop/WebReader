package webReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		try {
			String html = getHTML(parseURL("http://en.wikipedia.org/wiki/Mandelbrot_Set"));
			String parsed = parseHTML(html);
		} catch(IOException e) {
			System.out.println(e);
		}
		
		String test = "</ul>";
		test += "	<div style=\"clear:both\"></div>";
		test += "</div>";
		test += "<script>/*<![CDATA[*/window.jQuery && jQuery.ready();/*]]>*/</script><script>if(window.mw){";
		test += "mw.loader.state({\"site\":\"loading\",\"user\":\"ready\",\"user.groups\":\"ready\"});";
		test += "}</script>";
		test += "<script>if(window.mw){";
		test += "mw.loader.load([\"mobile.desktop\",\"mediawiki.action.view.postEdit\",\"mediawiki.user\",\"mediawiki.hidpi\",\"mediawiki.page.ready\",\"mediawiki.searchSuggest\",\"ext.cite\",\"ext.gadget.teahouse\",\"ext.gadget.ReferenceTooltips\",\"ext.gadget.DRN-wizard\",\"ext.gadget.charinsert\",\"mw.MwEmbedSupport.style\",\"ext.articleFeedbackv5.startup\",\"ext.wikimediaEvents.ve\",\"ext.navigationTiming\",\"schema.UniversalLanguageSelector\",\"ext.uls.eventlogger\",\"mw.PopUpMediaTransform\",\"skins.vector.collapsibleNav\"],null,true);";
		test += "}</script>";
		test += "<script src=\"//bits.wikimedia.org/en.wikipedia.org/load.php?debug=false&amp;lang=en&amp;modules=site&amp;only=scripts&amp;skin=vector&amp;*\"></script>";
		test += "<!-- Served by mw1179 in 0.204 secs. -->";
		test += "	</body>";
		test += "</html>";
		
		System.out.println("\n\nSPLIT TEST\n" + Arrays.toString(test.split("<script.*?>(.|$|^)*?</script>")));
		
	}
	
	public static InputStream parseURL(String s) {
		try {
			URL url = new URL(s);
			return url.openStream();
		} catch(Exception e) {
			System.out.println(e);
			return new StringBufferInputStream(s);
		}
	}
	
	public static String getHTML(InputStream s) throws IOException {
		byte[] barray = new byte[20];
		byte[] temp;
		int i = 0, b = s.read();
		while(b != -1) {
			if(i == barray.length) {
				temp = Arrays.copyOf(barray, i+20);
				barray = temp;
			}
			barray[i] = (byte)b;
			i++;
			b = s.read();
		}
		String str = new String(barray);
		//str = 
		return str;
	}
	
	private static String parseHTML(String s) {
		//System.out.println(s);
		Pattern p = Pattern.compile("<body.*?>.*?</body>", Pattern.DOTALL);
		Matcher m = p.matcher(s);
		m.find();
		s = s.substring(m.start(), m.end());

		System.out.println("TEST 1:");
		String[] split = s.split("(<script.*?>(.|$|^)*?</script>|<(.|$^)*?>|[^a-zA-Z]*?)?+");
		for(String str : split) {
			System.out.print(str.trim());
		}
		String[] strlist = s.split("<.*?>");
		System.out.println("TEST:");
		for(String str : strlist) {
			//if(!str.trim().equals("")) System.out.println(str);
		}
		return s;
	}
}
