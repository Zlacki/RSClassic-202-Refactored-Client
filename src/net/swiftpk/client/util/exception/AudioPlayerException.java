package net.swiftpk.client.util.exception;

public class AudioPlayerException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -339236507398341195L;

	// Parameterless Constructor
	public AudioPlayerException() {
	}

	// Constructor that accepts a message
	public AudioPlayerException(String message) {
		super(message);
	}

}
