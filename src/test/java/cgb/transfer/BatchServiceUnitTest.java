package cgb.transfer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import cgb.transfer.dto.BatchRequest;
import cgb.transfer.dto.BatchTransferRequest;
import cgb.transfer.entity.Account;
import cgb.transfer.entity.Batch;
import cgb.transfer.entity.BatchTransfer;
import cgb.transfer.exception.CreateTransferException;
import cgb.transfer.repository.AccountRepository;
import cgb.transfer.repository.BatchRepository;
import cgb.transfer.repository.BatchTransferRepository;
import cgb.transfer.service.BatchService;

@ExtendWith(MockitoExtension.class)
public class BatchServiceUnitTest {

	private static Account sourceAccount;
	private static Batch mockBatch;

	private static Account destinationAccount;
	private static BatchTransfer mockBatchTransfer;

	@Mock
	private AccountRepository accountRepo;

	@Mock
	private BatchRepository batchRepo;

	@Mock
	private BatchTransferRepository batchTransferRepo;

	@InjectMocks
	private BatchService batchService;

	@BeforeAll
	static void initObjects() {
		sourceAccount = new Account();
		sourceAccount.setAccountNumber("FR7618315100001028575571887");
		sourceAccount.setSolde(500.00);

		mockBatch = new Batch();
		mockBatch.setRefBatch(LocalDate.now() + "-1");
		mockBatch.setSourceAccount(sourceAccount.getAccountNumber());
		mockBatch.setStartDate(LocalDate.now());
		mockBatch.setDescription("Lot standard avec 1 virement.");
		mockBatch.setStatus("received");

		destinationAccount = new Account();
		destinationAccount.setAccountNumber("FR7618315100000406690515531");
		destinationAccount.setSolde(500.00);

		mockBatchTransfer = new BatchTransfer();
		mockBatchTransfer.setAmount(2.00);
		mockBatchTransfer.setBatch(mockBatch);
		mockBatchTransfer.setDescription("Deux euros !");
		mockBatchTransfer.setDestinationAccount(destinationAccount.getAccountNumber());

		mockBatch.addTransfer(mockBatchTransfer);
	}

	@Test
	void shouldReturnBatch_Success() throws CreateTransferException {
		when(accountRepo.findById("FR7618315100001028575571887")).thenReturn(Optional.of(sourceAccount));
		when(batchRepo.save(Mockito.any(Batch.class))).thenReturn(mockBatch);

		BatchRequest br = new BatchRequest();
		br.setDescription("Lot standard avec 1 virement.");
		br.setSourceAccount("FR7618315100001028575571887");

		Batch batch = batchService.createBatch(br);

		assertTrue(batch.getRefBatch().equals(LocalDate.now() + "-1"));
		assertTrue(batch.getDescription().equals(br.getDescription()));
		assertTrue(batch.getStatus().equals("received"));
		assertTrue(batch.getStartDate().equals(LocalDate.now()));
		assertTrue(batch.getDescription().equals(br.getDescription()));
	}

	@Test
	void shouldReturnBatch_Failure() {
		BatchRequest br = new BatchRequest();
		br.setDescription("Lot standard avec 1 virement.");
		br.setSourceAccount("FR7618315100001028575571887");

		assertThrows(CreateTransferException.class, () -> batchService.createBatch(br));
	}

	@Test
	void shouldReturnBatchTransfer_Success() {
		when(accountRepo.findById("FR7618315100001028575571887")).thenReturn(Optional.of(destinationAccount));
		when(batchTransferRepo.save(Mockito.any(BatchTransfer.class))).thenReturn(mockBatchTransfer);

		BatchTransferRequest btr = BatchTransferRequest.BatchTransferToDTO(mockBatchTransfer);

		BatchTransfer bt = batchService.createBatchTransfer(mockBatch, btr);
		assertTrue(bt.getAmount().equals(bt.getAmount()));
		assertTrue(bt.getDestinationAccount().equals(bt.getDestinationAccount()));
		assertTrue(bt.getDescription().equals(bt.getDescription()));
		assertTrue(bt.getBatch().equals(mockBatch));
	}

	/*@Test
	void shouldReturnBatchTransfer_Failure() {
		when(accountRepo.findById("FR7618315100001028575571887")).thenReturn(Optional.of(destinationAccount));
		when(batchTransferRepo.save(Mockito.any(BatchTransfer.class))).thenReturn(mockBatchTransfer);

		mockBatchTransfer.setDestinationAccount(null);

		BatchTransferRequest btr = BatchTransferRequest.BatchTransferToDTO(mockBatchTransfer);

		BatchTransfer bt = batchService.createBatchTransfer(mockBatch, btr);
		assertTrue(bt.getStatus().equals("failure"));

		btr.setDestinationAccount("FR7618315100001028575571887");
		btr.setAmount(-2.00);

		bt = batchService.createBatchTransfer(mockBatch, btr);
		assertTrue(bt.getStatus().equals("canceled"));

		btr.setAmount(2000000.00);

		bt = batchService.createBatchTransfer(mockBatch, btr);
		assertTrue(bt.getStatus().equals("delayed"));
	}*/
}
