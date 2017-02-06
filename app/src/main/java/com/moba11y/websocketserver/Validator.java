package com.moba11y.websocketserver;

import com.google.gson.JsonObject;

/**
 * Validators validate incoming JSON Objects before the IncomingMessage can be constructed.
 */
interface Validator {

    class ValidatorException extends Message.MessageException {
        ValidatorException(String message) {
            super(message);
        }
        ValidatorException(Exception e) {
            super(e);
        }
    }

    /**
     * Called when an incoming messages needs validated.
     * @param jsonObject The object to be validated.
     * @return True if the jsonObject is valid.
     * @throws ValidatorException Failures can also be thrown as exceptions.
     */
    boolean validate(JsonObject jsonObject) throws ValidatorException;
}
