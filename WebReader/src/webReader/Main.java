package webReader;

import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.net.URL;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
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
	
	public static String parseHTML(InputStream s) {
		return "";
	}
}
