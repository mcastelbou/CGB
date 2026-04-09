package cgb.transfer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cgb.transfer.repository.AccountRepository;
import cgb.transfer.repository.BatchRepository;
import cgb.transfer.repository.BatchTransferRepository;

@Service
public class BatchService {
	
	@Autowired
	private static AccountRepository accountRepo;
	
	@Autowired
	private static BatchRepository batchRepo;
	
	@Autowired
	private static BatchTransferRepository transferRepo;

	@Async
	public void executeBatch(BatchRequest batch) {
		//TODO
	}
	
	@Transactional
	public void createBatchTransfer() {
		//TODO
	}
}
