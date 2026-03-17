package cgb.utils;

import org.apache.commons.validator.routines.IBANValidator;

import cgb.transfer.exception.InvalidIbanFormatException;
import cgb.transfer.exception.InvalidUnCheckableIbanException;

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
	public static CGBIbanValidator getIntanceValidator() {
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
	 * @throws InvalidIbanFormatException 
	 */
	public boolean isIbanStructureValide(String iban) throws InvalidIbanFormatException {
		if (iban.matches("^FR[0-9]{25}$")) {
			return true;
		}
		throw new InvalidIbanFormatException();
	}

	/**
	 * Même fonction que la structure mais avec une vérification du CRC.
	 * 
	 * @param iban  L'IBAN du même boug.
	 * @return  True si l'IBAN et son CRC sont valides, sinon False.
	 * @throws InvalidIbanFormatException
	 * @throws InvalidUnCheckableIbanException 
	 */
	public boolean isIbanValide(String iban) throws InvalidIbanFormatException, InvalidUnCheckableIbanException {
		if (this.isIbanStructureValide(iban)) {
			if (IBANValidator.getInstance().isValid(iban)) {
				return true;
			}
			throw new InvalidUnCheckableIbanException();
		}
		return false;
	}

	/**
	 * Getter des 2 premiers caractères de l'IBAN.
	 * 
	 * @param iban  L'IBAN de Jean Castex.
	 * @return  'FR'.
	 * @throws InvalidIbanFormatException 
	 */
	public String getCodePays(String iban) throws InvalidIbanFormatException {
		if (this.isIbanStructureValide(iban)) {
			return iban.substring(0, 2);
		}
		return null;
	}

	/**
	 * Getter des 2 chiffres après le 'FR'.
	 * 
	 * @param iban  Le Liban.
	 * @return  CR7 en string.
	 * @throws InvalidIbanFormatException 
	 */
	public String getCRC(String iban) throws InvalidIbanFormatException {
		if (this.isIbanStructureValide(iban)) {
			return iban.substring(2, 4);
		}
		return null;
	}

	/**
	 * Getter du BBAN.
	 * 
	 * @param iban  L'IBAN du compte actuellement traité.
	 * @return  Le reste de L'IBAN
	 * @throws InvalidIbanFormatException 
	 */
	public String getBBAN(String iban) throws InvalidIbanFormatException {
		if (this.isIbanStructureValide(iban)) {
			return iban.substring(4);
		}
		return null;
	}
}
