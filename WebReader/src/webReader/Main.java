package webReader;

/*
 * TODO:
 *  -Switch results area (wordsList) from GridLayout to BoxLayout?
 *  -Maybe add a multi-result grid for comparing many sites?
 *  -Associate results with specific URLs
 */

public class Main {
	private static Screen screen;

	public static void main(String[] args) {
		screen = new Screen(new URLParser());
		screen.Update();
	}
}
