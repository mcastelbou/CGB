package cgb.utils;

import org.apache.commons.validator.routines.IBANValidator;

/**
 * 
 */
public class CGBIbanValidator {

	private static CGBIbanValidator uniqueinstance;
	
	protected CGBIbanValidator() {}
	
	/**
	 * Fonction pour récupération du singleton.
	 * @return L'instance de validateur d'IBAN.
	 */
	public CGBIbanValidator getIntanceValidator() {
		if (uniqueinstance == null) {
			uniqueinstance = new CGBIbanValidator();
		}
		return uniqueinstance;
	}
	
	public boolean isIbanStructureValide(String iban) {
		//TODO ... 
		return iban.matches("[A-Z]{2}[0-9]{2}([0-9][A-Z]){23}");
	}
	
	public boolean isIbanValide(String iban) {
		//TODO ... 
		return IBANValidator.getInstance().isValid(iban);
	}
	
	public String getCodePays(String iban) {
		//TODO ...
		return null;
	}
	
	public String getCRC(String iban) {
		//TODO ...
		return null;
	}
	
	public String getBBAN(String iban) {
		//TODO ...
		return null;
	}
}
