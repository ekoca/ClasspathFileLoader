package com.emre.loader;

import javax.ws.rs.core.MultivaluedMap;

import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * Indicates that an input field to a web service is missing.
 * 
 * @author Emre Koca
 */
public class InputValidationException extends Exception {

	public final MultivaluedMap<String, String> errors;

	private static final long serialVersionUID = 1;

	public InputValidationException(String message,
			MultivaluedMap<String, String> errors) {
		super(message);
		this.errors = errors;
	}

	public InputValidationException(String message) {
		this(message, null);
	}

	public static InputValidationException oneFieldError(String message,
			String field, String error) throws InputValidationException {
		MultivaluedMap<String, String> errors = new MultivaluedMapImpl();
		errors.add(field, error);
		throw new InputValidationException(message, errors);
	}
	
	public static InputValidationException oneFieldTwoErrors(String message,
			String field, String error1, String error2) throws InputValidationException {
		MultivaluedMap<String, String> errors = new MultivaluedMapImpl();
		errors.add(field, error1);
		errors.add(field, error2);
		throw new InputValidationException(message, errors);
	}

	@Override
	public String toString() {
		return super.toString() + '\n' + errors;
	}
}