package cgb.transfer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cgb.transfer.service.BatchService;

@RestController
@RequestMapping("/api")
public class BatchRestController {
	@Autowired
	private BatchService batchService;

	@GetMapping("/start-async-task")
	public ResponseEntity<String> startAsyncTask() {
		batchService.executeBatch();
		return ResponseEntity.ok("Traitement asynchrone lancé.");
	}
}