/*
 *    Copyright 2017 Chris McMeeking
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.moba11y.websocketserver;

import com.google.gson.JsonObject;

/**
 * Validators validate incoming JSON Objects before the IncomingMessage can be constructed.
 */
public interface Validator {

    class ValidatorException extends Message.MessageException {
        public ValidatorException(String message) {
            super(message);
        }
        public ValidatorException(Exception e) {
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
