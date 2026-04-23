package cgb.transfer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cgb.transfer.dto.BatchRequest;
import cgb.transfer.dto.BatchTransferRequest;
import cgb.transfer.entity.Batch;
import cgb.transfer.exception.CreateTransferException;
import cgb.transfer.service.BatchService;

@RestController
@RequestMapping("/api/batches")
public class BatchRestController {

	@Autowired
	private BatchService batchService;

	@PostMapping
	public ResponseEntity<?> startAsyncTask(@RequestBody BatchRequest batchRequest) {
		try {
			Batch batch = batchService.createBatch(batchRequest);
			List<BatchTransferRequest> transferList = batchRequest.getTransfers();

			batchService.executeBatch(batch.getRefBatch(), transferList);

			batchRequest.setStartDate(batch.getStartDate());
			batchRequest.setStatus("received");

			return ResponseEntity.ok(batchRequest);
		} catch (CreateTransferException e) {
			TransferResponse errorResponse = new TransferResponse("FAILURE", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
	}
}