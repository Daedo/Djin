package codeNode;

public class DebugMessage {
	public static boolean debug = true;
	public static void log(String message) {
		if(debug) {
			System.out.println("Debug: "+message);
		}
	}
}
