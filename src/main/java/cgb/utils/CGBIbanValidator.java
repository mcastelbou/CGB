package cgb.utils;

import org.apache.commons.validator.routines.IBANValidator;

/**
 * Classe de validation du format des IBAN de l'application CGB
 */
public class CGBIbanValidator {

	private static CGBIbanValidator uniqueinstance;

	protected CGBIbanValidator() {}

	/**
	 * Fonction pour récupération du singleton.
	 * 
	 * @return L'instance de validateur d'IBAN.
	 */
	public CGBIbanValidator getIntanceValidator() {
		if (uniqueinstance == null) {
			uniqueinstance = new CGBIbanValidator();
		}
		return uniqueinstance;
	}

	/**
	 * Fonction de vérification de structure d'un IBAN français tel que :
	 * FRXXXXXXXXXXXXXXXXXXXXXXXXX où X est un nombre.
	 * 
	 * @param iban  L'IBAN d'un boug.
	 * @return  True si la structure est bonne, sinon False.
	 */
	public boolean isIbanStructureValide(String iban) {
		return iban.matches("FR[0-9]{25}");
	}

	/**
	 * Même fonction que la structure mais avec une vérification du CRC.
	 * 
	 * @param iban  L'IBAN du même boug.
	 * @return  True si l'IBAN et son CRC sont valides, sinon False.
	 */
	public boolean isIbanValide(String iban) {
		return IBANValidator.getInstance().isValid(iban);
	}

	/**
	 * Getter des 2 premiers caractères de l'IBAN.
	 * 
	 * @param iban  L'IBAN de Jean Castex.
	 * @return  'FR'.
	 */
	public String getCodePays(String iban) {
		return iban.substring(0, 2);
	}

	/**
	 * Getter des 2 chiffres après le 'FR'.
	 * 
	 * @param iban  Le Liban.
	 * @return  CR7 en string.
	 */
	public String getCRC(String iban) {
		return iban.substring(2, 4);
	}

	/**
	 * Getter du BBAN.
	 * 
	 * @param iban  L'IBAN du compte actuellement traité.
	 * @return  Le reste de L'IBAN
	 */
	public String getBBAN(String iban) {
		return iban.substring(4);
	}
}
