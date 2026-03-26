package cgb.transfer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

import cgb.transfer.entity.Account;
import cgb.transfer.entity.Transfer;
import cgb.transfer.exception.DeleteTransferException;
import cgb.transfer.repository.AccountRepository;
import cgb.transfer.repository.TransferRepository;
import cgb.transfer.service.TransferService;

@ExtendWith(MockitoExtension.class)
public class TransferServiceUnitTest {
	
	private static Transfer mockTransfer;
	private static Account sourceAccount;
	private static Account destinationAccount;

	@Mock
	private AccountRepository accountRepository;
	
	@Mock
	private TransferRepository transferRepository;
	
	@InjectMocks
	private TransferService transferService;
	
	@BeforeAll
	static void initObjects() {
		mockTransfer = new Transfer();
		mockTransfer.setSourceAccountNumber("123456789");
		mockTransfer.setDestinationAccountNumber("567891234");
		mockTransfer.setAmount(2.00);
		mockTransfer.setTransferDate(LocalDate.now());
		mockTransfer.setDescription("Deux euros!");
		
		sourceAccount = new Account();
		sourceAccount.setAccountNumber("123456789");
		sourceAccount.setSolde(500.00);
		
		destinationAccount = new Account();
		destinationAccount.setAccountNumber("567891234");
		destinationAccount.setSolde(500.00);
	}
	
	@Test
	void shouldReturnTransfer() {		
		when(accountRepository.findById("123456789")).thenReturn(Optional.of(sourceAccount));
		when(accountRepository.findById("567891234")).thenReturn(Optional.of(destinationAccount));
		
		when(transferRepository.save(Mockito.any(Transfer.class))).thenReturn(mockTransfer);
		
		Transfer transfer = transferService.createTransfer("123456789", "567891234", 2.00, LocalDate.now(), "Deux euros!");
		
		assertEquals("123456789", transfer.getSourceAccountNumber());
		assertEquals("567891234", transfer.getDestinationAccountNumber());
		assertEquals(2.00, transfer.getAmount());
		assertEquals(LocalDate.now(), transfer.getTransferDate());
		assertEquals("Deux euros!", transfer.getDescription());
		assertEquals(transfer, mockTransfer);
	}
	
	@Test
	void shouldDeleteTransfer() throws DeleteTransferException {
		when(transferRepository.findById(mockTransfer.getId())).thenReturn(Optional.of(mockTransfer));
		
		Transfer transfer = transferService.deleteTransfer(mockTransfer.getId());
		
		assertEquals(transfer, mockTransfer);
	}
}
