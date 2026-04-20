package cgb.transfer.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cgb.transfer.dto.BatchRequest;
import cgb.transfer.entity.Batch;
import cgb.transfer.service.BatchService;

@RestController
@RequestMapping("/api/lots")
public class BatchRestController {
	
	@Autowired
	private BatchService batchService;

	@PostMapping
	public ResponseEntity<Batch> startAsyncTask(@RequestBody BatchRequest batchRequest) {
		Batch batch = batchService.createBatch(batchRequest);
		batchService.executeBatch(batch);
		return ResponseEntity.ok(batch);
	}
}