package net.robotmedia.billing.security;

public interface ISignatureValidator {

	/**
	 * Validates that the specified signature matches the computed signature on
	 * the specified signed data. Returns true if the data is correctly signed.
	 * 
	 * @param signedData
	 *            signed data
	 * @param signature
	 *            signature
	 * @return true if the data and signature match, false otherwise.
	 */
	public boolean validate(String signedData, String signature);

}