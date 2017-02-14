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

import android.util.Base64;

import com.google.gson.JsonObject;

import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;

/**
 * A simple validator that confirms the RSA signature of a JSON message.
 */
@SuppressWarnings("unused")
public class ValidatorRSASignature implements Validator {

    private enum SignatureMethod {SHA256withRSA}

    private static final String PROP_SIGNATURE = "signature";

    private final PublicKey mPublicKey;
    private SignatureMethod mSignatureMethod;

    public ValidatorRSASignature(PublicKey publicKey) {
        mPublicKey = publicKey;
        mSignatureMethod = SignatureMethod.SHA256withRSA;
    }

    @Override
    public boolean validate(JsonObject jsonObject) throws ValidatorException {

        final String signatureString = jsonObject.get(PROP_SIGNATURE).getAsString();

        if (signatureString == null || signatureString.contentEquals("")) {
            throw new ValidatorException("There is no signature string in the message.\n" + jsonObject.toString());
        } else {
            jsonObject.remove(PROP_SIGNATURE);
        }

        final byte[] data = jsonObject.toString().getBytes();

        try {
            Signature signature = Signature.getInstance(mSignatureMethod.name());
            signature.initVerify(mPublicKey);
            signature.update(data, 0, data.length);

            if (!signature.verify(Base64.decode(signatureString.getBytes(), Base64.DEFAULT))) {
                throw new ValidatorException("Failed signature verification.  Signature incorrect.");
            }
        } catch (NoSuchAlgorithmException e) {
            //This can only happen if our enum names are off, so re-throw as a runtime exception
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new ValidatorException(e);
        }

        return true;
    }
}
