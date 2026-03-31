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
import cgb.transfer.exception.CreateTransferException;
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
		mockTransfer.setSourceAccountNumber("FR7618315100001028575571887");
		mockTransfer.setDestinationAccountNumber("FR7618315100000406690515531");
		mockTransfer.setAmount(2.00);
		mockTransfer.setTransferDate(LocalDate.now());
		mockTransfer.setDescription("Deux euros!");

		sourceAccount = new Account();
		sourceAccount.setAccountNumber("FR7618315100001028575571887");
		sourceAccount.setSolde(500.00);

		destinationAccount = new Account();
		destinationAccount.setAccountNumber("FR7618315100000406690515531");
		destinationAccount.setSolde(500.00);
	}

	@Test
	void shouldReturnTransfer_Success() throws CreateTransferException {
		when(accountRepository.findById("FR7618315100001028575571887")).thenReturn(Optional.of(sourceAccount));
		when(accountRepository.findById("FR7618315100000406690515531")).thenReturn(Optional.of(destinationAccount));

		when(transferRepository.save(Mockito.any(Transfer.class))).thenReturn(mockTransfer);

		Transfer transfer = transferService.createTransfer("FR7618315100001028575571887", "FR7618315100000406690515531",
				2.00, LocalDate.now(), "Deux euros!");

		assertEquals("FR7618315100001028575571887", transfer.getSourceAccountNumber());
		assertEquals("FR7618315100000406690515531", transfer.getDestinationAccountNumber());
		assertEquals(2.00, transfer.getAmount());
		assertEquals(LocalDate.now(), transfer.getTransferDate());
		assertEquals("Deux euros!", transfer.getDescription());
		assertEquals(transfer, mockTransfer);
	}

	@Test
	void shouldDeleteTransfer_Success() {
		when(transferRepository.findById(mockTransfer.getId())).thenReturn(Optional.of(mockTransfer));
		Transfer transfer = null;

		try {
			transfer = transferService.deleteTransfer(mockTransfer.getId());
		} catch (DeleteTransferException ex) {
			System.out.println(ex.getMessage());
		}

		assertEquals(transfer, mockTransfer);
	}

	@Test
	void shouldReturnTransfer_Failure() {
		// when(accountRepository.findById("FR7618315100001028575571887")).thenReturn(Optional.of(sourceAccount));
		// when(accountRepository.findById("FR7618315100000406690515531")).thenReturn(Optional.of(destinationAccount));

		assertThrows(CreateTransferException.class, () -> transferService.createTransfer("FR7618315100001028575571887",
				"XXXXXXXXXXXXXXXXXXXXXXXXXXX", 2.00, LocalDate.now(), "Deux euros!"));
		assertThrows(CreateTransferException.class, () -> transferService.createTransfer("XXXXXXXXXXXXXXXXXXXXXXXXXXX",
				"FR7618315100000406690515531", 2.00, LocalDate.now(), "Deux euros!"));
		assertThrows(CreateTransferException.class, () -> transferService.createTransfer("FR7618315100001028575571887",
				"FR7618315100000406690515531", 200000000000.00, LocalDate.now(), "Deux euros!"));
		assertThrows(CreateTransferException.class, () -> transferService.createTransfer("FR7618315100001028575571887",
				"FR7618315100000406690515531", 2.00, LocalDate.parse("2015-01-07"), "Deux euros!"));
	}

	@Test
	void shouldDeleteTransfer_Failure() {
		when(transferRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

		assertThrows(DeleteTransferException.class, () -> transferService.deleteTransfer(6767L));
	}
}
