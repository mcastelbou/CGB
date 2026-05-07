package cgb.transfer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cgb.transfer.dto.AccountRequest;
import cgb.transfer.exception.CustomerException;
import cgb.transfer.service.AccountService;

@RestController
@RequestMapping("/api/accounts")
public class AccountRestController {
	
	@Autowired
	private AccountService accountService;
	
	@GetMapping("/check/{idAccount}/customer/{idCustomer}")
	public ResponseEntity<?> checkAccountBelongsToCustomer(@PathVariable String idAccount, @PathVariable Long idCustomer) {
		try {
			AccountRequest account = accountService.checkAccountWithCustomer(idAccount, idCustomer);
			return ResponseEntity.ok(account);
		} catch (CustomerException e) {
			TransferResponse errorResponse = new TransferResponse("FAILURE", e.getMessage());
			return ResponseEntity.badRequest().body(errorResponse);
		}
	}

}
