package net.iubris.facri.console.actions.graph.utils.cache.persister.exception;

public class PersisterException extends Exception {

	private static final long serialVersionUID = 5568812581306093192L;

	public PersisterException() {
	}

	public PersisterException(String message) {
		super(message);
	}

	public PersisterException(Throwable cause) {
		super(cause);
	}

	public PersisterException(String message, Throwable cause) {
		super(message, cause);
	}

	public PersisterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
