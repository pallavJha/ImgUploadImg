package pl.imguploadimg.base.exception;

import java.io.IOException;

public class NoContentTypeAvailabeException extends IOException {

	/**
	 * NoContentTypeAvailabeException
	 */
	private static final long serialVersionUID = 1L;

	public NoContentTypeAvailabeException() {

    }

    public NoContentTypeAvailabeException(String message) {
        super(message);
    }

    public NoContentTypeAvailabeException(Throwable cause) {
        super(cause);
    }

    public NoContentTypeAvailabeException(String message, Throwable cause) {
        super(message, cause);
    }
	
}
