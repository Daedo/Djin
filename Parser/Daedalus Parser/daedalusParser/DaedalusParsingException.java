package daedalusParser;

public class DaedalusParsingException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7702085361228746563L;

	public DaedalusParsingException(String message) {
		super("Parsing Exception:\n"+message);
	}
}
