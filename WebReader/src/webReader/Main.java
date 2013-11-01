package webReader;

/*
 * TODO:
 *  -Add a minimum frequency input.
 *  -Add unit tests:
 *  	-URL parse test
 *  	-HTML parse test
 *  	-Graceful input fail test
 */

public class Main {
	private static Screen screen;

	public static void main(String[] args) {
		screen = new Screen(new URLParser());
		screen.Update();
	}
}
