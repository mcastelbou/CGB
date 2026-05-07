package cgb.transfer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cgb.transfer.dto.AccountRequest;
import cgb.transfer.entity.Customer;
import cgb.transfer.exception.CustomerException;
import cgb.transfer.exception.CustomerException.CustomerFailure;
import cgb.transfer.repository.AccountRepository;
import cgb.transfer.repository.CustomerRepository;

@Service
public class AccountService {
	
	@Autowired
	private AccountRepository accountRepo;
	
	@Autowired
	private CustomerRepository customerRepo;
	
	public AccountRequest checkAccountWithCustomer(String accountNumber, Long idCustomer) throws CustomerException {
		Customer customer = customerRepo.findById(idCustomer).orElseThrow(() -> new CustomerException(CustomerFailure.CUSTOMER_NOT_FOUND));
		AccountRequest account = new AccountRequest();
		account.setAccountNumber(accountNumber);
		if (customer.getMyAccounts().contains(accountNumber)) {	
			account.setBelongsToCustomer(true);
		} else {
			account.setBelongsToCustomer(false);
		}
		return account;
	}

}
