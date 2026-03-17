package cgb.utils;

import java.util.Random;

import org.apache.commons.validator.routines.IBANValidator;
import java.math.BigInteger;
import java.util.Random;

import org.apache.commons.validator.routines.IBANValidator;

public class IbanGenerator {

    private static final String COUNTRY_CODE = "FR";
    private static final Random RANDOM = new Random();

    public static String generateValidIban() {
        // 1. Génération BBAN  conforme norme FR
        String banque = String.format("%05d", RANDOM.nextInt(100000));
        String guichet = String.format("%05d", RANDOM.nextInt(100000));
        String compte = String.format("%011d", Math.abs(RANDOM.nextLong()) % 1_000_000_00000L);
        String cleRib = String.format("%02d", RANDOM.nextInt(100));
        
        String bban = banque + guichet + compte + cleRib;

        // 2. Calcul clé IBAN
        String checkDigits = calculateCheckDigits(COUNTRY_CODE, bban);

        // 3. IBAN final
        return COUNTRY_CODE + checkDigits + bban;
    }

    private static String calculateCheckDigits(String countryCode, String bban) {
        // 1 : réorganisation pour calcul check digit
        String reformatted = bban + countryCode + "00";
        // 2: conversion lettres → chiffres si besoin
        StringBuilder numeric = new StringBuilder();

        for (char c : reformatted.toCharArray()) {
            if (Character.isLetter(c)) {
                numeric.append((int) c - 55); // A=10, B=11...
            } else {
                numeric.append(c);
            }
        }

        // 3 : modulo 97 sécurisé avec big integer car Long environ 19 chiffres...
        BigInteger bigInt = new BigInteger(numeric.toString());
        int remainder = bigInt.mod(BigInteger.valueOf(97)).intValue();
        int checkDigits = 98 - remainder;
        /* Formatage du chiffre de contrôle en deux chiffres 
		% : Indique le début d'un spécificateur de format.
		0 : Indique que le nombre doit être complété avec des zéros à gauche si nécessaire.
		2 : Indique la largeur minimale du champ, ici 2 caractères.
		d : Indique que le type de donnée est un entier -->"décimal" pas un double ni float.
		ex: si nombre 5 --> 05  , si 12 --> 12
		*/ 
        return String.format("%02d", checkDigits);
    }
    
    public static void main(String[] args) {
        IBANValidator validator = IBANValidator.getInstance();
        for (int i = 0; i < 5; i++) {
            String iban = generateValidIban();
            System.out.println(iban + " -> " + validator.isValid(iban));
        }
    }
}
