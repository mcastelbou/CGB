package cgb.transfer.exemple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cgb.transfer.entity.Account;
import cgb.transfer.repository.AccountRepository;
import cgb.utils.IbanGenerator;
import jakarta.annotation.PostConstruct;

/**
 * Classe responsable de l'initialisation des données des comptes dans la base H2.
 */
@Component
public class DatabaseInitializer {

	/*
	 * @Autowired private final AccountRepository accountRepository;
	 * 
	 * Possibilité de faire une injection par l'attribut, mais il est recommander de
	 * la faire par constructeur comme présenté ci-dessous
	 * 
	 */

	private final AccountRepository accountRepository;

	@Autowired
	public DatabaseInitializer(AccountRepository accountRepository) {
		// super();
		this.accountRepository = accountRepository;
	}

	@PostConstruct
	public void init() {
		// Vérifiez si la base de données est vide avant d'insérer des données
		if (accountRepository.count() == 0) {
			insertSampleDataNetanew(accountRepository);
		}
	}

	public static void insertSampleDataOld(AccountRepository accountRepository) {
		// Insérer des comptes d'exemple
		Account account1 = new Account();
		account1.setAccountNumber("123456789");
		account1.setSolde(300.00);
		accountRepository.save(account1);

		Account account2 = new Account();
		account2.setAccountNumber("987654321");
		account2.setSolde(500.00);
		accountRepository.save(account2);

		Account account3 = new Account();
		account3.setAccountNumber("456789123");
		account3.setSolde(2000.00);
		accountRepository.save(account3);
	}

	/**
     * Fonction de valorisation de la base appellée si cette dernière est vide.
     * @param accountRepository  L'instance de Repository actuellement utilisée.
     */
	public static void insertSampleDataOldNew(AccountRepository accountRepository) {
		// Insérer des comptes d'exemple
		Account account1 = new Account();
		account1.setAccountNumber("123456789");
		account1.setSolde(300.00);
		accountRepository.save(account1);

		Account account2 = new Account();
		account2.setAccountNumber("234567891");
		account2.setSolde(500.00);
		accountRepository.save(account2);

		Account account3 = new Account();
		account3.setAccountNumber("345678912");
		account3.setSolde(2000.00);
		accountRepository.save(account3);

		Account account4 = new Account();
		account4.setAccountNumber("456789123");
		account4.setSolde(1000.00);
		accountRepository.save(account4);

		Account account5 = new Account();
		account5.setAccountNumber("567891234");
		account5.setSolde(1000.00);
		accountRepository.save(account5);

		Account account6 = new Account();
		account6.setAccountNumber("678912345");
		account6.setSolde(1000.00);
		accountRepository.save(account6);

		Account account7 = new Account();
		account7.setAccountNumber("789123456");
		account7.setSolde(1000.00);
		accountRepository.save(account7);

		Account account8 = new Account();
		account8.setAccountNumber("891234568");
		account8.setSolde(1000.00);
		accountRepository.save(account8);
	}

	public static void insertSampleDataNetanew(AccountRepository accountRepository) {
		// Insérer des comptes d'exemple avec un IBAN
		Account account1 = new Account();
		account1.setAccountNumber(IbanGenerator.generateValidIban());
		account1.setSolde(300.00);
		accountRepository.save(account1);

		Account account2 = new Account();
		account2.setAccountNumber(IbanGenerator.generateValidIban());
		account2.setSolde(500.00);
		accountRepository.save(account2);

		Account account3 = new Account();
		account3.setAccountNumber(IbanGenerator.generateValidIban());
		account3.setSolde(2000.00);
		accountRepository.save(account3);

		Account account4 = new Account();
		account4.setAccountNumber(IbanGenerator.generateValidIban());
		account4.setSolde(1000.00);
		accountRepository.save(account4);

		Account account5 = new Account();
		account5.setAccountNumber(IbanGenerator.generateValidIban());
		account5.setSolde(1000.00);
		accountRepository.save(account5);

		Account account6 = new Account();
		account6.setAccountNumber(IbanGenerator.generateValidIban());
		account6.setSolde(1000.00);
		accountRepository.save(account6);

		Account account7 = new Account();
		account7.setAccountNumber(IbanGenerator.generateValidIban());
		account7.setSolde(1000.00);
		accountRepository.save(account7);

		Account account8 = new Account();
		account8.setAccountNumber(IbanGenerator.generateValidIban());
		account8.setSolde(1000.00);
		accountRepository.save(account8);

		Account account9 = new Account();
		account9.setAccountNumber(IbanGenerator.generateValidIban());
		account9.setSolde(1000.00);
		accountRepository.save(account9);

		Account account10 = new Account();
		account10.setAccountNumber(IbanGenerator.generateValidIban());
		account10.setSolde(1000.00);
		accountRepository.save(account10);

		Account account11 = new Account();
		account11.setAccountNumber(IbanGenerator.generateValidIban());
		account11.setSolde(1000.00);
		accountRepository.save(account11);

		Account account12 = new Account();
		account12.setAccountNumber(IbanGenerator.generateValidIban());
		account12.setSolde(1000.00);
		accountRepository.save(account12);

		Account account13 = new Account();
		account13.setAccountNumber(IbanGenerator.generateValidIban());
		account13.setSolde(1000.00);
		accountRepository.save(account13);

		Account account14 = new Account();
		account14.setAccountNumber(IbanGenerator.generateValidIban());
		account14.setSolde(1000.00);
		accountRepository.save(account14);

		Account account15 = new Account();
		account15.setAccountNumber(IbanGenerator.generateValidIban());
		account15.setSolde(1000.00);
		accountRepository.save(account15);

		Account account16 = new Account();
		account16.setAccountNumber(IbanGenerator.generateValidIban());
		account16.setSolde(1000.00);
		accountRepository.save(account16);

		Account account17 = new Account();
		account17.setAccountNumber(IbanGenerator.generateValidIban());
		account17.setSolde(1000.00);
		accountRepository.save(account17);

		Account account18 = new Account();
		account18.setAccountNumber(IbanGenerator.generateValidIban());
		account18.setSolde(1000.00);
		accountRepository.save(account18);

		Account account19 = new Account();
		account19.setAccountNumber(IbanGenerator.generateValidIban());
		account19.setSolde(1000.00);
		accountRepository.save(account19);

		Account account20 = new Account();
		account20.setAccountNumber(IbanGenerator.generateValidIban());
		account20.setSolde(1000.00);
		accountRepository.save(account20);
	}
}
