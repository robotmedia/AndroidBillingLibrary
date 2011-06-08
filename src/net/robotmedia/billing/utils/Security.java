/*	Copyright 2011 Robot Media SL <http://www.robotmedia.net>. All rights reserved.
*
*	This file is part of Android Billing Library.
*
*	Android Billing Library is free software: you can redistribute it and/or modify
*	it under the terms of the GNU Lesser Public License as published by
*	the Free Software Foundation, either version 3 of the License, or
*	(at your option) any later version.
*
*	Android Billing Library is distributed in the hope that it will be useful,
*	but WITHOUT ANY WARRANTY; without even the implied warranty of
*	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*	GNU Lesser Public License for more details.
*
*	You should have received a copy of the GNU Lesser Public License
*	along with Android Billing Library.  If not, see <http://www.gnu.org/licenses/>.
*/

package net.robotmedia.billing.utils;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashSet;

import net.robotmedia.billing.utils.AESObfuscator.ValidationException;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

public class Security {

	private static final String KEY_FACTORY_ALGORITHM = "RSA";
	private static HashSet<Long> knownNonces = new HashSet<Long>();
	private static final SecureRandom RANDOM = new SecureRandom();
	private static final String SIGNATURE_ALGORITHM = "SHA1withRSA";
	private static final String TAG = Security.class.getSimpleName();

	/** Generates a nonce (a random number used once). */
	public static long generateNonce() {
		long nonce = RANDOM.nextLong();
		knownNonces.add(nonce);
		return nonce;
	}

	/**
	 * Generates a PublicKey instance from a string containing the
	 * Base64-encoded public key.
	 * 
	 * @param encodedPublicKey
	 *            Base64-encoded public key
	 * @throws IllegalArgumentException
	 *             if encodedPublicKey is invalid
	 */
	public static PublicKey generatePublicKey(String encodedPublicKey) {
		try {
			byte[] decodedKey = Base64.decode(encodedPublicKey);
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_FACTORY_ALGORITHM);
			return keyFactory.generatePublic(new X509EncodedKeySpec(decodedKey));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (InvalidKeySpecException e) {
			Log.e(TAG, "Invalid key specification.");
			throw new IllegalArgumentException(e);
		} catch (Base64DecoderException e) {
			Log.e(TAG, "Base64 decoding failed.");
			throw new IllegalArgumentException(e);
		}
	}

	public static boolean isNonceKnown(long nonce) {
		return knownNonces.contains(nonce);
	}

	public static void removeNonce(long nonce) {
		knownNonces.remove(nonce);
	}

	/**
	 * Verifies that the signature from the server matches the computed
	 * signature on the data. Returns true if the data is correctly signed.
	 * 
	 * @param publicKey
	 *            public key associated with the developer account
	 * @param signedData
	 *            signed data from server
	 * @param signature
	 *            server signature
	 * @return true if the data and signature match
	 */
	public static boolean verify(PublicKey publicKey, String signedData, String signature) {
		Signature sig;
		try {
			sig = Signature.getInstance(SIGNATURE_ALGORITHM);
			sig.initVerify(publicKey);
			sig.update(signedData.getBytes());
			if (!sig.verify(Base64.decode(signature))) {
				Log.e(TAG, "Signature verification failed.");
				return false;
			}
			return true;
		} catch (NoSuchAlgorithmException e) {
			Log.e(TAG, "NoSuchAlgorithmException.");
		} catch (InvalidKeyException e) {
			Log.e(TAG, "Invalid key specification.");
		} catch (SignatureException e) {
			Log.e(TAG, "Signature exception.");
		} catch (Base64DecoderException e) {
			Log.e(TAG, "Base64 decoding failed.");
		}
		return false;
	}

	public static boolean verify(String signedData, String signature, String base64EncodedPublicKey) {
		if (signedData == null) {
			Log.e(TAG, "Data is null");
			return false;
		}
		PublicKey key = Security.generatePublicKey(base64EncodedPublicKey);
		return Security.verify(key, signedData, signature);
	}
	
	public static String obfuscate(Context context, byte[] salt, String original) {
		final AESObfuscator obfuscator = getObfuscator(context, salt);
		return obfuscator.obfuscate(original);
	}
	
	private static AESObfuscator _obfuscator = null;
	
	private static AESObfuscator getObfuscator(Context context, byte[] salt) {
		if (_obfuscator == null) {
			final String installationId = Installation.id(context);
			final String deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
			final String password = installationId + deviceId + context.getPackageName();
			_obfuscator = new AESObfuscator(salt, password);
		}
		return _obfuscator;
	}
		
	public static String unobfuscate(Context context, byte[] salt, String obfuscated) {
		final AESObfuscator obfuscator = getObfuscator(context, salt);
		try {
			return obfuscator.unobfuscate(obfuscated);
		} catch (ValidationException e) {
			Log.w(TAG, "Invalid obfuscated data or key");
		}
		return null;
	}

}
